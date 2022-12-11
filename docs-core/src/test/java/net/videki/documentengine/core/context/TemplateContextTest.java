package net.videki.documentengine.core.context;

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

import lombok.extern.slf4j.Slf4j;
import net.videki.documentengine.core.context.dto.JsonValueObject;
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
