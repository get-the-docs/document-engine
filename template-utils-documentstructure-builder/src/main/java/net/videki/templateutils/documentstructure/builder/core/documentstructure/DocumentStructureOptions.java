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
            if (actElement.getTemplateElementId() != null &&
                    actElement.getTemplateElementId().getId().equals(friendlyName)) {
                result = Optional.of(actElement.getTemplateElementId());
            }
        }
        return result;
    }

    public String getDocumentStructureId() {
        return documentStructureId;
    }

    public Optional<OptionalTemplateElement> getElementByFriendlyName(final String friendlyName) {
        Optional<OptionalTemplateElement> result = Optional.empty();

        for (final OptionalTemplateElement actElement : this.elements) {
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
                '}';
    }
}
