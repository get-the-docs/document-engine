package net.videki.templateutils.template.core;

import net.videki.templateutils.template.core.service.TemplateServiceParamTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceParamTest.class);

    public static void closeResults(List<OutputStream> resultDocs) {
        if (resultDocs != null) {
            for (final OutputStream as : resultDocs) {
                try {
                    as.close();
                } catch (IOException e) {
                    LOGGER.error("Error closing result stream.", e);
                }
            }
        }
    }

}
