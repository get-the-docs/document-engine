package net.videki.templateutils.template.core.provider.templaterepository;

import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.core.template.descriptors.TemplateDocument;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Template document repository.
 * 
 * @author Levente Ban
 */
public interface TemplateRepository {

    /**
     * Init hook for constructing the repository. 
     * @param props actual env properties
     * @throws TemplateServiceConfigurationException if the repository construction was not successful
     */
    void init(Properties props) throws TemplateServiceConfigurationException;

    /**
     * Returns the actual template document list, if the repository provides this feature.
     * @param page the requested page to return
     * @throws TemplateServiceException thrown in case of query error
     * @return the template element list
     */
    Page<TemplateDocument> getTemplates(Pageable page) throws TemplateServiceException;

    /**
     * Returns a given template document, if the repository provides this feature and found.
     * @param id the requested descriptor, if found
     * @param version the template version for the id
     * @param withBinary true, if the template binary should be part of the response 
     * @throws TemplateServiceException thrown in case of query error
     * @return the template document
     */
    Optional<TemplateDocument> getTemplateDocumentById(String id, String version, boolean withBinary) throws TemplateServiceException;
    
    /**
     * Returns a given template as a stream, or null if not found.
     * @param templateFile the template document id
     * @return The given template as a stream, or null if not found.
     */
    InputStream getTemplate(String templateFile);

}
