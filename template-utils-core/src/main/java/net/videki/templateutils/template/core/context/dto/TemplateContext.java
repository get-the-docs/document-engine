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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONValue;
import net.videki.templateutils.template.core.context.ContextObjectProxyBuilder;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * JSON template model provider implementation to provide non-reflection based model data.
 * You can initialize the actual model context with a JSON string, then query JSONpath based values or value lists of it. 
 * Usage: Use the class methods in the placeholder parameters of the model value getters in the document processors.
 * 
 * @author Levente Ban
 */
public class TemplateContext implements IJsonTemplate, JsonModel {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextObjectProxyBuilder.class);

    @JsonIgnore
    private static final JsonMapper mapper = new JsonMapper();

    @JsonIgnore
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    @JsonIgnore
    private static final Lock writeLock = lock.writeLock();

    /**
     * Context root object key.
     */
    @JsonIgnore
    public static final String CONTEXT_ROOT_KEY = "ctx";

    /**
     * The model context.
     * It is a key-value store to hold objects.
     */
    private final Map<String, Object> ctx = new HashMap<>();

    /**
     * Document context from the caught JSON.
     */
    @JsonIgnore
    private transient DocumentContext dc;

    /**
     * The actual json data as plain string.
     */
    @JsonIgnore
    private String jsonData;

    /**
     * Initializes an empty context object.
     */
    public TemplateContext() {
        this.jsonData = null;
        this.dc = null;
    }

    protected void init(final String data) {
        this.jsonData = data;

        try {
            LOGGER.trace("Parsing context data...");

            this.dc = JsonPath.using(Configuration.defaultConfiguration()).parse(data);

            // 1. Process value objects as array and add them to the root context
            if (dc.json() instanceof JSONArray) {
                final JSONArray inputObjectArray = dc.json();
                for (final var actRoot : inputObjectArray) {
                    final Map<?, ?> propertiesArray = ((Map<?, ?>) actRoot);
                    buildObjectsFromJsonMap(propertiesArray);
                }
            } else {
                withContext(TemplateContext.CONTEXT_ROOT_KEY, ContextObjectProxyBuilder.build(padDocumentContext(data)));
            }

            // 2. Align the input json with the root context to be in sync with the jsonpath
//            this.toJson();
//            this.jsonData = padDocumentContext(this.jsonData).replaceAll("^(\\{\"ctx\"\\s*:\\s*\\[)([.\\s\\S]*)(\\]\\s*\\})$", "{\"ctx\":  $2}");
//            this.dc = JsonPath.using(Configuration.defaultConfiguration()).parse(this.jsonData);

            LOGGER.trace("Context parse successful");
        } catch (final Exception e) {
            final var msg = "Error reading JSON data";

            LOGGER.error(msg);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Value object to parse: {}", data);
            }

            throw new TemplateServiceRuntimeException(msg, e);
        }

    }

    private void buildObjectsFromJsonMap(final Map<?, ?> propertiesArray) {
        for (final var actContextKey : propertiesArray.keySet()) {
            final var actObject = propertiesArray.get(actContextKey);
            buildObject(actContextKey.toString(), actObject);
        }
    }

    private void buildObject(final String contextKey, final Object actItem) {
        final StringWriter sw = new StringWriter();
        try {
            JSONValue.writeJSONString(actItem, sw);
        } catch (final IOException e) {
            throw new TemplateServiceRuntimeException("Error parsing data", e);
        }
        withContext(contextKey, ContextObjectProxyBuilder.build(sw.toString()));
    }

    /**
     * Returns the model object for the root context.
     * Use this method in the placeholder for single model templates.
     * @return the root context's model object.
     */
    @JsonIgnore
    public Object getModel() {
        return this.ctx.get(CONTEXT_ROOT_KEY);
    }

    /**
     * Returns the context objects as a map for multi-object models provided for template processors to fill-in values.
     * @return the list of model objects.
     */
    public Map<String, Object> getCtx() {
        return this.ctx;
    }

    public <T> TemplateContext withContext(T value) {
        if (value instanceof String) {
            init((String)value);
        } else {
            this.ctx.put(CONTEXT_ROOT_KEY, value);
        }

        return this;
    }

    public <T> TemplateContext withContext(String contextKey, T value) {
        this.ctx.put(contextKey, value);

        return this;
    }

    /**
     * Returns a single value based on the JSON path caught.
     * @param path the JSONpath to be evaluated.
     * @return the context object if found by the given JSON path.
     */
    public Object getValue(final String path) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Getting data for path {}...", path);
        }
        try {
            if (this.dc == null) {
                this.build();
            }
            final Object value = this.dc.read(JsonValueObject.JSONPATH_PREFIX + path);

            if (value instanceof String) {
                return value;
            } else if (value instanceof Map) {
                final StringWriter sw = new StringWriter();
                try {
                    JSONValue.writeJSONString(value, sw);
                } catch (final IOException e) {
                    throw new TemplateServiceRuntimeException("Error parsing data.", e);
                }

                return ContextObjectProxyBuilder.build(padDocumentContext(sw.toString()));

            } else if (value instanceof JSONArray) {
                final List<Object> results = new ArrayList<>(((JSONArray) value).size());
                for(final Object actItem : ((JSONArray) value)) {
                    final StringWriter sw = new StringWriter();
                    try {
                        JSONValue.writeJSONString(actItem, sw);
                    } catch (final IOException e) {
                        throw new TemplateServiceRuntimeException("Error parsing data.", e);
                    }
                    results.add(ContextObjectProxyBuilder
                            .build(padDocumentContext(sw.toString())));
                }
                return results;
            }

            return value;
        } catch (final Exception e) {
            final var msg = String.format("Error reading JSON data. Path: %s", path);

            LOGGER.error(msg);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.debug("Value object to parse: {}", this.jsonData);
            }

            throw new TemplateServiceRuntimeException(msg, e);
        }
    }

/*    private DocumentContext parseDocumentContext(final String data) {
        DocumentContext localDocumentContext = JsonPath.using(Configuration.defaultConfiguration()).parse(data);

        if (localDocumentContext.json() instanceof JSONArray) {
            localDocumentContext =
                    JsonPath.using(Configuration.defaultConfiguration()).parse(
                            "{\"" + TemplateContext.CONTEXT_ROOT_KEY + "\": " + data.replace() + "}");
        }
        return localDocumentContext;
    }
*/
    private String padDocumentContext(final String data) {
        if (data != null) {
            return data.replaceAll("^(\\[)([.\\s\\S]*)(\\])$", "{\"ctx\": [ $2 ]}");
        } else {
            return null;
        }
    }

    /**
     * Returns a list of values based on the JSON path caught.
     * @param path the JSONpath to be evaluated.
     * @return the list of context object if found by the given JSON path.
     */
    public List<Object> getItems(final String path) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Getting items for path {}...", path);
        }
        return this.dc.read(JsonValueObject.JSONPATH_PREFIX + path);
    }

    /**
     * Template placeholder convenience mehod to return a single context object based on the caught path. 
     * @param path the JSON path on the actual model.
     * @return the result context object if found.
     */
    public Object jsonpath(final String path) {
        return getValue(path);
    }


    /**
     * Template placeholder convenience mehod to return a single context object based on the caught path. 
     * @param path the JSON path on the actual model.
     * @return the result context object if found.
     */
    public Object getJsonpath(final String path) {
        return jsonpath(path);
    }

    /**
     * Template placeholder convenience mehod to return a single context object based on the caught path. 
     * @param path the JSON path on the actual model.
     * @return the result context object if found.
     */
    public Object jp(final String path) {
        return jsonpath(path);
    }

    /**
     * Template placeholder convenience mehod to return a list of context object based on the caught path. 
     * @param path the JSON path on the actual model.
     * @return the result list of context objects if found.
     */
    public List<Object> items(final String path) {
        return getItems(path);
    }

    /**
     * Returns the original JSON string to build the context.
     * @return the JSON string.
     */
    @JsonIgnore
    public String getData() {
        return this.jsonData;
    }

    public TemplateContext build() {
        
        if (this.jsonData == null) {
            init(this.toJson());
        }

        return this;
    }
    /**
     * Returns the original JSON string to build the context.
     * @return the JSON string.
     */
    @JsonIgnore
    @Override
    public String toJson() {
        String result;

        try {
            writeLock.lock();

            try {
                result = this.jsonData = mapper.writer().writeValueAsString(this);
                this.dc = JsonPath.using(Configuration.defaultConfiguration()).parse(result);
            } catch (final JsonProcessingException e) {
                result = this.jsonData = null;
                LOGGER.warn("Error serializing the template context", e);
            }
        } finally {
            writeLock.unlock();
        }

        return result;

    }
}
