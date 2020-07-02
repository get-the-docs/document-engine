package net.videki.templateutils.template.core;

import net.videki.templateutils.template.core.documentstructure.ResultDocument;
import net.videki.templateutils.template.core.documentstructure.GenerationResult;
import net.videki.templateutils.template.core.service.TemplateServiceParamTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceParamTest.class);

    public static void closeResults(GenerationResult result) {
        if (result != null) {
            for (final ResultDocument as : result.getResults()) {
                try {
                    if (as != null && as.getContent() != null) {
                        as.getContent().close();
                    }
                } catch (IOException e) {
                    LOGGER.error("Error closing result stream.", e);
                }
            }
        }
    }

}
