package net.videki.templateutils.template.core.documentstructure;

import java.nio.file.Paths;
import java.util.UUID;

/**
 * <p>If multiple documents have to be generated, the DocumentStructure contains
 * the template file name.</p>
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
     * @param fileName the document file name.
     */
    public AbstractResultDocument(final String fileName) {
        this.transactionId = UUID.randomUUID().toString();
        this.fileName = Paths.get(fileName).getFileName().toString();
    }

    /**
     * Returns the file name.
     * @return the file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the transaction id.
     * @return the transaction id.
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the transaction id under which the document generation was performed.
     * (it equals with the container - folder name, etc. - of the result store)  
     * @param transactionId the transcation id.
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
