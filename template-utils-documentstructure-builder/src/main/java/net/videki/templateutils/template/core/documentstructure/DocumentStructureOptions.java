package net.videki.templateutils.template.core.documentstructure;

/*-
 * #%L
 * template-utils-documentstructure-builder
 * %%
 * Copyright (C) 2021 Levente Ban
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import net.videki.templateutils.template.core.documentstructure.v1.OptionalTemplateElement;
import net.videki.templateutils.template.core.documentstructure.v1.TemplateElement;
import net.videki.templateutils.template.core.documentstructure.v1.TemplateElementId;

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
