package net.videki.templateutils.template.core.documentstructure;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Document template generation result for a document structure.
 * It contains the generated document file names and their success flag.
 * The whole generation is linked through the transaction id,
 * which identifies the transaction id referring the valueset.
 *
 * @author Levente Ban
 */
public class StoredGenerationResult extends AbstractGenerationResult {

    private final List<StoredResultDocument> results;
    private String transactionId;
    private Instant generationStartTime;
    private Instant getGenerationEndTime;

    public StoredGenerationResult(final List<StoredResultDocument> results) {
        super();

        if (results != null) {
            this.results = results;
        } else {
            this.results  = new LinkedList<>();
        }
    }

    public StoredGenerationResult(final String transactionId, final List<StoredResultDocument> results) {
        this(results);

        setTransactionId(transactionId);
    }

    public List<StoredResultDocument> getResults() {
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
                "results=" + this.results +
                ", transactionId='" + this.transactionId + '\'' +
                ", generationStartTime=" + this.generationStartTime +
                ", getGenerationEndTime=" + this.getGenerationEndTime +
                '}';
    }
}
