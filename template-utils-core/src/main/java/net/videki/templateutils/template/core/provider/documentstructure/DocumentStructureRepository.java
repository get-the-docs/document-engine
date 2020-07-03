package net.videki.templateutils.template.core.provider.documentstructure;

import net.videki.templateutils.template.core.configuration.DocumentStructureRepositoryConfiguration;
import net.videki.templateutils.template.core.configuration.RepositoryConfiguration;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

public interface DocumentStructureRepository {

    void init(DocumentStructureRepositoryConfiguration props);

    DocumentStructure getDocumentStructure(final String ds) throws TemplateServiceConfigurationException;

}
