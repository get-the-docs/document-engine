package net.videki.templateutils.template.core.context;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import net.videki.templateutils.template.core.context.dto.JsonValueObject;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonTemplateContext extends TemplateContext {
    private static final String JSONPATH_PREFIX = "$.";

    private final transient DocumentContext dc;
    private final String jsonData;

    public JsonTemplateContext() {
        this.jsonData = null;
        this.dc = null;
    }

    public JsonTemplateContext(final String data) {
        this.jsonData = data;

        try {
            log.trace("Parsing context data...");

            this.dc = JsonPath.using(Configuration.defaultConfiguration()).parse(data);

            Map<String, Object> contextObjects = dc.read("$." + TemplateContext.CONTEXT_ROOT_KEY);

            contextObjects.keySet()
                    .forEach(t -> {
                        final StringWriter sw = new StringWriter();
                        try {
                            JSONValue.writeJSONString(contextObjects.get(t), sw);
                        } catch (final IOException e) {
                            throw new TemplateServiceRuntimeException("Error parsing data");
                        }
                        addValueObject(t, new JsonValueObject(sw.toString()));
                    });

            log.trace("Context parse successful");
        } catch (final Exception e) {
            final var msg = "Error reading JSON data";

            log.error(msg);
            if (log.isDebugEnabled()) {
                log.debug("Value object to parse: {}", data);
            }

            throw new TemplateServiceRuntimeException(msg);
        }
    }

    public Object getValue(final String path) {
        if (log.isTraceEnabled()) {
            log.trace("Getting data for path {}...", path);
        }
        return this.dc.read(JSONPATH_PREFIX + path);
    }

    public List<Object> getItems(final String path) {
        if (log.isTraceEnabled()) {
            log.trace("Getting items for path {}...", path);
        }
        return this.dc.read(JSONPATH_PREFIX + path);
    }

    public Object jsonpath(final String path) {
        return getValue(path);
    }

    public Object jp(final String path) {
        return getValue(path);
    }

    public List<Object> items(final String path) {
        return getItems(path);
    }

    public String getData() {
        return this.jsonData;
    }


    @Override
    public String toJson() {
        return this.jsonData;
    }
}
