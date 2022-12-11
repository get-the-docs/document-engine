package net.videki.templateutils.template.core.documentstructure;

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

import java.time.Instant;
import java.util.UUID;

/**
 * Document template generation result for a document structure.
 * It contains the generated document streams. The whole generation is linked through the transaction id,
 * which identifies the transaction id referring the valueset.
 *
 * @author Levente Ban
 */
public abstract class AbstractGenerationResult {

    /**
     * Document generation unique transaction id.
     */
    protected String transactionId;

    /**
     * Document generation start time.
     */
    protected Instant generationStartTime;

    /**
     * Document generation end time.
     */
    protected Instant generationEndTime;

    /**
     * Default consructor to initialize the container. 
     */
    public AbstractGenerationResult() {
        this.transactionId = UUID.randomUUID().toString();
    }

    /**
     * Returns the document structure generation result transacion id - uuid v4 by default.
     * It represents the folder or container name (folder, etc.) in the result store, containing the output documents. 
     * @return the transaction id.
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the document structure generation result transacion id - uuid v4 by default.
     * It represents the folder or container name (folder, etc.) in the result store, containing the output documents. 
     * @param transactionId the transaction id.
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Returns the generation result's start time.
     * @return the generation start time.
     */
    public Instant getGenerationStartTime() {
        return generationStartTime;
    }

    /**
     * Sets the generation result's start time.
     * @param generationStartTime the generation start time.
     */
    public void setGenerationStartTime(Instant generationStartTime) {
        this.generationStartTime = generationStartTime;
    }

    /**
     * Returns the generation end time.
     * @return the generation end time.
     */
    public Instant getGenerationEndTime() {
        return generationEndTime;
    }

    /**
     * Sets the generation end time.
     * @param getGenerationEndTime the generation end time.
     */
    public void setGenerationEndTime(Instant getGenerationEndTime) {
        this.generationEndTime = getGenerationEndTime;
    }
}
