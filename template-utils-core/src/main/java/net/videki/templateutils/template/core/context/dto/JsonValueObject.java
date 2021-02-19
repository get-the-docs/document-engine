package net.videki.templateutils.template.core.context.dto;

import net.videki.templateutils.template.core.context.ContextObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonValueObject extends ContextObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextObject.class);

    private static final String JSONPATH_PREFIX = "$.";

    private final transient DocumentContext dc;
    private final String data;

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

    @Override
    public Object getValue(final String path) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Getting data for path {}...", path);
        }
        return this.dc.read(JSONPATH_PREFIX + path);
    }

    @Override
    public List<Object> getItems(final String path) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Getting items for path {}...", path);
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
        return this.data;
    }

    @Override
    public String toJson() {
        return data;
    }
}
