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

    /**
     * The list of result documents.
     */
    private final List<StoredResultDocument> results;

    /**
     * Initializes the container with a list of result documents with a random transaction id.
     * @param results the list of the result documents.
     */
    public StoredGenerationResult(final List<StoredResultDocument> results) {
        super();

        if (results != null) {
            this.results = results;
        } else {
            this.results  = new LinkedList<>();
        }
    }

    /**
     * Initializes the container with a predefined transaction id and result document set.
     * @param transactionId the document structure generation transaction id.
     * @param results the result document list (list of their streams).
     */
    public StoredGenerationResult(final String transactionId, final List<StoredResultDocument> results) {
        this(results);

        setTransactionId(transactionId);
    }

    /**
     * Returns the result documents' descriptors (file names, etc.).
     * @return the list of the result document descriptors.
     */
    public List<StoredResultDocument> getResults() {
        return results;
    }

    /**
     * Convenience method for logging.
     */
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
