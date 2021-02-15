package net.videki.templateutils.template.core.provider.documentstructure.builder.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureOptionsBuilder;
import net.videki.templateutils.template.core.documentstructure.descriptors.DocumentStructureOptions;
import net.videki.templateutils.template.core.documentstructure.descriptors.OptionalTemplateElement;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
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
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OptionalTemplateElement.class, new OptionalTemplateElementDeserializer());
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        mapper.registerModule(module);
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
