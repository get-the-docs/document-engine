package net.videki.templateutils.template.core.context;

/*-
 * #%L
 * template-utils-core
 * %%
 * Copyright (C) 2021 Levente Ban
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javassist.*;
import javassist.bytecode.Descriptor;
import javassist.bytecode.SignatureAttribute;
import javassist.util.proxy.ProxyFactory;
import net.videki.templateutils.template.core.context.dto.JsonModel;
import net.videki.templateutils.template.core.context.dto.JsonValueObject;

import net.videki.templateutils.template.core.context.dto.TemplateContext;
import org.apache.commons.lang3.StringUtils;

import net.videki.templateutils.template.core.context.dto.PropertyClassHolder;
import net.videki.templateutils.template.core.dto.extensions.ITemplate;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy class builder for JSON DTOs to inject into as typed elements into the
 * template processors inorder to use EL expression evaluation without
 * explicitly providing the DTO types as a dependency. Objects will be generated
 * as descendants of the JsonTemplateContext class to provide both typed and
 * json path capabilities.
 *
 * @author Levente Ban
 */
public class ContextObjectProxyBuilder {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextObjectProxyBuilder.class);

    /** Json mapper */
    private static final ObjectMapper mapper = new JsonMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Entry point to generate and instantiate the holder object for a given DTO.
     * 
     * @param data the value object as json string
     * @return the generated object.
     */
    public static Object build(final Map<?, ?> data) {

        if (data == null) {
            LOGGER.trace("Null input caught to create a proxy object.");
            return null;
        }

        try {
            return build(mapper.writer().writeValueAsString(data));
        } catch (JsonProcessingException e) {
            final String msg = "Error reading the DTO caught.";
            LOGGER.error(msg, e);

            throw new TemplateServiceRuntimeException(msg, e);
        }

    }

    /**
     * Entry point to generate and instanciate the holder object for a given DTO.
     * 
     * @param data the value object as json string
     * @return the generated object.
     */
    public static Object build(final String data) {
        try {
            if (StringUtils.isBlank(data)) {
                LOGGER.trace("Null input caught to create a proxy object.");
                return null;
            }

            LOGGER.debug("Building a proxy object for the DTO.");
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("DTO raw data: {}", data);
            }

            Map<?, ?> objContext = mapper.reader().readValue(data, Map.class);

            LOGGER.trace("DTO parse ok, creating proxy...");

//            final PropertyClassHolder dtoClass = getObjectForContext(TemplateContext.class, "Dto",
//                    objContext, null, null);
            final PropertyClassHolder dtoClass = getObjectForContext(JsonValueObject.class, "Dto",
                    objContext, null, null);

            LOGGER.trace("Proxy build successful, deserializing data.");

            final var dtoc = dtoClass.getHolderClassBuilder().toClass(
                    JsonValueObject.class.getClassLoader(), JsonValueObject.class.getProtectionDomain());
            return mapper.reader().readValue(data, dtoc);

        } catch (final CannotCompileException | IOException e) {
            final String msg = "Error reading the DTO caught.";
            LOGGER.error(msg, e);

            throw new TemplateServiceRuntimeException(msg, e);
        }

    }

    /**
     * Creates a holder class definition for the given context (segment of the
     * original json).
     * 
     * @param baseClass                    the base class to derive the tmp class
     *                                     from.
     * @param propertyName                 the actual property name in the json
     *                                     holding the context.
     * @param context                      the context (the parsed json part as
     *                                     map).
     * @param ctClass                      the aggregator class for lists, null
     *                                     otherwise.
     * @param reversedAggregatorProperties the aggregated property list for list
     *                                     types, null otherwise (is handled with
     *                                     ctClass together).
     * @return A property class holder object with either type's class, or in case
     *         of generated types its holder factory for further property
     *         aggregation (see null values in list items).
     */
    protected static PropertyClassHolder getObjectForContext(final Class<?> baseClass, final String propertyName,
            final Map<?, ?> context, final CtClass ctClass,
            final List<PropertyClassHolder> reversedAggregatorProperties) {
        try {

            final List<PropertyClassHolder> reversedProperties = new LinkedList<>();
            if (reversedAggregatorProperties != null) {
                reversedProperties.addAll(reversedAggregatorProperties);
            }
            CtClass aggregatorType = ctClass;

            for (final var actProperty : context.keySet()) {

                final String actPropertyName = (String) actProperty;
                final Object actValue = context.get(actProperty);

                if (actValue instanceof Map) {
                    LOGGER.trace("Found property: " + actPropertyName);

                    final PropertyClassHolder propHolder = getObjectForContext(baseClass, actPropertyName,
                            (Map<?, ?>) actValue, null, null);
                    reversedProperties.add(propHolder);
                } else if (actValue instanceof List) {
                    LOGGER.trace("Found property: List: " + actPropertyName);

                    List<PropertyClassHolder> tmpAggregatedProperties = null;
                    for (final Object actItem : (List<?>) actValue) {
                        if (actItem instanceof Map) {
                            final PropertyClassHolder propHolder = getObjectForContext(baseClass, actPropertyName,
                                    (Map<?, ?>) actItem, aggregatorType, tmpAggregatedProperties);
                            aggregatorType = propHolder.getHolderClassBuilder();
                            tmpAggregatedProperties = propHolder.getHolderProperties();
//                        } else {
//                            addPropertyIfNotExists(reversedProperties, new PropertyClassHolder("value", Object.class,
//                                    null, null, null));

                        }
                    }

                    final PropertyClassHolder listItemHolder;
                    if (tmpAggregatedProperties != null) {
                        listItemHolder = generateClass(baseClass, actPropertyName, tmpAggregatedProperties, aggregatorType);
                    } else {
                        listItemHolder = new PropertyClassHolder("value", Object.class);
                    }
                    addPropertyIfNotExists(reversedProperties, new PropertyClassHolder(actPropertyName, LinkedList.class,
                            null, listItemHolder.getHolderClass(), null));

                } else if (actValue instanceof String) {
                    addPropertyIfNotExists(reversedProperties, new PropertyClassHolder(actPropertyName, String.class));
                } else if (actValue instanceof Integer) {
                    addPropertyIfNotExists(reversedProperties, new PropertyClassHolder(actPropertyName, Integer.class));
                } else if (actValue instanceof Double) {
                    addPropertyIfNotExists(reversedProperties, new PropertyClassHolder(actPropertyName, Double.class));
                } else if (actValue instanceof Boolean) {
                    addPropertyIfNotExists(reversedProperties, new PropertyClassHolder(actPropertyName, Boolean.class));
                } else {
                    LOGGER.error("Unhandled data type caught: {}", actValue.getClass());
                }

            }

            return generateClass(baseClass, propertyName, reversedProperties, ctClass);

        } catch (final Exception e) {
            final var msg = String.format("Error reading building the context object. The last seen entry is: %s",
                    propertyName);

            throw new TemplateServiceRuntimeException(msg, e);
        }
    }

    private static void addPropertyIfNotExists(final List<PropertyClassHolder> reversedProperties, final PropertyClassHolder property) {
        if (reversedProperties != null && property != null) {
            if (reversedProperties
                    .stream()
                    .noneMatch(p -> property.getPropertyName().equalsIgnoreCase(p.getPropertyName()))) {
                reversedProperties.add(property);
            }
        }
    }

    /**
     * Generates a class for a given property holder descriptor.
     * 
     * @param baseClass      the base class to derive the class from.
     * @param className      the actual class name to be generated (will be
     *                       normailzed and unified).
     * @param properties     the property set for back propagation in case of lists.
     * @param aggregatorType the class generated for the items.
     * @return a property holder object containing the compiled class.
     * @throws NotFoundException      thrown if a referred type within the property
     *                                holders cannot be found.
     * @throws CannotCompileException thrown in case of class compilation errors
     *                                when generating the holder type (indicates a
     *                                mapping error in the proxy builder logic).
     */
    public static PropertyClassHolder generateClass(final Class<?> baseClass, final String className,
            final List<PropertyClassHolder> properties, final CtClass aggregatorType)
            throws NotFoundException, CannotCompileException {

        final String actClassName = StringUtils.capitalize(className);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Generating class {}{}", baseClass, actClassName);
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Generating class with field set: {}.{} - properties: [{}]", baseClass.getName(), className, properties);
        }

        CtClass cc;
        if (aggregatorType == null) {
            ClassPool pool = ClassPool.getDefault();
            final String effectiveClassName = ProxyFactory.nameGenerator.get(baseClass.getName() + "$" + actClassName);
            cc = pool.makeClass(effectiveClassName);

            cc.setSuperclass(pool.get(baseClass.getName()));

            cc.addInterface(resolveCtClass(ITemplate.class));
            cc.addInterface(resolveCtClass(JsonModel.class));

            cc.addConstructor(CtNewConstructor.defaultConstructor(cc));

            LOGGER.trace("New type, the effective class name is: {}", effectiveClassName);
        } else {
            cc = aggregatorType;

            LOGGER.trace("Extending previously composed class: {}", cc.getName());
        }

        for (final PropertyClassHolder entry : properties) {
            final String actPropertyName = entry.getPropertyName();
            try {
                cc.getField(actPropertyName);
                LOGGER.trace("Field: {} exists.", actPropertyName);
            } catch (final NotFoundException e) {

                final Class<?> itemParameterType = entry.getItemParameterType();
                final Class<?> holderClass = entry.getHolderClass();
                LOGGER.trace("Property: {} - class: {}", entry.getPropertyName(), holderClass);

                cc.addField(generateField(holderClass, itemParameterType, entry, cc));
                cc.addMethod(generateGetter(cc, entry.getPropertyName(), holderClass, itemParameterType));
                cc.addMethod(generateSetter(cc, entry.getPropertyName(), holderClass, itemParameterType));

                LOGGER.trace("Added field to type: {}.{}", className, actPropertyName);
            }
        }

        return new PropertyClassHolder(className, null, cc, null, properties);
    }

    /**
     * Generates a field for a property.
     *
     * @param holderClass           property type.
     * @param holderPropertiesClass generic type for the property in case of lists.
     * @param entry                 the property holder.
     * @param cc                    the enclosing class factory.
     * @return a field object holding the property.
     * @throws CannotCompileException thrown in case of compilation errors when
     *                                generating the field.
     */
    private static CtField generateField(final Class<?> holderClass, final Class<?> holderPropertiesClass,
                                         final PropertyClassHolder entry, final CtClass cc) throws CannotCompileException, NotFoundException {

        final CtField actField  = new CtField(resolveCtClass(holderClass), entry.getPropertyName(), cc);

        if (List.class.isAssignableFrom(holderClass)) {
            actField.setGenericSignature(getGenericSignature(holderClass, holderPropertiesClass));
        }

        return actField;
    }

    /**
     * Generates a getter method for a property.
     * 
     * @param declaringClass     the class definition to add the getter to.
     * @param fieldName          the property name.
     * @param fieldClass         the field type.
     * @param itemParameterClass generic type for the property in case of lists.
     * @return a method object holding the getter method.
     * @throws CannotCompileException thrown in case of compilation errors when
     *                                generating the getter method.
     */
    private static CtMethod generateGetter(final CtClass declaringClass, final String fieldName,
            final Class<?> fieldClass, final Class<?> itemParameterClass) throws CannotCompileException {

        final String paramType = fieldClass.getName();
        final String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        final String sb = "public " + paramType + " " + getterName + "(){" + "return this." + fieldName + ";"
                + "}";

        final CtMethod method = CtMethod.make(sb, declaringClass);
        setParamTypeSignature(method, fieldClass, itemParameterClass, false);

        return method;
    }

    /**
     * Generates a setter method for a property.
     * 
     * @param declaringClass     the class definition to add the setter to.
     * @param fieldName          the property name.
     * @param fieldClass         the field type.
     * @param itemParameterClass generic type for the property in case of lists.
     * @return a method object holding the getter method.
     * @throws CannotCompileException thrown in case of compilation errors when
     *                                generating the setter method.
     */
    private static CtMethod generateSetter(final CtClass declaringClass, final String fieldName,
            final Class<?> fieldClass, final Class<?> itemParameterClass) throws CannotCompileException {

        final String paramType = fieldClass.getName();
        final String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        final String sb = "public void " + setterName + "(" + paramType + " " + fieldName + ")" + "{"
                + "this." + fieldName + "=" + fieldName + ";" + "}";

        final CtMethod method = CtMethod.make(sb, declaringClass);
        setParamTypeSignature(method, fieldClass, itemParameterClass, true);

        return method;
    }

    /**
     * Adjusts the param type for the accessors.
     *
     * @param method             the accessor method.
     * @param fieldClass         the parameter type.
     * @param itemParameterClass the generic type in case of lists.
     * @param isSetter           true for setter method, false: getter (the signature is different for obvious reasons).
     */
    private static void setParamTypeSignature(final CtMethod method,
                                                final Class<?> fieldClass,
                                                final Class<?> itemParameterClass,
                                                final boolean isSetter) {
        if (List.class.isAssignableFrom(fieldClass)) {

            String signatureType = getGenericSignature(fieldClass, itemParameterClass);

            if (isSetter) {
                // (Ljava/util/List<Lnet/videki/templateutils/template/core/context/MyItem;>;)V
                signatureType = "(" + signatureType + ")V";
            } else {
                // ()Ljava/util/List<Lnet/videki/templateutils/template/core/context/MyItem;>;
                signatureType = "()" + signatureType;
            }

            method.setGenericSignature(signatureType);
        }
    }

    /**
     * Returns the generic signature for parameterized types.
     *
     * @param fieldClass         the parameter type.
     * @param itemParameterClass the generic type in case of lists.
     * @return the type signature as string
     */
    private static String getGenericSignature(final Class<?> fieldClass,
                                              final Class<?> itemParameterClass) {
        String signatureType = "";
        final SignatureAttribute.ClassType listItemType = new SignatureAttribute.ClassType(fieldClass.getName(),
                new SignatureAttribute.TypeArgument[]{
                        new SignatureAttribute.TypeArgument( new SignatureAttribute.ClassType(itemParameterClass.getName()) )});
        signatureType = listItemType.encode();

        return signatureType;
    }

    /**
     * Returns the class definition for a given class.
     * 
     * @param clazz the class to be generated/modified.
     * @return the type factory instance.
     * @throws NotFoundException thrown if the given class cannot be found.
     */
    private static CtClass resolveCtClass(final Class<?> clazz) throws NotFoundException {
        final ClassPool pool = ClassPool.getDefault();

        return pool.get(clazz.getName());
    }
}
