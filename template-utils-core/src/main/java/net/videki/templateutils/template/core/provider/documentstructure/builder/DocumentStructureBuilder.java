package net.videki.templateutils.template.core.provider.documentstructure.builder;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.io.InputStream;

/**
 * Base interface for document structure builder implementations.
 * @author Levente Ban
 */
public interface DocumentStructureBuilder {

    /**
     * Builds a document structure descriptor from the input stream.
     * @param dsConfig the document structure as a stream in its specific format.
     * @return the document struture descriptor if the input could be successfully parsed.
     * @throws TemplateServiceConfigurationException thrown in case of configuration errors.
     */
    DocumentStructure build(InputStream dsConfig) throws TemplateServiceConfigurationException;
}
