package net.videki.templateutils.documentstructure.builder.core.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.videki.templateutils.documentstructure.builder.core.documentstructure.OptionalTemplateElement;
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

        templateElement.setDefaultLocale(new Locale(teNode.get("defaultLocale").textValue()));
        templateElement.setCount(teNode.get("count").asInt());

        return templateElement;
    }
}
