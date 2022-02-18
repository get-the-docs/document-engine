package net.videki.templateutils.template.core.context.dto;

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

import javassist.CannotCompileException;
import javassist.CtClass;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;

import java.util.List;

/**
 * Holder object for generated classes. The type Basically holds property-data
 * type pairs, where the type is either an arbitary type, or a generated one.
 * For the latter case it also holds the class factory and the property holder
 * list to deal with null values in lists the holder properties are aggregated
 * by traversing the value list (the enclosing class will contain the
 * composition of the list items).
 * 
 * @author Levente Ban
 */
public class PropertyClassHolder {

    /** The property name */
    private final String propertyName;

    /** Holder class list for array types */
    private final List<PropertyClassHolder> holderProperties;

    /** The property value class for existing classes */
    private Class<?> holderClass;

    /** The property value for generated classes (editable) */
    private CtClass holderClassBuilder;

    /** The item parameter type in case of parameterized types like lists */
    private final Class<?> itemParameterType;

    /**
     * Constructor for existing class based properties.
     * 
     * @param name  the property name.
     * @param clazz the value class.
     */
    public PropertyClassHolder(final String name, final Class<?> clazz) {
        this(name, clazz, null, null, null);
    }

    /**
     * Constructor for generated properties.
     * 
     * @param name               the property name.
     * @param clazz              the value class.
     * @param holderClassBuilder class factory for generated classes.
     * @param holderProperties   holder properties for generated classes.
     */
    public PropertyClassHolder(final String name, final Class<?> clazz, final CtClass holderClassBuilder,
            final Class<?> itemParameterType, final List<PropertyClassHolder> holderProperties) {

        if (name == null || name.isBlank()) {
            throw new TemplateServiceRuntimeException("Empty property name caught for class generation.");
        }
        if (clazz == null && holderClassBuilder == null) {
            throw new TemplateServiceRuntimeException("Empty type caught for class generation.");
        }

        this.propertyName = name;
        this.holderClass = clazz;
        this.holderClassBuilder = holderClassBuilder;
        this.itemParameterType = itemParameterType;
        this.holderProperties = holderProperties;
    }

    /**
     * Returns the property name.
     * 
     * @return the property name.
     */
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * Returns the holder class.
     * 
     * @return the holder class.
     */
    public Class<?> getHolderClass() throws CannotCompileException {

        if (this.holderClassBuilder != null) {
            this.holderClass = this.holderClassBuilder.toClass(JsonValueObject.class.getClassLoader(),
                    JsonValueObject.class.getProtectionDomain());
            this.holderClassBuilder = null;
        }

        return this.holderClass;
    }

    /**
     * Returns the class factory for the property, if given.
     * 
     * @return the class factory.
     */
    public CtClass getHolderClassBuilder() {
        return this.holderClassBuilder;
    }


    /**
     * Returns the item parameter type for the holderClass.
     *
     * @return the item parameter type.
     */
    public Class<?> getItemParameterType() {
        return this.itemParameterType;
    }

    /**
     * Returns the property holder list for generated list element types.
     * 
     * @return the property holder list.
     */
    public List<PropertyClassHolder> getHolderProperties() {
        return this.holderProperties;
    }

}
