package org.getthedocs.documentengine.core.provider.documentstructure;

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
import org.getthedocs.documentengine.core.documentstructure.DocumentStructure;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;

import java.util.Properties;

/**
 * Base document structure repository interface.
 * 
 * @author Levente Ban
 */
public interface DocumentStructureRepository {

    /**
     * Configuration property key for the builder implementation to (de)serialize
     * the document structure descriptors.
     */
    String DOCUMENT_STRUCTURE_BUILDER = "repository.documentstructure.builder";

    /**
     * Entry point for repository initialization.
     * 
     * @param props configuration properties (see document-engine.properties)
     * @throws TemplateServiceConfigurationException thrown in case of
     *                                               initialization errors based on
     *                                               configuration errors.
     */
    void init(Properties props) throws TemplateServiceConfigurationException;

    /**
     * Returns the actual document structure list, if the repository provides this
     * feature.
     * 
     * @param page the requested page to return
     * @throws TemplateServiceException thrown in case of query error
     * @return the document structure list
     */
    Page<DocumentStructure> getDocumentStructures(Pageable page) throws TemplateServiceException;

    /**
     * Returns a document structure identified by its id in the configured document
     * structure repository.
     * 
     * @param ds the document structure id.
     * @return the document structure descriptor.
     * @throws TemplateServiceConfigurationException thrown in case of repository
     *                                               configuration error.
     */
    DocumentStructure getDocumentStructure(String ds) throws TemplateServiceConfigurationException;

}
