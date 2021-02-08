package net.videki.templateutils.template.core.provider.templaterepository;

import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.io.InputStream;
import java.util.Properties;

public interface TemplateRepository {

    void init(Properties props) throws TemplateServiceConfigurationException;

    InputStream getTemplate(String templateFile);

}
