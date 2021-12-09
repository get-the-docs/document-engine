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

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javassist.util.proxy.ProxyFactory;
import net.videki.templateutils.template.core.context.dto.JsonModel;
import net.videki.templateutils.template.core.context.dto.TemplateContext;

import org.apache.commons.lang3.StringUtils;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
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
    private static final ObjectMapper mapper = new ObjectMapper();

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
            return build(mapper.writeValueAsString(data));
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

            final Map<?, ?> objContext = mapper.readValue(data, Map.class);

            LOGGER.trace("DTO parse ok, creating proxy...");

//            final PropertyClassHolder dtoClass = getObjectForContext(TemplateContext.class, "Dto",
//                    objContext, null, null);
            final PropertyClassHolder dtoClass = getObjectForContext(TemplateContext.class, "Dto",
                    objContext, null, null);

            LOGGER.trace("Proxy build successful, deserializing data.");

            return mapper.readValue(data, dtoClass.getHolderClassBuilder().toClass(
                TemplateContext.class.getClassLoader(), TemplateContext.class.getProtectionDomain()));

        } catch (final JsonProcessingException | CannotCompileException e) {
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
                        }
                    }
                    reversedProperties.add(new PropertyClassHolder(actPropertyName, LinkedList.class));
                } else if (actValue instanceof String) {
                    reversedProperties.add(new PropertyClassHolder(actPropertyName, String.class));
                } else if (actValue instanceof Integer) {
                    reversedProperties.add(new PropertyClassHolder(actPropertyName, Integer.class));
                } else if (actValue instanceof Double) {
                    reversedProperties.add(new PropertyClassHolder(actPropertyName, Double.class));
                } else if (actValue instanceof Boolean) {
                    reversedProperties.add(new PropertyClassHolder(actPropertyName, Boolean.class));
                } else {
                    LOGGER.error("Unhandled data type caught: {}", actValue.getClass());
                }

            }

            return generateClass(baseClass, propertyName, reversedProperties, aggregatorType);

        } catch (final Exception e) {
            final var msg = String.format("Error reading building the context object. The last seen entry is: %s",
                    propertyName);

            throw new TemplateServiceRuntimeException(msg, e);
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
                final Class<?> holderClass = entry.getHolderClass() == null
                        ? entry.getHolderClassBuilder().toClass(TemplateContext.class.getClassLoader(),
                                TemplateContext.class.getProtectionDomain())
                        : entry.getHolderClass();
                LOGGER.trace("Property: {} - class: {}", entry.getPropertyName(), holderClass);

                cc.addField(new CtField(resolveCtClass(holderClass), entry.getPropertyName(), cc));
                cc.addMethod(generateGetter(cc, entry.getPropertyName(), holderClass));
                cc.addMethod(generateSetter(cc, entry.getPropertyName(), holderClass));

                LOGGER.trace("Added field to type: {}.{}", className, actPropertyName);
            }
        }

        return new PropertyClassHolder(className, null, cc, properties);
    }

    /**
     * Generates a getter method for a property.
     * 
     * @param declaringClass the class definition to add the getter to.
     * @param fieldName      the property name.
     * @param fieldClass     the field type.
     * @return a method object holding the getter method.
     * @throws CannotCompileException thrown in case of compilation errors when
     *                                generating the getter method.
     */
    private static CtMethod generateGetter(final CtClass declaringClass, final String fieldName,
            final Class<?> fieldClass) throws CannotCompileException {

        final String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        final String sb = "public " + fieldClass.getName() + " " + getterName + "(){" + "return this." + fieldName + ";"
                + "}";

        return CtMethod.make(sb, declaringClass);
    }

    /**
     * Generates a setter method for a property.
     * 
     * @param declaringClass the class definition to add the setter to.
     * @param fieldName      the property name.
     * @param fieldClass     the field type.
     * @return a method object holding the getter method.
     * @throws CannotCompileException thrown in case of compilation errors when
     *                                generating the setter method.
     */
    private static CtMethod generateSetter(final CtClass declaringClass, final String fieldName,
            final Class<?> fieldClass) throws CannotCompileException {

        final String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        final String sb = "public void " + setterName + "(" + fieldClass.getName() + " " + fieldName + ")" + "{"
                + "this." + fieldName + "=" + fieldName + ";" + "}";

        return CtMethod.make(sb, declaringClass);
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
