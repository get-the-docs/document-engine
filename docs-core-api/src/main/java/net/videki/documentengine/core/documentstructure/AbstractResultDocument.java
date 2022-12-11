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

import java.nio.file.Paths;
import java.util.UUID;

/**
 * <p>
 * If multiple documents have to be generated, the DocumentStructure contains
 * the template file name.
 * </p>
 *
 * @author Levente Ban
 */
public abstract class AbstractResultDocument {
    /** Document generation id */
    private String transactionId;

    /**
     * The result's filename
     */
    private final String fileName;

    /**
     * Initializes the container for a given file name.
     * 
     * @param fileName the document file name.
     */
    public AbstractResultDocument(final String fileName) {
        this(null, fileName);
    }

    /**
     * Initializes the container for a given file name.
     * 
     * @param transactionId the transaction id, if defined.
     * @param fileName      the document file name.
     */
    public AbstractResultDocument(final String transactionId, final String fileName) {
        if (transactionId != null) {
            this.transactionId = transactionId; 
        } else {
            this.transactionId = UUID.randomUUID().toString();
        }

        this.fileName = Paths.get(fileName).getFileName().toString();
    }

    /**
     * Returns the file name.
     * 
     * @return the file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the transaction id.
     * 
     * @return the transaction id.
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the transaction id under which the document generation was performed.
     * (it equals with the container - folder name, etc. - of the result store)
     * 
     * @param transactionId the transcation id.
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
