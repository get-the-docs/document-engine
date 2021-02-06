package net.videki.templateutils.template.core.documentstructure;

import net.videki.templateutils.template.core.documentstructure.descriptors.GenerationResultMode;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;
import net.videki.templateutils.template.core.dto.JsonModel;
import net.videki.templateutils.template.core.service.OutputFormat;

import java.util.*;

/**
 * <p>If multiple documents have to be generated, the DocumentStructure describes the template structure of
 * the desired output document collection.</p>
 *
 * @author Levente Ban
 */
public class DocumentStructure implements JsonModel {

    /** The template structure's unique id */
    private final String documentStructureId;

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

    public DocumentStructure() {
        this.documentStructureId = UUID.randomUUID().toString();
    }

    public DocumentStructure(final String id) {
        this.documentStructureId = id;
    }

    public List<TemplateElement> getElements() {
        return elements;
    }

    public Optional<TemplateElementId> getElementIdByFriendlyName(final String friendlyName) {
        Optional<TemplateElementId> result = Optional.empty();

        for (final TemplateElement actElement : this.elements) {
            if (actElement.getTemplateElementId().getId().equals(friendlyName)) {
                result = Optional.of(actElement.getTemplateElementId());
            }
        }
        return result;
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

    public String getDocumentStructureId() {
        return documentStructureId;
    }

    public Optional<TemplateElement> getElementByFriendlyName(final String friendlyName) {
        Optional<TemplateElement> result = Optional.empty();

        for (final TemplateElement actElement : this.elements) {
            if (actElement.getTemplateElementId().getId().equals(friendlyName)) {
                result = Optional.of(actElement);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "DocumentStructure{" +
                "documentStructureId='" + documentStructureId + '\'' +
                ", elements=" + elements +
                ", resultMode=" + resultMode +
                ", outputFormat=" + outputFormat +
                ", copies=" + copies +
                '}';
    }
}
