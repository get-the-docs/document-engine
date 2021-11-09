package net.videki.templateutils.template.core.documentstructure;

import java.beans.Transient;

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

    @Transient
    public byte[] getBinary() {
        return this.binary;
    }
}
