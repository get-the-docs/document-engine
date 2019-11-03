package net.videki.templateutils.template.core.documentstructure;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Levente Ban
 *
 * Document template generation result for a document structure.
 */
public class GenerationResult {

    private final List<DocumentResult> results;
    private String transactionId;
    private Instant generationStartTime;
    private Instant getGenerationEndTime;

    public GenerationResult(List<DocumentResult> results) {
        if (results != null) {
            this.results = results;
        } else {
            this.results  = new LinkedList<>();
        }
    }

    public List<DocumentResult> getResults() {
        return results;
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

    public Instant getGetGenerationEndTime() {
        return getGenerationEndTime;
    }

    public void setGenerationEndTime(Instant getGenerationEndTime) {
        this.getGenerationEndTime = getGenerationEndTime;
    }

    @Override
    public String toString() {
        return "GenerationResult{" +
                "results=" + results +
                ", transactionId='" + transactionId + '\'' +
                ", generationStartTime=" + generationStartTime +
                ", getGenerationEndTime=" + getGenerationEndTime +
                '}';
    }
}
