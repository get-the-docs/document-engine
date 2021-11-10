package net.videki.templateutils.template.core.documentstructure;

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

import java.beans.Transient;

import net.videki.templateutils.template.core.documentstructure.descriptors.StoredResultDocumentStatus;

/**
 * @author Levente Ban
 *
 * <p>If multiple documents have to be generated, the DocumentStructure contains
 * the template file name and its success flag.</p>
 */
public class StoredResultDocument extends AbstractResultDocument {

    /**
     * Indicates whether the generation was successful
     */
    private final boolean generated;


    /**
     * Document status.
     */
    private StoredResultDocumentStatus status;

    /**
     * The document binary, if requested.
     */
    private byte[] binary;

    /**
     * Initializes the container with a result document name and its success flag (whether it was generated successfully or not).
     * @param fileName the result document file name.
     * @param generated true, if the generation was successful.
     */
    public StoredResultDocument(final String fileName, final boolean generated) {
        this(fileName, generated, null);
    }

    /**
     * Initializes the container with a result document name and its success flag (whether it was generated successfully or not).
     * @param fileName the result document file name.
     * @param generated true, if the generation was successful.
     * @param binary the result document, if requested.
     */
    public StoredResultDocument(final String fileName, final boolean generated, final byte[] binary) {
        super(fileName);

        this.generated = generated;
        this.binary = binary;
    }


    /**
     * Initializes the container with a result document name and its success flag (whether it was generated successfully or not).
     * @param transactionId the transaction id, if defined.
     * @param fileName the result document file name.
     * @param generated true, if the generation was successful.
     */
    public StoredResultDocument(final String transactionId, final String fileName, final boolean generated) {
        this(transactionId, fileName, generated, null);
    }

    /**
     * Initializes the container with a result document name and its success flag (whether it was generated successfully or not).
     * @param transactionId the transaction id, if defined.
     * @param fileName the result document file name.
     * @param generated true, if the generation was successful.
     * @param binary the result document, if requested.
     */
    public StoredResultDocument(final String transactionId, final String fileName, final boolean generated, final byte[] binary) {
        super(transactionId, fileName);

        this.generated = generated;
        this.binary = binary;
    }

    /**
     * Indicates whether the document was generated and saved successfully by the result store.
     * @return true on success, false otherwise
     */
    public boolean isGenerated() {
        return this.generated;
    }

    /**
     * Returns the document status. 
     * @return the document status.
     */
    public StoredResultDocumentStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the document status.
     * @param status the document status.
     */
    public void setStatus(final StoredResultDocumentStatus status) {
        this.status = status;
    }

    /**
     * Returns the binary, if requested.
     * @return the binary.
     */
    @Transient
    public byte[] getBinary() {
        return this.binary;
    }
}
