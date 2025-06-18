package org.getthedocs.documentengine.core.provider.resultstore;

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

import org.getthedocs.documentengine.core.documentstructure.ResultDocument;
import org.getthedocs.documentengine.core.documentstructure.GenerationResult;
import org.getthedocs.documentengine.core.documentstructure.StoredResultDocument;
import org.getthedocs.documentengine.core.documentstructure.StoredGenerationResult;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;

import java.util.Optional;
import java.util.Properties;

/**
 * Adapter interface for saving the template generation results through the desired implementation set
 * in the TemplateServiceConfiguration.
 *
 * @author Levente Ban
 */
public interface ResultStore {

    /**
     * Repository initializer entry point.
     * @param props the system properties (see document-engine.properties).
     * @throws TemplateServiceConfigurationException thrown in case of configuration errors.
     */
    void init(Properties props) throws TemplateServiceConfigurationException;

    /**
     * Registers a transaction in the result store to let it appear in the queries.
     * @param transactionId the transaction id
     * @return success flag.
     */
    boolean registerTransaction(String transactionId);

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
