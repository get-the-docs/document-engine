package net.videki.templateutils.api.document.service;

import java.util.Optional;

import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;
import net.videki.templateutils.template.core.template.descriptors.TemplateDocument;

/**
 * @author Levente Ban
 * 
 * Wraps the template registry for the API.
 */
public interface TemplateApiService {

    String TEMPLATE_PACKAGE_TEMPLATE_SEPARATOR = ":";
    String TEMPLATE_PACKAGE_SEPARATOR = "\\";

    /**
     * Returns a page of templates from the configured template repository service. 
     * @param page the page to retrieve (effective only if the template repository implementation has paging capability).
     * @return the requested page, if exists.
     */
    Page<TemplateDocument> getTemplates(Pageable page);

    /**
     * Returns a template descriptor by id.
     * @param id the template id.
     * @param version template version for the given id, optional.
     * @param withBinary returns the template binary if specified, default false.
     * @return The template descriptor
     */
    Optional<TemplateDocument> getTemplateById(String id, String version, boolean withBinary);

    /**
     * Posts a single doument generation for the given template identified by its id. 
     * @param id the template id.
     * @param body the value object.
     * @return the transaction id for the document generation.
     */
    String postTemplateGenerationJob(String id, Object body);

}
