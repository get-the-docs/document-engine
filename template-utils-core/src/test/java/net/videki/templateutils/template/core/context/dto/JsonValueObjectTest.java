package net.videki.templateutils.template.core.context.dto;

import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.junit.Assert;
import org.junit.Test;

public class JsonValueObjectTest {

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

    @Test(expected = TemplateServiceRuntimeException.class)
    public void nullDataShouldThrow() {
        new JsonValueObject(null);
    }

    @Test
    public void validJsonShouldBeParsed() {
        final var jo = new JsonValueObject(jsonData);

        Assert.assertNotNull(jo);
    }

    @Test
    public void getValueStringOk() {

        final var val = new JsonValueObject(jsonData);

        Assert.assertEquals("naist street", val.jsonpath("address.streetAddress"));
    }

    @Test
    public void getListValueOk() {

        final var val = new JsonValueObject(jsonData);

        Assert.assertEquals(2, val.getItems("phoneNumbers[*].number").size());
    }

}
