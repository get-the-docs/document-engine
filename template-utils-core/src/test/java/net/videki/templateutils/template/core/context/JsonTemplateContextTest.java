package net.videki.templateutils.template.core.context;

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

import lombok.extern.slf4j.Slf4j;
import net.videki.templateutils.template.core.context.dto.JsonValueObject;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
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
            "          \"phoneNumber\": \"+1 800 2234 567\",\n" +
            "          \"name\": \"Jim Doe\",\n" +
            "          \"birthDate\": {\n" +
            "            \"year\": 1975,\n" +
            "            \"month\": 8,\n" +
            "            \"day\": 11\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"phoneNumber\": \"+1 800 2234 568\",\n" +
            "          \"name\": \"Tim Doe\",\n" +
            "          \"birthDate\": {\n" +
            "            \"year\": 1976,\n" +
            "            \"month\": 8,\n" +
            "            \"day\": 12\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"phoneNumber\": \"+1 800 2234 569\",\n" +
            "          \"name\": \"Pim Doe\",\n" +
            "          \"birthDate\": {\n" +
            "            \"year\": 1977,\n" +
            "            \"month\": 8,\n" +
            "            \"day\": 13\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"phoneNumber\": \"+1 800 3234 567\",\n" +
            "          \"name\": \"Jack Ryan\",\n" +
            "          \"birthDate\": {\n" +
            "            \"year\": 1962,\n" +
            "            \"month\": 8,\n" +
            "            \"day\": 11\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"phoneNumber\": \"+1 800 3234 568\",\n" +
            "          \"name\": \"John Goodall\",\n" +
            "          \"birthDate\": {\n" +
            "            \"year\": 1946,\n" +
            "            \"month\": 8,\n" +
            "            \"day\": 11\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"phoneNumber\": \"+1 800 3234 569\",\n" +
            "          \"name\": \"Mortimer Young\",\n" +
            "          \"birthDate\": {\n" +
            "            \"year\": 1991,\n" +
            "            \"month\": 8,\n" +
            "            \"day\": 11\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"phoneNumber\": \"+1 800 3234 560\",\n" +
            "          \"name\": \"Zack Black\",\n" +
            "          \"birthDate\": {\n" +
            "            \"year\": 1987,\n" +
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
            "    },\n" +
            "    \"doc\": {\n" +
            "      \"dmsUrl\": \"http://dms.internal.pbvintage.com/050bca79-5aba-4e32-a34d-9409edcb0a68\",\n" +
            "      \"login\": \"PB\\\\cnorris\",\n" +
            "      \"generationDate\": {\n" +
            "        \"year\": 1970,\n" +
            "        \"month\": 7,\n" +
            "        \"day\": 20\n" +
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
    public void evalJsonPathArrayValue() {
        final var ctx = new JsonTemplateContext(this.jsonDataMultiContext);

        final var result = ctx.jsonpath("ctx['contract'].beneficiaries");

        assertEquals(7, ((List<?>) result).size());

    }

    @Test
    public void evalJsonPathObjectValue() {
        final var ctx = new JsonTemplateContext(this.jsonDataMultiContext);

        final var result = ctx.jsonpath("ctx['contract'].beneficiaries[0]");

        assertEquals(JsonTemplateContext.class, result.getClass());

    }

    @Test
    public void evalJsonPathIJsonTemplateDateValue() {
        final var ctx = new JsonTemplateContext(this.jsonDataMultiContext);

        final var result = ctx.fmtDate((JsonTemplateContext) ctx.jsonpath("ctx['contract'].beneficiaries[0].birthDate"));

        assertEquals(ctx.fmtDate(LocalDate.of(1975, Month.AUGUST, 11)), result);

    }

    @Test(expected = TemplateServiceRuntimeException.class)
    public void evalJsonPathNonexistingPath() {
        final var ctx = new JsonTemplateContext(this.jsonDataMultiContext);

        ctx.jsonpath("ctx['contract'].contractor.naame_i_have_a_typo_in_it");

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
