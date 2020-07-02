package net.videki.templateutils.template.core.documentstructure;

import java.io.OutputStream;

/**
 * <p>If multiple documents have to be generated, the DocumentStructure contains
 * the template file name and its binary.</p>
 *
 * @author Levente Ban
 */
public class ResultDocument extends AbstractResultDocument implements AutoCloseable {

    /**
     * The document binary
     */
    private final OutputStream content;

    public ResultDocument(final String fileName, final OutputStream content) {
        super(fileName);

        this.content = content;
    }

    public OutputStream getContent() {
        return content;
    }

    @Override
    public void close() throws Exception {
        if (this.content != null) {
            this.content.close();
        }
    }
}
