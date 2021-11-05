package net.videki.templateutils.template.core.documentstructure;

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

    @Override
    public String toString() {
        return "GenerationResult{" +
                "results=" + this.results +
                ", transactionId='" + this.getTransactionId() + '\'' +
                ", generationStartTime=" + this.getGenerationStartTime() +
                ", generationEndTime=" + this.getGenerationEndTime() +
                '}';
    }
}
