package net.videki.templateutils.template.core.provider.documentstructure.builder;

import net.videki.templateutils.template.core.documentstructure.descriptors.DocumentStructureOptions;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.io.InputStream;

public interface DocumentStructureOptionsBuilder extends DocumentStructureBuilder {
    DocumentStructureOptions buildOptions(InputStream dsConfig) throws TemplateServiceConfigurationException;
}
