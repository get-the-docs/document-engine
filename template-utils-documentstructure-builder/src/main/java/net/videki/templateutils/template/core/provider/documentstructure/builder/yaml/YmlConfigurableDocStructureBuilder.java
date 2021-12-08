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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.videki.templateutils.template.core.documentstructure.DocumentStructureOptions;
import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureOptionsBuilder;
import net.videki.templateutils.template.core.documentstructure.v1.DocumentStructureOptionsV1;
import net.videki.templateutils.template.core.documentstructure.v1.OptionalTemplateElement;
import net.videki.templateutils.template.core.service.exception.DocumentStructureException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class YmlConfigurableDocStructureBuilder extends YmlDocStructureBuilder implements DocumentStructureOptionsBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(YmlConfigurableDocStructureBuilder.class);

    @Override
    public DocumentStructureOptions buildOptions(final InputStream dsConfig) throws TemplateServiceConfigurationException {

        DocumentStructureOptions result;
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OptionalTemplateElement.class, new OptionalTemplateElementDeserializer());
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.registerModule(module);
        try {
            if (dsConfig == null) {
                throw new FileNotFoundException();
            }

            final String dsStr = new String(dsConfig.readAllBytes(), StandardCharsets.UTF_8);

            result = mapper.readValue(dsStr, getDocumentStructureOptionsVersion(dsStr));
            if (LOGGER.isDebugEnabled()) {
                final String msg = String.format("DocumentStructureOptions loaded: configFile: %s", dsStr);

                LOGGER.debug(msg);
            }
            if (LOGGER.isTraceEnabled()) {
                final String msg = String.format("DocumentStructureOptions loaded: configFile: %s, content: %s", dsStr,
                        ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE));

                LOGGER.trace(msg);
            }
        } catch (Exception e) {
            final String msg = "Error reading documentStructureOptions.";
            LOGGER.warn(msg, e);

            throw new TemplateServiceConfigurationException("40b51a6b-4ad5-4940-80d3-ffac4bba3c99", msg);
        }
        return result;
    }

    protected Class<? extends DocumentStructureOptions> getDocumentStructureOptionsVersion(final String ds) {

        try {
            final Yaml yaml= new Yaml();
            final Map<String,Object> map= yaml.load(ds);

            final String apiVersion = (String) map.get("apiVersion");

            switch (apiVersion) {
                case "v1":
                    return DocumentStructureOptionsV1.class;
                default:
                    final String msg =
                            String.format("Unhandled documentStructure version: - %s", apiVersion);

                    throw new DocumentStructureException("97861a0d-a7dc-4e7a-8e71-deba3496e3f6", msg);
            }
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cannot determine documentStructure version: [{}] - \"apiVersion\" field missing.", ds);
            }

            throw new DocumentStructureException("c7ab8596-2dbc-4f98-94ee-2230d9f71df0",
                    "Cannot determine documentStructure version: \"apiVersion\" field missing.");
        }
    }
}
