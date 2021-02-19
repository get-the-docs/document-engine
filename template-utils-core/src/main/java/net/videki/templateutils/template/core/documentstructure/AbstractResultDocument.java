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

    public AbstractResultDocument(final String fileName) {
        this.transactionId = UUID.randomUUID().toString();
        this.fileName = Paths.get(fileName).getFileName().toString();
    }

    public String getFileName() {
        return fileName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
