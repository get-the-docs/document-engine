package net.videki.templateutils.template.core.context;

/*-
 * #%L
 * docs-core
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

import net.videki.templateutils.template.core.dto.ITemplate;
import net.videki.templateutils.template.core.dto.JsonModel;

import java.util.List;

/**
 * Abstract value holder for expression based actual model objects.
 * It implements the ITemplate interface to provide the reflection based convinience methods and the json capabilities. 
 * Usage: for literal values the getValue() getter, for value lists the getItems().
 *  
 * @author Levente Ban
 */
public abstract class ContextObject implements ITemplate, JsonModel {

    /**
     * Returns a literal value described by the expression defined, if assigned to the object.
     * @param expression the expression to return a literal.
     * @return the expression result object.
     */
    public abstract Object getValue(String expression);

    /**
     * Returns the list of embedded objects, if persent.
     * @param expression the expression to return a value list.
     * @return the list of objects as a result of the expression evaluation provided by the actual parameter.
     */
    public abstract List<Object> getItems(String expression);
}
