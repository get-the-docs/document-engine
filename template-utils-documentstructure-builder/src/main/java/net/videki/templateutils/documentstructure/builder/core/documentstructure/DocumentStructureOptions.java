package net.videki.templateutils.documentstructure.builder.core.documentstructure;

import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DocumentStructureOptions {

    /** The template structure's unique id */
    private final String documentStructureId;
    /**
     * The document parts
     */
    private final List<OptionalTemplateElement> elements = new LinkedList<>();

    public DocumentStructureOptions() {
        this.documentStructureId = UUID.randomUUID().toString();
    }

    public DocumentStructureOptions(final String id) {
        this.documentStructureId = id;
    }

    public List<OptionalTemplateElement> getElements() {
        return elements;
    }

    public Optional<TemplateElementId> getElementIdByFriendlyName(final String friendlyName) {
        Optional<TemplateElementId> result = Optional.empty();

        for (final TemplateElement actElement : this.elements) {
            if (actElement.getTemplateElementId().equals(friendlyName)) {
                result = Optional.of(actElement.getTemplateElementId());
            }
        }
        return result;
    }

    public String getDocumentStructureId() {
        return documentStructureId;
    }

    @Override
    public String toString() {
        return "DocumentStructure{" +
                "documentStructureId='" + documentStructureId + '\'' +
                ", elements=" + elements +
                '}';
    }
}
