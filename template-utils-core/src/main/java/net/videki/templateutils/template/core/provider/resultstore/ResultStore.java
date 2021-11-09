package net.videki.templateutils.template.core.provider.resultstore;

import net.videki.templateutils.template.core.documentstructure.ResultDocument;
import net.videki.templateutils.template.core.documentstructure.GenerationResult;
import net.videki.templateutils.template.core.documentstructure.StoredResultDocument;
import net.videki.templateutils.template.core.documentstructure.StoredGenerationResult;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;

import java.util.Optional;
import java.util.Properties;

/**
 * Adapter interface for saving the template generation results through the desired implementation set
 * in the TemplateServiceConfiguration.
 *
 * @see net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration
 *
 * @author Levente Ban
 */
public interface ResultStore {

    /**
     * Repository initializer entry point.
     * @param props the system properties (see template-utils.properties).
     * @throws TemplateServiceConfigurationException thrown in case of configuration errors.
     */
    void init(Properties props) throws TemplateServiceConfigurationException;

    /**
     * Saves a single template based result document.
     * @param result the generated document.
     * @return DocumentResult the save results (the result filename and its success flag).
     */
    StoredResultDocument save(final ResultDocument result);

    /**
     * Saves a document structure based generation result.
     * @param results the generated document structure to save.
     * @return StoredGenerationResult the save results (the input params and their success flags).
     */
    StoredGenerationResult save(final GenerationResult results);

    /**
     * Returns a given result document and/or its binary if requested.
     * For document structures query the generation result for the transaction id, then request its result documents. 
     * @param transactionId the requested transaction, if found.
     * @param resultDocument the result document name as stored in the result store.
     * @param withBinary true, if the result binary should be part of the response.
     * @throws TemplateServiceException thrown in case of query error.
     * @return the template document.
     */
    Optional<StoredResultDocument> getResultDocumentByTransactionId(String transactionId, String resultDocument, boolean withBinary) throws TemplateServiceException;

    /**
     * Returns a given result document and/or its binary if requested.
     * For document structures query 
     * @param transactionId the requested transaction, if found.
     * @throws TemplateServiceException thrown in case of query error.
     * @return the template document.
     */
    Optional<StoredGenerationResult> getGenerationResultByTransactionId(String transactionId) throws TemplateServiceException;
   
}
