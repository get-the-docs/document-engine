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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONValue;
import net.videki.templateutils.template.core.context.ContextObjectProxyBuilder;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON template model provider implementation to provide non-reflection based model data.
 * You can initialize the actual model context with a JSON string, then query JSONpath based values or value lists of it. 
 * Usage: Use the class methods in the placeholder parameters of the model value getters in the document processors.
 * 
 * @author Levente Ban
 */
@Slf4j
public class TemplateContext implements IJsonTemplate, JsonModel {

    /**
     * Context root object key.
     */
    public static final String CONTEXT_ROOT_KEY = "ctx";

    /**
     * The model context.
     * It is a key-value store to hold objects.
     */
    private Map<String, Object> ctx = new HashMap<>();

    /**
     * JSON path query prefix.
     */
    private static final String JSONPATH_PREFIX = "$.";

    /**
     * Document context from the caught JSON.
     */
    @JsonIgnore
    private transient DocumentContext dc;

    /**
     * The actual json data as plain string.
     */
    private String jsonData;

    /**
     * Initializes an empty context object.
     */
    public TemplateContext() {
        this.jsonData = null;
        this.dc = null;
    }

    /**
     * Initializes the context with a JSON as string.
     * @param data the JSON data as a string.
     * @throws TemplateServiceRuntimeException on parse errors.
     */
    public TemplateContext(final String data) {
        init(data);
    }

    protected void init(final String data) {
        this.jsonData = data;

        try {
            log.trace("Parsing context data...");

            this.dc = JsonPath.using(Configuration.defaultConfiguration()).parse(data);

            Map<String, Object> contextObjects = dc.read(JSONPATH_PREFIX + TemplateContext.CONTEXT_ROOT_KEY);

            final StringWriter sw = new StringWriter();
            try {
                JSONValue.writeJSONString(contextObjects, sw);
            } catch (final IOException e) {
                throw new TemplateServiceRuntimeException("Error parsing data", e);
            }
            if (this.ctx.keySet().isEmpty()) {
                withContext(ContextObjectProxyBuilder.build(sw.toString()));
            }

            log.trace("Context parse successful");
        } catch (final Exception e) {
            final var msg = "Error reading JSON data";

            log.error(msg);
            if (log.isDebugEnabled()) {
                log.debug("Value object to parse: {}", data);
            }

            throw new TemplateServiceRuntimeException(msg, e);
        }

    }

    /**
     * Returns the model object for the root context.
     * Use this method in the placeholder for single model templates.
     * @return the root context's model object.
     */

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
        this.ctx.put(CONTEXT_ROOT_KEY, value);

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
        if (log.isTraceEnabled()) {
            log.trace("Getting data for path {}...", path);
        }
        try {
            if (this.dc == null) {
                this.build();
            }
            final Object value = this.dc.read(JSONPATH_PREFIX + path);

            if (value instanceof String) {
                return value;
            } else if (value instanceof Map) {
                final StringWriter sw = new StringWriter();
                try {
                    JSONValue.writeJSONString(value, sw);
                } catch (final IOException e) {
                    throw new TemplateServiceRuntimeException("Error parsing data.", e);
                }

                return ContextObjectProxyBuilder.build("{\"" + TemplateContext.CONTEXT_ROOT_KEY + "\": " + sw.toString() + "}");

//                return new TemplateContext("{\"" + TemplateContext.CONTEXT_ROOT_KEY + "\": " + sw.toString() + "}");
            } else if (value instanceof JSONArray) {
                final List<TemplateContext> results = new ArrayList<>(((JSONArray) value).size());
                for(final Object actItem : ((JSONArray) value)) {
                    final StringWriter sw = new StringWriter();
                    try {
                        JSONValue.writeJSONString(actItem, sw);
                    } catch (final IOException e) {
                        throw new TemplateServiceRuntimeException("Error parsing data.", e);
                    }
                    results.add((TemplateContext) ContextObjectProxyBuilder.build("{\"" + TemplateContext.CONTEXT_ROOT_KEY + "\": " + sw.toString() + "}"));
//                    results.add(new TemplateContext("{\"" + TemplateContext.CONTEXT_ROOT_KEY + "\": " + sw.toString() + "}"));
                }
                return results;
            }

            return value;
        } catch (final Exception e) {
            final var msg = String.format("Error reading JSON data. Path: %s", path);

            log.error(msg);
            if (log.isTraceEnabled()) {
                log.debug("Value object to parse: {}", this.jsonData);
            }

            throw new TemplateServiceRuntimeException(msg, e);
        }
    }

    /**
     * Returns a list of values based on the JSON path caught.
     * @param path the JSONpath to be evaluated.
     * @return the list of context object if found by the given JSON path.
     */
    public List<Object> getItems(final String path) {
        if (log.isTraceEnabled()) {
            log.trace("Getting items for path {}...", path);
        }
        return this.dc.read(JSONPATH_PREFIX + path);
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
    @Override
    public String toJson() {
        if (this.jsonData == null) {
            synchronized (this) {
                if (this.jsonData == null) {
                    final Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    return this.jsonData = gson.toJson(this);
                }
            }
        }

        return this.jsonData;

    }
}
