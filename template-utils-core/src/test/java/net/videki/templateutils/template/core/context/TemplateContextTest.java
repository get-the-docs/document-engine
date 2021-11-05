package net.videki.templateutils.template.core.context;

import lombok.extern.slf4j.Slf4j;
import net.videki.templateutils.template.core.context.dto.JsonValueObject;
import org.junit.Test;

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
