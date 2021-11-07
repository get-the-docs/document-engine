package net.videki.templateutils.template.core.documentstructure;

import java.util.LinkedList;
import java.util.List;

/**
 * Document template generation result for a document structure.
 * It contains the generated document streams. The whole generation is linked through the transaction id,
 * which identifies the transaction id referring the valueset.
 *
 * @author Levente Ban
 */
public class GenerationResult extends AbstractGenerationResult {

    /**
     * The list of the result documents.
     */
    private final List<ResultDocument> results;

    /**
     * Initializes the container with a list of result documents.
     * @param results the list of result documents.
     */
    public GenerationResult(final List<ResultDocument> results) {
        super();

        if (results != null) {
            this.results = results;
        } else {
            this.results  = new LinkedList<>();
        }
    }

    /**
     * Returns the list of reesult documents.
     * @return the result documents.
     */
    public List<ResultDocument> getResults() {
        return results;
    }

    /**
     * Convencience method for logging.
     */
    @Override
    public String toString() {
        return "GenerationResult{" +
                "results=" + results +
                ", transactionId='" + this.getTransactionId() + '\'' +
                ", generationStartTime=" + this.getGenerationStartTime() +
                ", generationEndTime=" + this.getGenerationEndTime() +
                '}';
    }
}
