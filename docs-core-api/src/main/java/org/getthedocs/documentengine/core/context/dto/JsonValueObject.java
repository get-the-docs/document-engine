package org.getthedocs.documentengine.core.context.dto;

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

import org.getthedocs.documentengine.core.service.exception.TemplateServiceRuntimeException;
import org.getthedocs.documentengine.core.context.ContextObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * JSON value object to represent JSON data as a model object.
 * 
 * @author Levente Ban
 */
public class JsonValueObject extends ContextObject {

    /**
     * Class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextObject.class);

    /**
     * JSON path prefix for expression evaluation. 
     */
    private static final String JSONPATH_PREFIX = "$.";

    /**
     * Document context from the caught JSON.
     */
    private final transient DocumentContext dc;

    /**
     * The actual json data as plain string.
     */
    private final String data;

    /**
     * Initializes the context with a JSON as string.
     * @param dc the JSON data as a string.
     * @throws TemplateServiceRuntimeException on parse errors.
     */
    public JsonValueObject(final String dc) {
        try {
            this.dc = JsonPath.using(Configuration.defaultConfiguration()).parse(dc);
            this.data = dc;
        } catch (final Exception e) {
            final var msg = "Error reading JSON data";

            LOGGER.error(msg);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Value object to parse: {}", dc);
            }

            throw new TemplateServiceRuntimeException(msg);
        }
    }

   /**
     * Returns a single value based on the JSON path caught.
     * @param path the JSONpath to be evaluated.
     * @return the model object if found by the given JSON path.
     */ 
    @Override
    public Object getValue(final String path) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Getting data for path {}...", path);
        }
        return this.dc.read(JSONPATH_PREFIX + path);
    }

    /**
     * Returns a list of values based on the JSON path caught.
     * @param path the JSONpath to be evaluated.
     * @return the list of model object if found by the given JSON path.
     */    
    @Override
    public List<Object> getItems(final String path) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Getting items for path {}...", path);
        }
        return this.dc.read(JSONPATH_PREFIX + path);
    }

    /**
     * Template placeholder convenience method to return a single model object based on the caught path.
     * @param path the JSON path on the actual model.
     * @return the result model object if found.
     */
    public Object jsonpath(final String path) {
        return getValue(path);
    }

    /**
     * Template placeholder convenience mehod to return a single model object based on the caught path. 
     * @param path the JSON path on the actual model.
     * @return the result model object if found.
     */
    public Object jp(final String path) {
        return getValue(path);
    }

    /**
     * Template placeholder convenience mehod to return a list of model object based on the caught path. 
     * @param path the JSON path on the actual model.
     * @return the result list of model objects if found.
     */    
    public List<Object> items(final String path) {
        return getItems(path);
    }

    /**
     * Returns the original JSON string to build the context.
     * @return the JSON string.
     */    
    public String getData() {
        return this.data;
    }

    /**
     * Returns the original JSON string to build the context.
     * @return the JSON string.
     */    
    @Override
    public String toJson() {
        return data;
    }
}
