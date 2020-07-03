package net.videki.templateutils.template.core.provider.documentstructure.builder.yaml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class TemplateElementDeserializer extends StdDeserializer<TemplateElement> {
    protected TemplateElementDeserializer() {
        super(TemplateElement.class);
    }

    @Override
    public TemplateElement deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode teNode = jp.getCodec().readTree(jp);
        TemplateElement templateElement = new TemplateElement(teNode.get("templateElementId").get("id").textValue());
        JsonNode templateNamesNode = teNode.get("templateNames");

        Iterator<Map.Entry<String, JsonNode>> it = templateNamesNode.fields();

        while(it.hasNext()) {
            Map.Entry<String,JsonNode> entry = it.next();
            String name = entry.getKey();
            String value = entry.getValue().textValue();

            templateElement.getTemplateNames().put(new Locale(name), value);

        }

        templateElement.setDefaultLocale(new Locale(teNode.get("defaultLocale").textValue()));
        templateElement.setCount(teNode.get("count").asInt());

        return templateElement;
    }
}
