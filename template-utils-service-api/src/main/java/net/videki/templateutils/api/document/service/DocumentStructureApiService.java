package net.videki.templateutils.api.document.service;

import java.util.Optional;

import net.videki.templateutils.api.document.model.ValueSet;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;

/**
 * Document structure API.
 * 
 * @author Levente Ban
 */
public interface DocumentStructureApiService {

    /**
     * Returns a page of document structures from the configured document structure
     * repository service.
     * 
     * @param page the page to retrieve (effective only if the document structure
     *             repository implementation has paging capability).
     * @return the requested page, if exists.
     */
    Page<DocumentStructure> getDocumentStructures(Pageable page);

    /**
     * Returns a document structure.
     * 
     * @param id the document structure's id in the repository.
     * @return the document structure descriptor, if found.
     */
    Optional<DocumentStructure> getDocumentStructureById(String id);

    /**
     * Posts a doument structure generation.
     * 
     * @param id              the document structure id.
     * @param values          the value set.
     * @param notificationUrl notification url, optional.
     * @return the transaction id for the document generation.
     */
    String postDocumentStructureGenerationJob(String id, Object values, String notificationUrl);
}
