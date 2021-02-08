package net.videki.templateutils.template.core.provider.documentstructure;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.util.Properties;

public interface DocumentStructureRepository {

    void init(Properties props) throws TemplateServiceConfigurationException;

    DocumentStructure getDocumentStructure(String ds) throws TemplateServiceConfigurationException;

}
