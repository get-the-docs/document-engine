package net.videki.templateutils.template.core.provider.resultstore;

import net.videki.templateutils.template.core.documentstructure.ResultDocument;
import net.videki.templateutils.template.core.documentstructure.GenerationResult;
import net.videki.templateutils.template.core.documentstructure.StoredResultDocument;
import net.videki.templateutils.template.core.documentstructure.StoredGenerationResult;

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
     * Saves a single template based result document.
     * @param result the generated document
     * @return DocumentResult the save results (the result filename and its success flag)
     */
    StoredResultDocument save(final ResultDocument result);

    /**
     * Saves a document structure based generation result
     * @param results the generated document structure to save
     * @return StoredGenerationResult the save results (the input params and their success flags)
     */
    StoredGenerationResult save(final GenerationResult results);
}
