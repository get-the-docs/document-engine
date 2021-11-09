package net.videki.templateutils.template.core.provider.resultstore.filesystem;

import net.videki.templateutils.template.core.documentstructure.ResultDocument;
import net.videki.templateutils.template.core.documentstructure.GenerationResult;
import net.videki.templateutils.template.core.documentstructure.StoredResultDocument;
import net.videki.templateutils.template.core.documentstructure.StoredGenerationResult;
import net.videki.templateutils.template.core.provider.resultstore.ResultStore;
import net.videki.templateutils.template.core.service.exception.ResultNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * File system based result store implementation. It stores the generation
 * results of single template and document structure processings.
 * 
 * @author Levente Ban
 */
public class FileSystemResultStore implements ResultStore {

    /**
     * Configuration property key in the system properties to define the basedir of
     * the result store.
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
     * 
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
     * 
     * @param result the result document descriptor.
     * @return the descriptor with the storage results (related transaction, file
     *         location, etc.).
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

        return new StoredResultDocument(result.getTransactionId(), resultFileName, actSuccessFlag);
    }

    /**
     * Entryp point to save a multi document result (the results of a document
     * structure generation).
     * 
     * @param generationResult the generation result container.
     * @return the storage result descriptor.
     */
    @Override
    public StoredGenerationResult save(final GenerationResult generationResult) {
        final List<StoredResultDocument> storedResults = new LinkedList<>();

        if (generationResult == null) {
            throw new TemplateProcessException("0d383b14-c3ef-430d-9007-3add27e086d8", "No result caught.");
        }

        for (final ResultDocument actResult : generationResult.getResults()) {
            if (actResult == null) {
                throw new TemplateProcessException("d82bfd82-e745-4c71-8835-98c64145307a", "No result caught.");
            }
            if (actResult.getContent() != null) {
                storedResults.add(save(actResult));
            } else {
                LOGGER.warn(
                        "Result document or part not generated - possible due errors. "
                                + "TransactionId: {}, fileName: {}",
                        actResult.getTransactionId(), actResult.getFileName());
            }
        }

        LOGGER.info("Done.");

        return new StoredGenerationResult(storedResults);
    }

    /**
     * Returns a given result document and/or its binary if requested. For document
     * structures query the generation result for the transaction id, then request
     * its result documents.
     * 
     * @param transactionId  the requested transaction, if found.
     * @throws TemplateServiceException thrown in case of query error.
     * @return the template document.
     */
    @Override
    public Optional<StoredGenerationResult> getGenerationResultByTransactionId(final String transactionId)
            throws TemplateServiceException {
        try {

            final Path basePath = Paths.get(this.baseDir + File.separator + transactionId).toAbsolutePath();
            final List<StoredResultDocument> items = Files.list(basePath)
                .filter(file -> !Files.isDirectory(file))
                .map(Path::getFileName)
                .map(Path::toString)
                .map(t -> new StoredResultDocument(t, true))
                .collect(Collectors.toList());

            final StoredGenerationResult result = new StoredGenerationResult(items);
            result.setTransactionId(transactionId);

            if (!items.isEmpty()) {
                return Optional.of(result);
            } else {
                return Optional.empty();
            }

        } catch (final IOException e) {
            final  String msg = String.format("Error retrieving the template list - baseDir: [{}]", this.baseDir);
            LOGGER.error(msg, e);

            throw new TemplateServiceException("68b79868-c05c-4d14-8d94-1d8815625c8f", msg);
        }
    }

    /**
     * Returns a given result document and/or its binary if requested. For document
     * structures query
     * 
     * @param transactionId the requested transaction, if found.
     * @throws TemplateServiceException thrown in case of query error.
     * @return the template document.
     */
    @Override
    public Optional<StoredResultDocument> getResultDocumentByTransactionId(final String transactionId, final String resultDocument,
            final boolean withBinary) throws TemplateServiceException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getResultDocumentByTransactionId - {}/{}. binary: {}", transactionId, resultDocument,
                    withBinary);
        }

        StoredResultDocument result;
        try (final InputStream is = getDocumentBinary(transactionId, resultDocument)) {

            if (is != null) {
                if (withBinary) {
                    result = new StoredResultDocument(resultDocument, true, is.readAllBytes());
                } else {
                    result = new StoredResultDocument(resultDocument, true, null);
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("getResultDocumentByTransactionId - result document not found: {}/{}", transactionId,
                            resultDocument);
                }
                throw new ResultNotFoundException("a66e1963-de92-4e26-ac76-e5c82d4c4cbd",
                        String.format("Result document not found: {]/{}", transactionId, resultDocument));
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("getResultDocumentByTransactionId end - {}/{}. result: {}", transactionId, resultDocument,
                        result);
            }
            return Optional.of(result);

        } catch (final ResultNotFoundException e) {
            return Optional.empty();
        } catch (final IOException e) {
            throw new TemplateServiceException("cb4da157-c092-4851-bbfb-48d24a95005c",
                    String.format("Error reading the document - transactionId: {}, resultDocument: {}.", transactionId,
                            resultDocument));
        }

    }

    /**
     * Returns a result document for a given transaction and name.
     * 
     * @param transactionId  the transaction id.
     * @param resultDocument the document name.
     * @return the document as stream if found.
     */
    public InputStream getDocumentBinary(final String transactionId, final String resultDocument) {
        InputStream result;

        final String pathToFile = this.baseDir + File.separator + transactionId + File.separator + resultDocument;
        try {
            result = Paths.get(pathToFile).toUri().toURL().openStream();
        } catch (final IOException e) {
            result = null;
        }

        if (result == null) {
            LOGGER.error("Result document not found. File: {}. ", pathToFile);
        } else {
            LOGGER.debug("Result document found. File: {}. ", pathToFile);
        }

        return result;

    }

}
