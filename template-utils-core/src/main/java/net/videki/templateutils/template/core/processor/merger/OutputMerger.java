package net.videki.templateutils.template.core.processor.merger;

import net.videki.templateutils.template.core.service.OutputFormat;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Base interface for document merger implementations.
 * Mergers are implementations to combine documents to a single result document.
 * @author Levente Ban
 */
public interface OutputMerger {

    /**
     * Returns the output format which the merger supports. 
     * @return the output format.
     */
    OutputFormat getOutputFormat();

    /**
     * Entry point for merging a list of documents having the format of the merger output format.
     * @param sources the documents to be processed.
     * @return the result document stream if the merge was successful.
     */
    OutputStream merge(final List<InputStream> sources);
}
