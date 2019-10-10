package net.videki.templateutils.template.core.documentstructure;

import net.videki.templateutils.template.core.documentstructure.descriptors.GenerationResultMode;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.service.OutputFormat;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Levente Ban
 *
 * <p>If multiple documents have to be generated, the DocumentStructure describes the template structure of
 * the desired output document collection.</p>
 * <p></p>
 */
public class DocumentStructure {
    /**
     * The document parts
     */
    private final List<TemplateElement> elements = new LinkedList<>();
    /**
     * Sets the output - one/many files (the formats will be checked before generation against
     * the actual converter set)
     */
    private GenerationResultMode resultMode = GenerationResultMode.SEPARATE_DOCUMENTS;
    /**
     * The main output format
     */
    private OutputFormat outputFormat = OutputFormat.UNCHANGED;

    /** The number of copies */
    private int copies = 1;

    public List<TemplateElement> getElements() {
        return elements;
    }

    public GenerationResultMode getResultMode() {
        return resultMode;
    }

    public void setResultMode(GenerationResultMode resultMode) {
        this.resultMode = resultMode;
    }


    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }
}
