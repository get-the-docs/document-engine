package net.videki.templateutils.template.core.documentstructure.v1;

/*-
 * #%L
 * template-utils-core
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
import java.util.Objects;

/**
 * Document template generation result for a document structure.
 * It contains the generated document streams. The whole generation is linked through the transaction id,
 * which identifies the transaction id referring the valueset.
 *
 * @author Levente Ban
 */
public class GenerationResult extends AbstractGenerationResult {

    /**
     * The list of the result documents.
     */
    private final List<ResultDocument> results;

    /**
     * The value set transaction id.
     */
    private String valueSetTransactionId;

    /**
     * Initializes the container with a list of result documents.
     * @param results the list of result documents.
     */
    public GenerationResult(final List<ResultDocument> results) {
        super();

        this.results = Objects.requireNonNullElseGet(results, LinkedList::new);
    }

    /**
     * Returns the list of reesult documents.
     * @return the result documents.
     */
    public List<ResultDocument> getResults() {
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
     * Convencience method for logging.
     */
    @Override
    public String toString() {
        return "GenerationResult{" +
                "results=" + results +
                ", transactionId='" + this.getTransactionId() + '\'' +
                ", valueSetTransactionId='" + this.getValueSetTransactionId() + '\'' +
                ", generationStartTime=" + this.getGenerationStartTime() +
                ", generationEndTime=" + this.getGenerationEndTime() +
                '}';
    }
}
