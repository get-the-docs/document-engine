package org.getthedocs.documentengine.core.service;

/*-
 * #%L
 * docs-core
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

import org.getthedocs.documentengine.core.context.JsonTemplateContext;
import org.getthedocs.documentengine.core.documentstructure.StoredResultDocument;
import org.getthedocs.documentengine.core.service.exception.TemplateProcessException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.getthedocs.documentengine.test.dto.ContractDataFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.fail;

public class TemplateServiceInputFormatTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceInputFormatTest.class);
    private final TemplateService ts = TemplateServiceRegistry.getInstance();

    // TODO: Correct lang level related issues when Java 17 lambda runtime is out
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


/*
    private final String jsonDataMultiContext = """
            {
              "ctx": {
                "org": {
                  "orgCode": "PB",
                  "name": "Vintage Services - Palm beach",
                  "address": {
                    "zip": "Y-1234567",
                    "city": "Simply City",
                    "address": "Main blvd 432"
                  }
                },
                "officer": {
                  "name": "Chuck Norris",
                  "orgCode": "PB-001",
                  "login": "PB\\\\cnorris"
                },
                "contract": {
                  "contractor": {
                    "name": "John Doe",
                    "birthDate": {
                      "year": 1970,
                      "month": 7,
                      "day": 20
                    }
                  },
                  "contractType": {
                    "contractTypeName": "Vintage Gold",
                    "fee": 1500,
                    "paymentFrequency": "MONTHLY"
                  },
                  "beneficiaries": [
                    {
                      "phoneNumber": "+1 800 2234 567",
                      "name": "Jim Doe",
                      "birthDate": {
                        "year": 1975,
                        "month": 8,
                        "day": 11
                      }
                    },
                    {
                      "phoneNumber": "+1 800 2234 568",
                      "name": "Tim Doe",
                      "birthDate": {
                        "year": 1976,
                        "month": 8,
                        "day": 12
                      }
                    },
                    {
                      "phoneNumber": "+1 800 2234 569",
                      "name": "Pim Doe",
                      "birthDate": {
                        "year": 1977,
                        "month": 8,
                        "day": 13
                      }
                    },
                    {
                      "phoneNumber": "+1 800 3234 567",
                      "name": "Jack Ryan",
                      "birthDate": {
                        "year": 1962,
                        "month": 8,
                        "day": 11
                      }
                    },
                    {
                      "phoneNumber": "+1 800 3234 568",
                      "name": "John Goodall",
                      "birthDate": {
                        "year": 1946,
                        "month": 8,
                        "day": 11
                      }
                    },
                    {
                      "phoneNumber": "+1 800 3234 569",
                      "name": "Mortimer Young",
                      "birthDate": {
                        "year": 1991,
                        "month": 8,
                        "day": 11
                      }
                    },
                    {
                      "phoneNumber": "+1 800 3234 560",
                      "name": "Zack Black",
                      "birthDate": {
                        "year": 1987,
                        "month": 8,
                        "day": 11
                      }
                    }
                  ],
                  "signDate": {
                    "year": 2021,
                    "month": 2,
                    "day": 16
                  }
                },
                "doc": {
                  "dmsUrl": "http://dms.internal.pbvintage.com/050bca79-5aba-4e32-a34d-9409edcb0a68",
                  "login": "PB\\\\cnorris",
                  "generationDate": {
                    "year": 1970,
                    "month": 7,
                    "day": 20
                  }
                }
              }
            }""";
*/

    @Test
    public void processorDocxOKTest() {

        try {
            final StoredResultDocument result = ts.fillAndSave("unittests/docx/SimpleContract_v1_21-pojo.docx",
                    ContractDataFactory.createContract());

            Assert.assertTrue(result.isGenerated());
        } catch (final TemplateServiceException e) {
            LOGGER.error("processorDocxOKTest error", e);

            Assert.assertFalse(false);
        }

    }

    @Test
    public void processorDocxJsonpathOkTest() {

        try {
            final StoredResultDocument result = ts.fillAndSave("unittests/docx/SimpleContract_v1_21-jsonpath.docx",
                    new JsonTemplateContext(jsonDataMultiContext));

            Assert.assertTrue(result.isGenerated());
        } catch (final TemplateServiceException e) {
            LOGGER.error("processorDocxJsonpathOkTest error", e);

            fail();
        }

    }

    @Test
    public void processorDocxJsonpathPlaceholderErrorTest() throws TemplateProcessException, TemplateServiceException {

        final StoredResultDocument result = ts.fillAndSave("unittests/docx/SimpleContract_v1_21-jsonpath-placeholder_error.docx",
                new JsonTemplateContext(jsonDataMultiContext));

        Assert.assertFalse(result.isGenerated());

    }

    @Test
    public void processorDocxJsonpathPlaceholderPojoDTOErrorTest() throws TemplateProcessException, TemplateServiceException {

        final StoredResultDocument result = ts.fillAndSave("unittests/docx/SimpleContract_v1_21-jsonpath.docx",
                ContractDataFactory.createContract());

        Assert.assertFalse(result.isGenerated());
    }

    @Test
    public void processorDocxToPdfOKTest() {

        try {
            final StoredResultDocument result = ts.fillAndSave("unittests/docx/SimpleContract_v1_21-pojo.docx",
                    ContractDataFactory.createContract(), OutputFormat.PDF);

            Assert.assertTrue(result.isGenerated());
        } catch (TemplateServiceException e) {
            LOGGER.error("Error generating the template. ", e);

            fail();
        }
    }

    @Test
    public void docxProcessorTestNotRegistered() {

        try {
            ts.fill("myTemplate.rtf", ContractDataFactory.createContract());
            Assert.assertFalse(false);
        } catch (TemplateProcessException e) {
            Assert.assertEquals("c14d63df-8db2-45a2-bf21-e62fe60a23a0", e.getCode());
        } catch (TemplateServiceException e) {
            Assert.fail();
        }
    }

    @Test
    public void templateRegistryDocxProcessorTestUnhandledInputFormat() {

        try {
            ts.fill("myTemplate.rtf", new Object());
            Assert.assertFalse(false);
        } catch (TemplateProcessException e) {
            Assert.assertEquals("c14d63df-8db2-45a2-bf21-e62fe60a23a0", e.getCode());
            //d320e547-b4c6-45b2-bdd9-19ac0b699c97
        } catch (TemplateServiceException e) {
            Assert.fail();
        }
    }

}
