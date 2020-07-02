package net.videki.templateutils.template.core.documentstructure;

/**
 * @author Levente Ban
 *
 * <p>If multiple documents have to be generated, the DocumentStructure contains
 * the template file name and its success flag.</p>
 */
public class StoredResultDocument extends AbstractResultDocument {

    /**
     * Indicates whether the generation was successful
     */
    private final boolean generated;

    public StoredResultDocument(final String fileName, final boolean generated) {
        super(fileName);

        this.generated = generated;
    }

    /**
     * Indicates whether the document was generated and saved successfully by the result store.
     * @return true on success, false otherwise
     */
    public boolean isGenerated() {
        return this.generated;
    }

}
