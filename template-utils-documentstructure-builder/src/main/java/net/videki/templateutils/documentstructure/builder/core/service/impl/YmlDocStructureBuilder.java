package net.videki.templateutils.documentstructure.builder.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.videki.templateutils.documentstructure.builder.core.documentstructure.DocumentStructureOptions;
import net.videki.templateutils.documentstructure.builder.core.documentstructure.OptionalTemplateElement;
import net.videki.templateutils.documentstructure.builder.core.service.DocumentStructureBuilder;
import net.videki.templateutils.documentstructure.builder.core.service.DocumentStructureOptionsBuilder;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class YmlDocStructureBuilder implements DocumentStructureBuilder, DocumentStructureOptionsBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(YmlDocStructureBuilder.class);

    @Override
    public DocumentStructure build(final String dsConfig) throws TemplateServiceConfigurationException {

        DocumentStructure result;
        SimpleModule module = new SimpleModule();
        module.addDeserializer(TemplateElement.class, new TemplateElementDeserializer());
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        mapper.registerModule(module);
        try {
            result = mapper.readValue(YmlDocStructureBuilder.class.getResourceAsStream(dsConfig),
                    DocumentStructure.class);
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

    @Override
    public DocumentStructureOptions buildOptions(final String dsConfig) throws TemplateServiceConfigurationException {

        DocumentStructureOptions result;
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OptionalTemplateElement.class, new OptionalTemplateElementDeserializer());
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        mapper.registerModule(module);
        try {
            final InputStream is = YmlDocStructureBuilder.class.getResourceAsStream(dsConfig);
            if (is == null) {
                throw new FileNotFoundException();
            }

            result = mapper.readValue(is, DocumentStructureOptions.class);
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