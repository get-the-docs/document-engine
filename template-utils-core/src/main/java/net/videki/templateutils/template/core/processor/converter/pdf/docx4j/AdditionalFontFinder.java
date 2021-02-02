package net.videki.templateutils.template.core.processor.converter.pdf.docx4j;

import net.videki.templateutils.template.core.configuration.FontConfig;
import org.docx4j.fonts.PhysicalFonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class AdditionalFontFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocxToPdfConverter.class);

    private static URL getURL(final Object o) throws Exception {

        if (o instanceof java.io.File) {
            java.io.File f = (java.io.File)o;

            return f.toURL();
        } else if (o instanceof java.net.URL) {
            return (URL)o;
        } else {
            throw new Exception("Unexpected object:" + o.getClass().getName() );
        }
    }

    public static void discoverFonts(final List<FontConfig> fontConfigList) {
        for (Iterator iter = fontConfigList.iterator(); iter.hasNext();) {
            try {
                final URL fontUrl = getURL(iter.next());

                PhysicalFonts.addPhysicalFont(fontUrl);
            } catch (final Exception e) {
                LOGGER.warn("Error initializing template-utils config based additional fonts", e);
            }
        }
    }

}
