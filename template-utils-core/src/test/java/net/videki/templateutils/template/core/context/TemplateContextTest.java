package net.videki.templateutils.template.core.context;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.context.dto.JsonValueObject;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class TemplateContextTest {

    private final String jsonData = "{\n" +
            "  \"firstName\": \"John\",\n" +
            "  \"lastName\" : \"doe\",\n" +
            "  \"age\"      : 26,\n" +
            "  \"address\"  : {\n" +
            "    \"streetAddress\": \"naist street\",\n" +
            "    \"city\"         : \"Nara\",\n" +
            "    \"postalCode\"   : \"630-0192\"\n" +
            "  },\n" +
            "  \"phoneNumbers\": [\n" +
            "    {\n" +
            "      \"type\"  : \"iPhone\",\n" +
            "      \"number\": \"0123-4567-8888\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\"  : \"home\",\n" +
            "      \"number\": \"0123-4567-8910\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    public void serializationOneEntryShouldbeSerializedTest() {

        final var tc = new TemplateContext();
        tc.addValueObject(new JsonValueObject(this.jsonData));

        log.debug("Data: {}", tc.toJson());
    }
}
