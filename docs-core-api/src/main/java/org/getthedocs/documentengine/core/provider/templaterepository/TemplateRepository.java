package org.getthedocs.documentengine.core.provider.templaterepository;

/*-
 * #%L
 * docs-core
 * %%
 * Copyright (C) 2021 Levente Ban
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.getthedocs.documentengine.core.provider.persistence.Page;
import org.getthedocs.documentengine.core.provider.persistence.Pageable;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.getthedocs.documentengine.core.template.descriptors.TemplateDocument;

import java.io.InputStream;
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
     * @return the template document
     */
    Optional<TemplateDocument> getTemplateDocumentById(String id, String version, boolean withBinary);
    
    /**
     * Returns a given template as a stream, or null if not found.
     * @param templateFile the template document id
     * @return The given template as a stream, or null if not found.
     * @throws TemplateServiceConfigurationException thrown in case of configuration errors.
     */
    InputStream getTemplate(String templateFile) throws TemplateServiceConfigurationException;

}
