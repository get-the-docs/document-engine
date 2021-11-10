package net.videki.templateutils.template.core.provider.documentstructure.builder.yaml;

/*-
 * #%L
 * template-utils-documentstructure-builder
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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.videki.templateutils.template.core.documentstructure.descriptors.OptionalTemplateElement;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementOption;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class OptionalTemplateElementDeserializer extends StdDeserializer<OptionalTemplateElement> {
    protected OptionalTemplateElementDeserializer() {
        super(OptionalTemplateElement.class);
    }

    @Override
    public OptionalTemplateElement deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        final JsonNode teNode = jp.getCodec().readTree(jp);
        OptionalTemplateElement templateElement = new OptionalTemplateElement(
                teNode.get("templateElementId").get("id").textValue());

        final JsonNode origIdNode = teNode.get("originalTemplateElementId");
        if (origIdNode != null) {
            templateElement.setOriginalTemplateElementId(new TemplateElementId(
                    origIdNode.get("id").textValue()));
        }

        final JsonNode templateNamesNode = teNode.get("templateNames");

        final Iterator<Map.Entry<String, JsonNode>> it = templateNamesNode.fields();

        while(it.hasNext()) {
            Map.Entry<String,JsonNode> entry = it.next();
            final String name = entry.getKey();
            final String value = entry.getValue().textValue();

            templateElement.getTemplateNames().put(new Locale(name), value);

        }

        final JsonNode optionNode = teNode.get("option");
        if (optionNode != null) {
            templateElement.setOption(TemplateElementOption.valueOf(optionNode.textValue()));
        }
        templateElement.setDefaultLocale(new Locale(teNode.get("defaultLocale").textValue()));
        templateElement.setCount(teNode.get("count").asInt());

        return templateElement;
    }
}
