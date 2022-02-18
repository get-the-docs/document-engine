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

import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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

    @Test
    public void nullDataShouldInitializeAsEmptyObject() {
        final JsonValueObject o = new JsonValueObject(null);

        assertNotNull(o);
    }

    @Test
    public void validJsonShouldBeParsed() {
        final var jo = new JsonValueObject(jsonData);

        assertNotNull(jo);
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
