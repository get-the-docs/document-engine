package net.videki.templateutils.api.document.service;

/*-
 * #%L
 * template-utils-service-api
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

import java.util.Optional;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
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
     * @param id   the document structure's id in the repository.
     * @param page the page to retrieve (effective only if the document structure
     *             repository implementation has paging capability).
     * @return the requested page, if exists.
     */
    Optional<Page<DocumentStructure>> getDocumentStructures(String id, Pageable page);

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
     * @param transactionId   the transaction id.
     * @param id              the document structure id.
     * @param values          the value set.
     * @param notificationUrl notification url, optional.
     */
    void postDocumentStructureGenerationJob(String transactionId, String id, ValueSet values, String notificationUrl);
}
