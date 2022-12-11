package net.videki.documentengine.core.documentstructure;

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

import java.util.LinkedList;
import java.util.List;

/**
 * Document template generation result for a document structure.
 * It contains the generated document file names and their success flag.
 * The whole generation is linked through the transaction id,
 * which identifies the transaction id referring the valueset.
 *
 * @author Levente Ban
 */
public class StoredGenerationResult extends AbstractGenerationResult {

    /**
     * The list of result documents.
     */
    private final List<StoredResultDocument> results;

    /**
     * The value set transaction id.
     */
    private String valueSetTransactionId;

    /**
     * Initializes the container with a list of result documents with a random transaction id.
     * @param results the list of the result documents.
     */
    public StoredGenerationResult(final List<StoredResultDocument> results) {
        super();

        if (results != null) {
            this.results = results;
        } else {
            this.results  = new LinkedList<>();
        }
    }

    /**
     * Initializes the container with a predefined transaction id and result document set.
     * @param transactionId the document structure generation transaction id.
     * @param results the result document list (list of their streams).
     */
    public StoredGenerationResult(final String transactionId, final List<StoredResultDocument> results) {
        this(results);

        setTransactionId(transactionId);
    }

    /**
     * Returns the result documents' descriptors (file names, etc.).
     * @return the list of the result document descriptors.
     */
    public List<StoredResultDocument> getResults() {
        return results;
    }

    /**
     * Sets the value set transaction id (it describes the business transaction to which the model objects belong).
     * @param valueSetTransactionId the value set transaction id.
     */
    public void setValueSetTransactionId(String valueSetTransactionId) {
        this.valueSetTransactionId = valueSetTransactionId;
    }

    /**
     * Returns the value set's transaction id.
     * @return the value set transaction id.
     */
    public String getValueSetTransactionId() {
        return this.valueSetTransactionId;
    }

    /**
     * Convenience method for logging.
     */
    @Override
    public String toString() {
        return "GenerationResult{" +
                "results=" + this.results +
                ", transactionId='" + this.getTransactionId() + '\'' +
                ", valueSetTransactionId='" + this.getValueSetTransactionId() + '\'' +
                ", generationStartTime=" + this.getGenerationStartTime() +
                ", generationEndTime=" + this.getGenerationEndTime() +
                '}';
    }
}
