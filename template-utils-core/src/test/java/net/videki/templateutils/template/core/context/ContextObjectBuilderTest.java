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

import java.util.List;

import net.videki.templateutils.template.core.context.dto.JsonModel;
import net.videki.templateutils.template.core.context.dto.JsonValueObject;
import org.junit.Test;

import net.videki.templateutils.template.core.context.ContextObjectProxyBuilder;

import static org.junit.Assert.*;

public class ContextObjectBuilderTest {

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

    private final String jsonSmall = "{\n" +
            "    \"doc\": {\n" +
            "      \"dmsUrl\": \"http://dms.internal.pbvintage.com/050bca79-5aba-4e32-a34d-9409edcb0a68\",\n" +
            "      \"login\": \"PB\\\\cnorris\",\n" +
            "      \"generationDate\": {\n" +
            "        \"year\": 1970,\n" +
            "        \"month\": 7,\n" +
            "        \"day\": 20\n" +
            "      }\n" +
            "    }\n" +
            "}";

    private final String jsonDataTypeSamples = "{\n" +
            "    \"doc\": {\n" +
            "      \"stringValue\": \"http://dms.internal.pbvintage.com/050bca79-5aba-4e32-a34d-9409edcb0a68\",\n" +
            "      \"numberValue\": 1234567890,\n" +
            "      \"floatValue\": 1234567890.123456,\n" +
            "      \"booleanValue\": true,\n" +
            "      \"dateEntry\": {\n" +
            "        \"year\": 1970,\n" +
            "        \"month\": 7,\n" +
            "        \"day\": 20\n" +
            "      }\n" +
            "    }\n" +
            "}";

    private final String jsonDataArray = "{\n" +
            "  \"ctx\": {\n" +
            "    \"contract\": {\n" +
            "      \"contractor\": {\n" +
            "        \"name\": \"John Doe\",\n" +
            "        \"birthDate\": {\n" +
            "          \"year\": 1970,\n" +
            "          \"month\": 7,\n" +
            "          \"day\": 20\n" +
            "        }\n" +
            "      },\n" +
            "      \"beneficiaries\": [\n" +
            "        {\n" +
            "          \"phoneNumber\": \"+1 800 2234 567\",\n" +
            "          \"name\": \"Jim Doe\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Tim Doe\",\n" +
            "          \"birthDate\": {\n" +
            "            \"year\": 1976,\n" +
            "            \"month\": 8,\n" +
            "            \"day\": 12\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Pim Doe\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Test
    public void noParamShouldReturnNull() {
        try {
            var generatedObject = ContextObjectProxyBuilder.build(null);

            assertNull(generatedObject);

        } catch (final Exception e) {
            fail();
        }
    }

    @Test
    public void emptyParamShouldReturnNull() {
        try {
            var generatedObject = ContextObjectProxyBuilder.build(" ");

            assertNull(generatedObject);

        } catch (final Exception e) {
            fail();
        }
    }


    @Test
    public void literalArrayParamShouldReturnAggregatedObject() {
        try {
            var generatedObject = ContextObjectProxyBuilder.build("{ \"mylist\": [1, 2, 3, \"apple\"]} ");

            assertNotNull(generatedObject);

        } catch (final Exception e) {
            fail();
        }
    }

    @Test
    public void dataWithAllDataTypesShouldBeParsed() {

        try {
            var generatedObject = ContextObjectProxyBuilder.build(jsonDataTypeSamples);

            if (generatedObject != null) {
                var reSerializedValue = ((JsonModel) generatedObject).toJson();
                final var ctx = new JsonValueObject(reSerializedValue);

                var strValueOut = ctx.jsonpath("doc.stringValue");
                var numberValueOut = ctx.jsonpath("doc.numberValue");
                Double doubleValueOut = (Double) ctx.jsonpath("doc.floatValue", Double.class);
                var booleanValueOut = ctx.jsonpath("doc.booleanValue");

                assertEquals("http://dms.internal.pbvintage.com/050bca79-5aba-4e32-a34d-9409edcb0a68", strValueOut);
                assertEquals(1234567890, numberValueOut);
                assertEquals(1234567890.123456, doubleValueOut, 1);
                assertTrue((Boolean) booleanValueOut);
            } else {
                fail();
            }
        } catch (final Exception e) {
            fail();
        }
    }

    @Test
    public void repeatedProxyBuildShouldBeParsedIdentically() {
        try {
            var generatedObject = ContextObjectProxyBuilder.build(jsonDataMultiContext);
            var generatedObject2 = ContextObjectProxyBuilder.build(jsonDataMultiContext);

            assertNotNull(generatedObject);
            assertNotNull(generatedObject2);

            var reSerializedValue = ((JsonModel) generatedObject).toJson();
            var reSerializedValue2 = ((JsonModel) generatedObject2).toJson();

            assertEquals(reSerializedValue, reSerializedValue2);

        } catch (final Exception e) {
            fail();
        }

    }

    @Test
    public void arrayWithNullsShouldBeAggregated() {
        try {
            final var generatedObject = ContextObjectProxyBuilder.build(jsonDataArray);

            assertNotNull(generatedObject);

            final var reSerializedValue = ((JsonModel) generatedObject).toJson();

            final var ctx = new JsonValueObject(reSerializedValue);

            final List<Object> listValue = ctx.getItems("ctx['contract'].beneficiaries");

            assertNotNull(listValue);
            assertEquals(3, listValue.size());

            final String name = (String)ctx.jsonpath("ctx['contract'].beneficiaries[0].name", String.class);
            assertEquals("Jim Doe", name);

        } catch (final Exception e) {
            fail();
        }

    }
}
