package net.videki.templateutils.template.core.provider.documentstructure;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.util.Properties;

/**
 * Base document structure repository interface.
 * @author Levente Ban
 */
public interface DocumentStructureRepository {

    /**
     * Entry point for repository initialization.
     * @param props configuration properties (see template-utils.properties) 
     * @throws TemplateServiceConfigurationException thrown in case of initialization errors based on configuration errors. 
     */
    void init(Properties props) throws TemplateServiceConfigurationException;

    /**
     * Returns a document structure identified by its id in the configured document structure repository.
     * @param ds the document structure id.
     * @return the document structure descriptor.
     * @throws TemplateServiceConfigurationException thrown in case of repository configuration error.
     */
    DocumentStructure getDocumentStructure(String ds) throws TemplateServiceConfigurationException;

}
