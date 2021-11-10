package net.videki.templateutils.template.core.processor.merger.pdf;

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

import net.videki.templateutils.template.core.util.FileSystemHelper;
import net.videki.templateutils.template.core.processor.merger.OutputMerger;
import net.videki.templateutils.template.core.service.OutputFormat;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Document merger implementation based on the Apache Pdfbox library.
 * see https://pdfbox.apache.org/
 * @author Levente Ban
 */
public class PdfMerger implements OutputMerger {
    /**
     * Logger.
     */
    private static final Logger LOGGER         = LoggerFactory.getLogger(OutputMerger.class);

    /**
     * Returns the supported output format.
     * @return the output format (pdf).
     */
    @Override
    public OutputFormat getOutputFormat() {
        return OutputFormat.PDF;
    }

    /**
     * Entry point for document merging.
     * The merge will happen in the order specified by the pdfPart parameter's order.
     * @param pdfParts the list of pdf documents to merge. 
     * @return the merged pdf document if the merge was successful.
     */
    @Override
    public OutputStream merge(final List<InputStream> pdfParts) {
        OutputStream result = FileSystemHelper.getOutputStream();

        try {
            // Should be kept per thread since the converter is NOT thread safe
            final PDFMergerUtility pdfMerger = new PDFMergerUtility();

            LOGGER.debug("pdf Part size: " + pdfParts.size());
            for (InputStream actPart : pdfParts) {
                pdfMerger.addSource(actPart);
                LOGGER.debug("pdf Part [i]: " + actPart);
            }

            pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            pdfMerger.setDestinationStream(result);
        } catch (final IOException e) {
            LOGGER.error("Error on pdf concatenation: ", e);

            result = null;
        }

        return result;
    }

}
