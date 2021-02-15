package net.videki.templateutils.template.core.provider.documentstructure.repository;

import net.videki.templateutils.template.core.documentstructure.descriptors.DocumentStructureOptions;
import net.videki.templateutils.template.core.provider.documentstructure.DocumentStructureRepository;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

public interface ConfigurableDocumentStructureRepository extends DocumentStructureRepository {

    DocumentStructureOptions getDocumentStructureOptions(String ds) throws TemplateServiceConfigurationException;

}
