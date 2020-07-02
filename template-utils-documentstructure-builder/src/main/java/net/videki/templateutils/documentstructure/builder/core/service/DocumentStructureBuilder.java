package net.videki.templateutils.documentstructure.builder.core.service;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

public interface DocumentStructureBuilder {
    DocumentStructure build(String dsConfig) throws TemplateServiceConfigurationException;
}
