package net.videki.templateutils.template.core.documentstructure;

/*-
 * #%L
 * template-utils-core
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

    /**
     * Default constructor to initialize the data structure.
     * (By default the transaction id with a random uuid)
     */
    public DocumentStructure() {
        this.documentStructureId = UUID.randomUUID().toString();
    }

    /**
     * Initializes the container with a pre-defined transaction id.
     * This setup is used in case of active result stores, where the document conatiner name is generated by the storage system, or
     * has to be identical with the enclosing business transaction.
     * @param id the transaction id to be used during generation.
     */
    public DocumentStructure(final String id) {
        this.documentStructureId = id;
    }

    /**
     * Returns the template elements within the document structure.
     * @return the current template element list.
     */
    public List<TemplateElement> getElements() {
        return elements;
    }

    /**
     * Returns a template element descriptor within the document structure identified by its friendly name.
     * @param friendlyName the template element friendly name.
     * @return the template element if found.
     */
    public Optional<TemplateElementId> getElementIdByFriendlyName(final String friendlyName) {
        Optional<TemplateElementId> result = Optional.empty();

        for (final TemplateElement actElement : this.elements) {
            if (actElement.getTemplateElementId().getId().equals(friendlyName)) {
                result = Optional.of(actElement.getTemplateElementId());
            }
        }
        return result;
    }

    /**
     * Returns the document generation result's mode - single (concatenated), or separate documents. 
     * @return the generation result mode.
     */
    public GenerationResultMode getResultMode() {
        return resultMode;
    }

    /**
     * Sets the document generation result mode.
     * @param resultMode result's mode - single (concatenated), or separate documents.
     */
    public void setResultMode(GenerationResultMode resultMode) {
        this.resultMode = resultMode;
    }

    /**
     * Returns the output format to which the result will be converted.
     * @return the output format.
     */
    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    /**
     * Sets the output format to which the result will be converted.
     * @param outputFormat the output format.
     */
    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    /**
     * Returns the number of copies at document level of the generated documents.
     * @return the number of copies.
     */
    public int getCopies() {
        return copies;
    }

    /**
     * Sets the number of copies at document level of the generated documents.
     * @param copies the number of copies.
     */
    public void setCopies(int copies) {
        this.copies = copies;
    }

    /**
     * Returns the document structure id.
     * @return the document structure id.
     */
    public String getDocumentStructureId() {
        return documentStructureId;
    }

    /**
     * Returns a template element defined by the document structure, selected by its friendly name.
     * @param friendlyName the template element's friendly name.
     * @return the template element, if found.
     */
    public Optional<TemplateElement> getElementByFriendlyName(final String friendlyName) {
        Optional<TemplateElement> result = Optional.empty();

        for (final TemplateElement actElement : this.elements) {
            if (actElement.getTemplateElementId().getId().equals(friendlyName)) {
                result = Optional.of(actElement);
            }
        }
        return result;
    }

    /**
     * Convencience method for logging.
     */
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
