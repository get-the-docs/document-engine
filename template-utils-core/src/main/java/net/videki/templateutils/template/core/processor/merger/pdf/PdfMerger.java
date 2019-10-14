package net.videki.templateutils.template.core.processor.merger.pdf;

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

public class PdfMerger implements OutputMerger {
    private static final Logger LOGGER         = LoggerFactory.getLogger(OutputMerger.class);

    @Override
    public OutputFormat getOutputFormat() {
        return OutputFormat.PDF;
    }

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
