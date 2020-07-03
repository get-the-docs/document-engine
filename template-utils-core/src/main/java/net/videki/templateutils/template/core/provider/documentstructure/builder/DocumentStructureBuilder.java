package net.videki.templateutils.template.core.provider.documentstructure.builder;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.io.InputStream;

public interface DocumentStructureBuilder {
    DocumentStructure build(InputStream dsConfig) throws TemplateServiceConfigurationException;
}
