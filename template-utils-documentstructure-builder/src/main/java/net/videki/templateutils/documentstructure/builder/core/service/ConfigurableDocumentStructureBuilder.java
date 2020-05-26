package net.videki.templateutils.documentstructure.builder.core.service;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

public interface ConfigurableDocumentStructureBuilder {
    DocumentStructure build(final String config,
                            final String options,
                            final String configuration,
                            final ValueSet values) throws TemplateServiceConfigurationException;
}
