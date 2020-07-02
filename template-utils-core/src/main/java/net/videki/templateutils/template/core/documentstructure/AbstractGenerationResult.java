package net.videki.templateutils.template.core.documentstructure;

import java.time.Instant;
/**
 * Document template generation result for a document structure.
 * It contains the generated document streams. The whole generation is linked through the transaction id,
 * which identifies the transaction id referring the valueset.
 *
 * @author Levente Ban
 */
public abstract class AbstractGenerationResult {

    private String transactionId;
    private Instant generationStartTime;
    private Instant generationEndTime;

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

    public Instant getGetGenerationEndTime() {
        return generationEndTime;
    }

    public void setGenerationEndTime(Instant getGenerationEndTime) {
        this.generationEndTime = getGenerationEndTime;
    }

    @Override
    public String toString() {
        return "AbstractGenerationResult{" +
                ", transactionId='" + this.transactionId + '\'' +
                ", generationStartTime=" + this.generationStartTime +
                ", getGenerationEndTime=" + this.generationEndTime +
                '}';
    }
}
