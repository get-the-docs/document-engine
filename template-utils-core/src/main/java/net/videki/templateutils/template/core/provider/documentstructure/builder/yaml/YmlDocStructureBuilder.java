package net.videki.templateutils.template.core.provider.documentstructure.builder.yaml;

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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureBuilder;
import net.videki.templateutils.template.core.documentstructure.TemplateElement;
import net.videki.templateutils.template.core.service.exception.DocumentStructureException;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
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

/**
 * Yaml document structure builder.
 * Creates DocumentStructure descriptors from yaml documents. 
 * @author Levente Ban
 */
public class YmlDocStructureBuilder implements DocumentStructureBuilder {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(YmlDocStructureBuilder.class);

    /**
     * Entry point to build a DocumentStructure descriptor from an input stream containing a yaml document.
     * @param dsConfig the document structure descriptor in yaml format. 
     * @return the DocumentStructure descriptor if the parse was successful. 
     * @throws TemplateServiceConfigurationException thnrown in case of configuration errors.
     */
    @Override
    public DocumentStructure build(final InputStream dsConfig) throws TemplateServiceConfigurationException {

        if (dsConfig == null) {
            throw new TemplateProcessException("cfb09b69-cb69-4cb9-b7b0-b060c0717cf3", "Null input caught.");
        }

        DocumentStructure result;
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(TemplateElement.class, new TemplateElementDeserializer());
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.registerModule(module);
        try {

            final String dsStr = new String(dsConfig.readAllBytes(), StandardCharsets.UTF_8);

            result = mapper.readValue(dsStr, getDocumentStructureVersion(dsStr));
            if (LOGGER.isDebugEnabled()) {
                final String msg = String.format("DocumentStructure loaded: configFile: %s", dsConfig);

                LOGGER.debug(msg);
            }
            if (LOGGER.isTraceEnabled()) {
                final String msg = String.format("DocumentStructure loaded: configFile: %s, content: %s", dsConfig,
                        ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE));

                LOGGER.debug(msg);
            }
        } catch (final FileNotFoundException e) {
            final String msg = String.format("DocumentStructure configuration file not found: %s", dsConfig);
            LOGGER.warn(msg, e);

            throw new TemplateServiceConfigurationException("9c4233f3-ce43-45b5-80f9-a06424916649", msg);
        } catch (final Exception e) {
            final String msg = String.format("Error reading documentStructure configuration: %s", dsConfig);
            LOGGER.error(msg, e);

            throw new TemplateServiceConfigurationException("d53a8fb6-183f-466e-ad94-aa189bad455a", msg);
        }
        return result;
    }

    protected Class<? extends DocumentStructure> getDocumentStructureVersion(final String ds) {

        try {
            final Yaml yaml= new Yaml();
            final Map<String,Object> map= yaml.load(ds);

            final String apiVersion = (String) map.get("apiVersion");

            switch (apiVersion) {
                case "v1":
                    return net.videki.templateutils.template.core.documentstructure.v1.DocumentStructureV1.class;
                default:
                    final String msg =
                            String.format("Unhandled documentStructure version: - %s", apiVersion);

                    throw new DocumentStructureException("1f743a13-2f38-44cc-9eca-77db849ac249", msg);
            }
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cannot determine documentStructure version: [{}] - \"apiVersion\" field missing.", ds);
            }

            throw new DocumentStructureException("5feecc05-3be0-4690-a0c7-b4c0bedd6617",
                    "Cannot determine documentStructure version: \"apiVersion\" field missing.");
        }
    }

}
