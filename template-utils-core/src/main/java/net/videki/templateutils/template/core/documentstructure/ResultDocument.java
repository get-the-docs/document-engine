package net.videki.templateutils.template.core.documentstructure;

import java.io.OutputStream;

/**
 * <p>
 * If multiple documents have to be generated, the DocumentStructure contains
 * the template file name and its binary.
 * </p>
 *
 * @author Levente Ban
 */
public class ResultDocument extends AbstractResultDocument implements AutoCloseable {

    /**
     * The document binary
     */
    private final OutputStream content;

    /**
     * Initializes the container with an actual content.
     * 
     * @param fileName the filename to store the result under.
     * @param content  the actual contents.
     */
    public ResultDocument(final String fileName, final OutputStream content) {
        super(fileName);

        this.content = content;
    }

    /**
     * Initializes the container with an actual content.
     * 
     * @param transactionId the transaction id if defined.
     * @param fileName      the filename to store the result under.
     * @param content       the actual contents.
     */
    public ResultDocument(final String transactionId, final String fileName, final OutputStream content) {
        super(transactionId, fileName);

        this.content = content;
    }

    /**
     * Returns the document contents (the binary stream in the appropriate format).
     * 
     * @return the result document.
     */
    public OutputStream getContent() {
        return content;
    }

    /**
     * Closes the result document stream.
     */
    @Override
    public void close() throws Exception {
        if (this.content != null) {
            this.content.close();
        }
    }
}
