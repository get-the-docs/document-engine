package org.getthedocs.documentengine.core.provider.documentstructure.builder.yaml;

/*-
 * #%L
 * docs-documentstructure-builder
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
import org.getthedocs.documentengine.core.documentstructure.descriptors.DocumentStructureOptions;
import org.getthedocs.documentengine.core.documentstructure.descriptors.OptionalTemplateElement;
import org.getthedocs.documentengine.core.dto.json.ObjectMapperFactory;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.provider.documentstructure.builder.DocumentStructureOptionsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class YmlConfigurableDocStructureBuilder extends YmlDocStructureBuilder implements DocumentStructureOptionsBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(YmlConfigurableDocStructureBuilder.class);

    @Override
    public DocumentStructureOptions buildOptions(final InputStream dsConfig) throws TemplateServiceConfigurationException {

        DocumentStructureOptions result;
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(OptionalTemplateElement.class, new OptionalTemplateElementDeserializer());
        final ObjectMapper mapper = ObjectMapperFactory.yamlMapper(module);

        try {
            if (dsConfig == null) {
                throw new FileNotFoundException();
            }

            result = mapper.readValue(dsConfig, DocumentStructureOptions.class);
            if (LOGGER.isDebugEnabled()) {
                final String msg = String.format("DocumentStructureOptions loaded: configFile: %s", dsConfig);

                LOGGER.debug(msg);
            }
            if (LOGGER.isTraceEnabled()) {
                final String msg = String.format("DocumentStructureOptions loaded: configFile: %s, content: %s", dsConfig,
                        ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE));

                LOGGER.trace(msg);
            }
        } catch (FileNotFoundException e) {
            final String msg = String.format("DocumentStructureOptions configuration file not found: %s", dsConfig);
            LOGGER.error(msg, e);

            throw new TemplateServiceConfigurationException("bf4dbf44-8b7b-4cba-b449-906083eabf0d", msg);
        } catch (Exception e) {
            final String msg = String.format("Error reading documentStructureOptions configuration: %s", dsConfig);
            LOGGER.error(msg, e);

            throw new TemplateServiceConfigurationException("40b51a6b-4ad5-4940-80d3-ffac4bba3c99", msg);
        }
        return result;
    }
}
