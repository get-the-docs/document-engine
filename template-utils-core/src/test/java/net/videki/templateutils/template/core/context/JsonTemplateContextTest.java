package net.videki.templateutils.template.core.context;

import lombok.extern.slf4j.Slf4j;
import net.videki.templateutils.template.core.context.dto.JsonValueObject;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

@Slf4j
public class JsonTemplateContextTest {

    private final String jsonDataMultiContext = "{\n" +
            "  \"ctx\": {\n" +
            "    \"org\": {\n" +
            "      \"orgCode\": \"PB\",\n" +
            "      \"name\": \"Vintage Services - Palm beach\",\n" +
            "      \"address\": {\n" +
            "        \"zip\": \"Y-1234567\",\n" +
            "        \"city\": \"Simply City\",\n" +
            "        \"address\": \"Main blvd 432\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"officer\": {\n" +
            "      \"name\": \"Chuck Norris\",\n" +
            "      \"orgCode\": \"PB-001\",\n" +
            "      \"login\": \"PB\\\\cnorris\"\n" +
            "    },\n" +
            "    \"contract\": {\n" +
            "      \"contractor\": {\n" +
            "        \"name\": \"John Doe\",\n" +
            "        \"birthDate\": {\n" +
            "          \"year\": 1970,\n" +
            "          \"month\": 7,\n" +
            "          \"day\": 20\n" +
            "        }\n" +
            "      },\n" +
            "      \"contractType\": {\n" +
            "        \"contractTypeName\": \"Vintage Gold\",\n" +
            "        \"fee\": 1500,\n" +
            "        \"paymentFrequency\": \"MONTHLY\"\n" +
            "      },\n" +
            "      \"beneficiaries\": [\n" +
            "        {\n" +
            "          \"beneficiaries\": [\n" +
            "            {\n" +
            "              \"phoneNumber\": \"+1 800 2234 567\",\n" +
            "              \"name\": \"Jim Doe\",\n" +
            "              \"birthDate\": {\n" +
            "                \"year\": 1975,\n" +
            "                \"month\": 8,\n" +
            "                \"day\": 11\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"phoneNumber\": \"+1 800 2234 568\",\n" +
            "              \"name\": \"Tim Doe\",\n" +
            "              \"birthDate\": {\n" +
            "                \"year\": 1976,\n" +
            "                \"month\": 8,\n" +
            "                \"day\": 12\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"phoneNumber\": \"+1 800 2234 569\",\n" +
            "              \"name\": \"Pim Doe\",\n" +
            "              \"birthDate\": {\n" +
            "                \"year\": 1977,\n" +
            "                \"month\": 8,\n" +
            "                \"day\": 13\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"phoneNumber\": \"+1 800 3234 567\",\n" +
            "              \"name\": \"Jack Ryan\",\n" +
            "              \"birthDate\": {\n" +
            "                \"year\": 1962,\n" +
            "                \"month\": 8,\n" +
            "                \"day\": 11\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"phoneNumber\": \"+1 800 3234 568\",\n" +
            "              \"name\": \"John Goodall\",\n" +
            "              \"birthDate\": {\n" +
            "                \"year\": 1946,\n" +
            "                \"month\": 8,\n" +
            "                \"day\": 11\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"phoneNumber\": \"+1 800 3234 569\",\n" +
            "              \"name\": \"Mortimer Young\",\n" +
            "              \"birthDate\": {\n" +
            "                \"year\": 1991,\n" +
            "                \"month\": 8,\n" +
            "                \"day\": 11\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"phoneNumber\": \"+1 800 3234 560\",\n" +
            "              \"name\": \"Zack Black\",\n" +
            "              \"birthDate\": {\n" +
            "                \"year\": 1987,\n" +
            "                \"month\": 8,\n" +
            "                \"day\": 11\n" +
            "              }\n" +
            "            }\n" +
            "          ],\n" +
            "          \"phoneNumber\": \"+1 800 1234 567\",\n" +
            "          \"name\": \"Jane Doe\",\n" +
            "          \"birthDate\": {\n" +
            "            \"year\": 1971,\n" +
            "            \"month\": 4,\n" +
            "            \"day\": 2\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"beneficiaries\": [],\n" +
            "          \"phoneNumber\": \"+1 800 1234 568\",\n" +
            "          \"name\": \"Jenny Mack\",\n" +
            "          \"birthDate\": {\n" +
            "            \"year\": 1951,\n" +
            "            \"month\": 8,\n" +
            "            \"day\": 11\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"signDate\": {\n" +
            "        \"year\": 2021,\n" +
            "        \"month\": 2,\n" +
            "        \"day\": 16\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Test
    public void serializationMultiContextTest() {
        final var result = new JsonTemplateContext(this.jsonDataMultiContext);

        log.debug("Data: {}", result.toJson());

    }
    
    @Test
    public void evalJsonPathStringValue() {
        final var ctx = new JsonTemplateContext(this.jsonDataMultiContext);

        assertEquals("John Doe", ctx.jsonpath("ctx['contract'].contractor.name"));

    }

    @Test
    public void evalJsonPathDateValue() {
        final var ctx = new JsonTemplateContext(this.jsonDataMultiContext);

        assertEquals(ctx.fmtDate(LocalDate.of(1970, Month.JULY, 20)), ctx.fmtDate((Map<Object, Object>)ctx.jsonpath("ctx['contract'].contractor.birthDate")));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void concatValuesTest() {
        final var result = new JsonTemplateContext(this.jsonDataMultiContext);

        log.debug("Data: {}", result.toJson());

        Assert.assertEquals("Y-1234567 Simply City Main blvd 432",
                result.concat((Map<String, Object>)((JsonValueObject)result.getCtx().get("org"))
                        .jp("address['zip', 'city', 'address']")));
    }

}
