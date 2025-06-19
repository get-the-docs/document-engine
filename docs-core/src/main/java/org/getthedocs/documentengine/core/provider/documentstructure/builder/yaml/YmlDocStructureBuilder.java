package org.getthedocs.documentengine.core.provider.documentstructure.builder.yaml;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.getthedocs.documentengine.core.documentstructure.DocumentStructure;
import org.getthedocs.documentengine.core.documentstructure.descriptors.TemplateElement;
import org.getthedocs.documentengine.core.dto.json.ObjectMapperFactory;
import org.getthedocs.documentengine.core.service.exception.TemplateProcessException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.provider.documentstructure.builder.DocumentStructureBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
        final ObjectMapper mapper = ObjectMapperFactory.yamlMapper(module);

        try {
            result = mapper.readValue(dsConfig, DocumentStructure.class);
            if (LOGGER.isDebugEnabled()) {
                final String msg = String.format("DocumentStructure loaded: configFile: %s", dsConfig);

                LOGGER.debug(msg);
            }
            if (LOGGER.isTraceEnabled()) {
                final String msg = String.format("DocumentStructure loaded: configFile: %s, content: %s", dsConfig,
                        ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE));

                LOGGER.debug(msg);
            }
        } catch (FileNotFoundException e) {
            final String msg = String.format("DocumentStructure configuration file not found: %s", dsConfig);
            LOGGER.error(msg, e);

            throw new TemplateServiceConfigurationException("9c4233f3-ce43-45b5-80f9-a06424916649", msg);
        } catch (Exception e) {
            final String msg = String.format("Error reading documentStructure configuration: %s", dsConfig);
            LOGGER.error(msg, e);

            throw new TemplateServiceConfigurationException("d53a8fb6-183f-466e-ad94-aa189bad455a", msg);
        }
        return result;
    }

}
