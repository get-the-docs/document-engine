package net.videki.templateutils.template.core.documentstructure.descriptors;

/**
 * @author Levente Ban
 *
 * Defines the generation result of the whole generation.
 */
public enum GenerationResultMode {
    /**
     * Generate one output for all template elements
     */
    SEPARATE_DOCUMENTS,
    /**
     * Generate one result document by concatenating all parts defined by the document structure order and counts
     */
    SINGLE_OUTPUT;
}
