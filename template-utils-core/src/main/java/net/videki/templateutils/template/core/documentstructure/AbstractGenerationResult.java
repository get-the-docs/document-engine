package net.videki.templateutils.template.core.documentstructure;

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

    protected String transactionId;
    protected Instant generationStartTime;
    protected Instant generationEndTime;

    public AbstractGenerationResult() {
        this.transactionId = UUID.randomUUID().toString();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Instant getGenerationStartTime() {
        return generationStartTime;
    }

    public void setGenerationStartTime(Instant generationStartTime) {
        this.generationStartTime = generationStartTime;
    }

    public Instant getGenerationEndTime() {
        return generationEndTime;
    }

    public void setGenerationEndTime(Instant getGenerationEndTime) {
        this.generationEndTime = getGenerationEndTime;
    }
}
