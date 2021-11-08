package net.videki.templateutils.template.core.provider.resultstore.filesystem;

import net.videki.templateutils.template.core.documentstructure.ResultDocument;
import net.videki.templateutils.template.core.documentstructure.GenerationResult;
import net.videki.templateutils.template.core.documentstructure.StoredResultDocument;
import net.videki.templateutils.template.core.documentstructure.StoredGenerationResult;
import net.videki.templateutils.template.core.provider.resultstore.ResultStore;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * File system based result store implementation.
 * It stores the generation results of single template and document structure processings. 
 * @author Levente Ban
 */
public class FileSystemResultStore implements ResultStore {

    /**
     * Configuration property key in the system properties to define the basedir of the result store.
     */
    private static final String RESULT_REPOSITORY_PROVIDER_BASEDIR = "repository.result.provider.basedir";

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemResultStore.class);

    /**
     * The actual basedir.
     */
    private String baseDir;

    /**
     * Initializer entry point to bootstrap the repository.
     * @param props the system properties (see template-utils.properties)
     */
    @Override
    public void init(final Properties props) throws TemplateServiceConfigurationException {
        if (props == null) {
            throw new TemplateServiceConfigurationException("f962f1ac-c29d-482e-a402-4815068d4de5",
                    "Null or invalid template properties caught.");
        }

        this.baseDir = (String) props.get(RESULT_REPOSITORY_PROVIDER_BASEDIR);
    }

    /**
     * Entry point to save a single result document in the repository.
     * @param result the result document descriptor.
     * @return the descriptor with the storage results (related transaction, file location, etc.).
     */
    @Override
    public StoredResultDocument save(final ResultDocument result) {
        if (result == null || result.getContent() == null) {
            throw new TemplateProcessException("cb05a816-dea2-4498-823a-33fb4ece1565", "No result caught.");
        }

        String transactionDir = result.getTransactionId();
        if (transactionDir == null) {
            transactionDir = ".";
        }
        final String resultDir = this.baseDir + File.separator + transactionDir;

        synchronized (this) {
            final File subdir = new File(resultDir);
            if (!subdir.exists()) {
                if (!subdir.mkdirs()) {
                    throw new IllegalArgumentException("Cannot create directory: " + resultDir);
                }
            }
        }

        final String resultFileName = resultDir + File.separator + result.getFileName();
        boolean actSuccessFlag = false;

        LOGGER.info("Result file: {}.", resultFileName);

        try (FileOutputStream o = new FileOutputStream(resultFileName, false)) {

            o.write(((ByteArrayOutputStream) result.getContent()).toByteArray());
            o.flush();

            actSuccessFlag = true;
        } catch (IOException e) {
            LOGGER.error("Error saving the result file.", e);
        } finally {
            try {
                result.getContent().close();
            } catch (IOException e) {
                LOGGER.error("Error closing the result file.", e);
            }
        }

        return new StoredResultDocument(resultFileName, actSuccessFlag);
    }

    /**
     * Entryp point to save a multi document result (the results of a document structure generation).
     * @param generationResult the generation result container.
     * @return the storage result descriptor.
     */
    @Override
    public StoredGenerationResult save(final GenerationResult generationResult) {
        final List<StoredResultDocument> storedResults = new LinkedList<>();

        if (generationResult == null) {
            throw new TemplateProcessException("0d383b14-c3ef-430d-9007-3add27e086d8",
                    "No result caught.");
        }

        for (final ResultDocument actResult : generationResult.getResults()) {
            if (actResult == null) {
                throw new TemplateProcessException("d82bfd82-e745-4c71-8835-98c64145307a",
                        "No result caught.");
            }
            if (actResult.getContent() != null) {
                storedResults.add(save(actResult));
            } else {
                LOGGER.warn("Result document or part not generated - possible due errors. " +
                        "TransactionId: {}, fileName: {}", actResult.getTransactionId(), actResult.getFileName());
            }
        }

        LOGGER.info("Done.");

        return new StoredGenerationResult(storedResults);
    }
}
