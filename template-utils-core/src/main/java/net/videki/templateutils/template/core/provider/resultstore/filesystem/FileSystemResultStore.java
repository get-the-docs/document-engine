package net.videki.templateutils.template.core.provider.resultstore.filesystem;

import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.documentstructure.DocumentResult;
import net.videki.templateutils.template.core.documentstructure.GenerationResult;
import net.videki.templateutils.template.core.provider.resultstore.ResultStore;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.util.FileSystemHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSystemResultStore implements ResultStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemResultStore.class);
    private static final String CONFIG_PROPERTY_BASEDIR = "repository.result.provider.FileSystemResultStore.basedir";

    @Override
    public void save(final GenerationResult result) {
        if (result == null) {
            throw new TemplateProcessException("0d383b14-c3ef-430d-9007-3add27e086d8",
                    "No result caught.");
        }

        final TemplateServiceConfiguration configuration = TemplateServiceConfiguration.getInstance();
        final String projectOutDir =
                configuration.getConfigurationProperties().getProperty(CONFIG_PROPERTY_BASEDIR);

        final String resultDir = FileSystemHelper.getFullPath(projectOutDir, result.getTransactionId());

        final File subdir = new File(resultDir);
        if (!subdir.exists()) {
            if (!subdir.mkdirs()) {
                throw new IllegalArgumentException("Cannot create directory: " + resultDir);
            }
        }

        for (final DocumentResult actResult : result.getResults()) {

            final String resultFileName = FileSystemHelper.getFullPath(resultDir, actResult.getFileName());

            LOGGER.info("Result file: {}.", resultFileName);

            try (FileOutputStream o =
                         new FileOutputStream(resultFileName, false)) {

                o.write(((ByteArrayOutputStream) actResult.getContent()).toByteArray());
                o.flush();
            } catch (IOException e) {
                LOGGER.error("Error saving the result file.", e);
            } finally {
                try {
                    actResult.getContent().close();
                } catch (IOException e) {
                    LOGGER.error("Error closing the result file.", e);
                }
            }
        }

        LOGGER.info("Done.");
    }
}
