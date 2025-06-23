package org.getthedocs.documentengine.core.service;

/*-
 * #%L
 * docs-core
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

import org.getthedocs.documentengine.core.configuration.TemplateServiceConfiguration;
import org.getthedocs.documentengine.core.context.JsonTemplateContext;
import org.getthedocs.documentengine.core.context.TemplateContext;
import org.getthedocs.documentengine.core.documentstructure.*;
import org.getthedocs.documentengine.core.documentstructure.*;
import org.getthedocs.documentengine.core.documentstructure.descriptors.TemplateElement;
import org.getthedocs.documentengine.core.documentstructure.descriptors.TemplateElementId;
import org.getthedocs.documentengine.core.processor.ConverterRegistry;
import org.getthedocs.documentengine.core.processor.TemplateProcessorRegistry;
import org.getthedocs.documentengine.core.processor.converter.pdf.docx4j.DocxToPdfConverter;
import org.getthedocs.documentengine.core.processor.input.InputTemplateProcessor;
import org.getthedocs.documentengine.core.processor.input.PlaceholderEvalException;
import org.getthedocs.documentengine.core.service.exception.TemplateProcessException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.getthedocs.documentengine.core.util.FileSystemHelper;
import org.docx4j.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default template service implementation.
 * 
 * @author Levente Ban
 */
public class TemplateServiceImpl implements TemplateService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);

    /**
     * The service log category to collect underlying logs into this category.
     */
    private static Logger LOGGER_VALUE;

    /**
     * The actual service configuration.
     */
    private final TemplateServiceConfiguration configuration;

    /**
     * Deafult constructor.
     */
    TemplateServiceImpl() {
        this.configuration = TemplateServiceConfiguration.getInstance();

        String valueLogCategory = this.configuration.getDocStructureLogCategory();
        if (valueLogCategory == null) {
            LOGGER_VALUE = LoggerFactory.getLogger(TemplateService.class);
        } else {
            LOGGER_VALUE = LoggerFactory.getLogger(valueLogCategory);
        }
    }

    /**
     * Returns the context object for a given DTO.
     * 
     * @param <T> the model class.
     * @param dto the model object.
     * @return the template context.
     */
    @SuppressWarnings("rawtypes")
    private <T> TemplateContext getContextObject(final T dto) {
        TemplateContext context;

        if (dto instanceof String) {
            LOGGER.debug("JSON context caught.");
            context = new JsonTemplateContext((String) dto);
//            context.addValueObject(new JsonValueObject((String) dto));
        } else if (dto instanceof Map) {
            LOGGER.debug("Map context caught.");
            context = new TemplateContext();
            for (final Object actKey : ((Map) dto).keySet()) {
                context.getCtx().put((String) actKey, ((Map) dto).get(actKey));
            }
        } else if (dto instanceof JsonTemplateContext) {
            LOGGER.debug("JsonTemplateContext context caught.");
            context = (JsonTemplateContext) dto;
        } else if (dto instanceof TemplateContext) {
            LOGGER.debug("TemplateContext context caught.");
            context = (TemplateContext) dto;
        } else {
            LOGGER.debug("POJO context caught.");
            context = new TemplateContext();
            context.addValueObject(dto);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Context object: {}", context.toJson());
        }
        return context;
    }

    /**
     * Entry point to fill a single template with a given model.
     *
     * @param templateName  the template name (id) in the template repository.
     * @param <T>           the model class.
     * @param dto           the model object.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     * @return the filled document if the fill was successful.
     */
    @Override
    public <T> ResultDocument fill(final String templateName, final T dto)
            throws TemplateServiceException {

        return this.fill(null, templateName, dto);

    }

    /**
     * Entry point to fill a single template with a given model.
     *
     * @param transactionId the transaction id.
     * @param templateName  the template name (id) in the template repository.
     * @param <T>           the model class.
     * @param dto           the model object.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     * @return the filled document if the fill was successful.
     */
    @Override
    public <T> ResultDocument fill(final String transactionId, final String templateName, final T dto)
            throws TemplateServiceException {

        if (Strings.isNullOrEmpty(templateName) || dto == null) {
            throw new TemplateServiceConfigurationException("070f463e-743f-4cb2-a651-bd11e844728d",
                    String.format("%s - templateFileName: %s, dto: %s", TemplateServiceConfigurationException.MSG_INVALID_PARAMETERS, templateName, dto));
        }

        TemplateContext context = getContextObject(dto);

        final InputFormat format = InputFormat.getInputFormatForFileName(templateName);
        InputTemplateProcessor processor = TemplateProcessorRegistry.getInputTemplateProcessor(format);
        if (processor == null) {
            final String msg = "Could not determine the input processor";
            LOGGER.error(msg);
            throw new TemplateServiceConfigurationException("9476f20c-0d78-4c8a-87a5-277101256924", msg);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Found template processor for: templateFileName: %s, processor: %s",
                        templateName, processor.getClass()));
            }
        }

        try {
            final ResultDocument result = new ResultDocument(templateName, processor.fill(templateName, context));
            result.setTransactionId(transactionId);

            return result;
        } catch (final PlaceholderEvalException e) {
            final ResultDocument result = new ResultDocument(templateName, null);
            result.setTransactionId(transactionId);

            return result;
        }

    }


    /**
     * Entry point to fill a single template with a given model and convert it to
     * the given format.
     * 
     * @param transactionId the transaction id.
     * @param templateName  the template name (id) in the template repository.
     * @param <T>           the model class.
     * @param dto           the model object.
     * @param outputFormat  the desired output format.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retriev or fill.
     * @return the filled document if the fill was successful.
     */
    @Override
    public <T> ResultDocument fill(final String transactionId, final String templateName, final T dto,
            final OutputFormat outputFormat) throws TemplateServiceException {

        if (Strings.isNullOrEmpty(templateName) || dto == null || outputFormat == null) {
            throw new TemplateServiceConfigurationException("c936e550-8b0e-4577-bffa-7f36b211d981",
                    String.format("%s - templateFileName: %s, dto: %s, outputFormat: %s", TemplateServiceConfigurationException.MSG_INVALID_PARAMETERS,
                            templateName, dto, outputFormat));
        }

        OutputStream result = null;

        final Optional<ResultDocument> filledDoc = Optional.ofNullable(fill(templateName, dto));
        final InputFormat inputFormat = InputFormat.getInputFormatForFileName(templateName);

        if (filledDoc.isPresent()) {
            switch (outputFormat) {
            case DOCX:
                result = filledDoc.get().getContent();
                break;
            case PDF:
                final InputStream filledInputStream = FileSystemHelper.getInputStream(filledDoc.get().getContent());
                result = ConverterRegistry.getConverter(inputFormat, OutputFormat.PDF).convert(filledInputStream);
                break;
            default:
                LOGGER.warn("Unhandled output format {}. Has been a new one defined?", outputFormat);
            }
        }

        try {
            return new ResultDocument(transactionId, getOutputFileName(templateName, outputFormat), result);
        } catch (final PlaceholderEvalException e) {
            return new ResultDocument(transactionId, templateName, null);
        }
    }

    /**
     * Entry point to fill a document structure with a given set of models.
     * 
     * @param transactionId     the transaction id.
     * @param documentStructure the document structure descriptor.
     * @param values            the model objects.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     * @return the filled document if the fill was successful.
     */
    @Override
    public GenerationResult fill(final String transactionId, final DocumentStructure documentStructure,
                                 final ValueSet values) throws TemplateServiceException {
        if (documentStructure == null || values == null) {
            throw new TemplateServiceConfigurationException("bdaa9376-28b4-4718-9859-2ef5d88ab3b0", String.format(
                    "%s - documentStructure: %s, values: %s", TemplateServiceConfigurationException.MSG_INVALID_PARAMETERS, documentStructure, values));
        }

        final List<ResultDocument> results = new LinkedList<>();
        final GenerationResult result = new GenerationResult(results);
        result.setGenerationStartTime(Instant.now());
        result.setTransactionId(transactionId);
        result.setValueSetTransactionId(values.getTransactionId());

        final Optional<TemplateContext> globalContext = values.getGlobalContext();

        LOGGER.debug("Start processing document structure: element size: {}", documentStructure.getElements().size());

        if (LOGGER_VALUE.isDebugEnabled()) {
            LOGGER_VALUE.debug(String.format("Transaction id: [%s] - %s %s", values.getTransactionId(),
                    documentStructure, values));
        }

        for (final TemplateElement actTemplate : documentStructure.getElements()) {
            LOGGER.debug("Processing template: friendly name: {}, id: {}.", actTemplate.getTemplateName(),
                    actTemplate.getTemplateElementId());

            final TemplateElementId actTemplateElementId = actTemplate.getTemplateElementId();
            final Optional<TemplateContext> actContext = values.getContext(actTemplateElementId);

            LOGGER.debug("Getting context for template: friendly name: {}, context: {}.",
                    actTemplate.getTemplateElementId(), actContext);

            final ResultDocument actFilledDocument;

            final String templateFileName = actTemplate.getTemplateName(values.getLocale());
            if (actContext.isPresent() || globalContext.isPresent()) {
                actFilledDocument = new ResultDocument(templateFileName,
                        this.fill(templateFileName, getLocalTemplateContext(globalContext, actContext)).getContent());
            } else {
                actFilledDocument = new ResultDocument(templateFileName,
                        TemplateProcessorRegistry.getNoopProcessor().fill(templateFileName, null));
            }

            final ResultDocument actContent = convertToOutputFormat(documentStructure, actTemplate, actFilledDocument);

            if (actContent.getContent() != null) {
                actContent.setTransactionId(values.getTransactionId());

                results.addAll(
                        Stream.generate(() -> actContent).limit(actTemplate.getCount()).collect(Collectors.toList()));

            }

            result.setGenerationEndTime(Instant.now());

            LOGGER.debug("Processing template - end: friendly name: {} template name: {}, id: {}.",
                    actTemplate.getTemplateElementId(), actTemplate.getTemplateName(),
                    actTemplate.getTemplateElementId());
        }

        LOGGER.debug(
                String.format("End processing document structure. " + "Result document list size: %s", results.size()));

        return result;
    }

    /**
     * Return the output filename for a given format.
     * 
     * @param templateFileName the input template name.
     * @param format           the output format.
     * @return the result filename.
     */
    private String getOutputFileName(final String templateFileName, final OutputFormat format) {

        try {
            int fileExtPos = templateFileName.lastIndexOf(FileSystemHelper.FILENAME_COLON);
            if (fileExtPos > 0) {
                return templateFileName.substring(0, fileExtPos) + FileSystemHelper.FILENAME_COLON
                        + format.name().toLowerCase();

            } else {
                throw new IllegalArgumentException();
            }
        } catch (final IllegalArgumentException e) {
            final String msg = String.format("Unhandled template file format. Filename: %s", templateFileName);
            throw new TemplateProcessException("9fcf0c32-4096-4647-9f46-bbbe4564cdd7", msg);
        }

    }

    /**
     * Entry point to fill a document structure with a given set of models.
     * 
     * @param transactionId         the transaction id.
     * @param documentStructureFile the document structure name (id) in the document
     *                              structure repository.
     * @param values                the model objects.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     * @return the filled document if the fill was successful.
     */
    @Override
    public GenerationResult fillDocumentStructureByName(String transactionId, final String documentStructureFile,
            final ValueSet values) throws TemplateServiceException {
        final DocumentStructure ds = TemplateServiceConfiguration.getInstance().getDocumentStructureRepository()
                .getDocumentStructure(documentStructureFile);
        return this.fill(transactionId, ds, values);

    }

    /**
     * Processes a single template document by filling it with the given model
     * object and saves it in the configured result store.
     * 
     * @param templateName the template document name in the template repository.
     * @param <T>          the model class.
     * @param dto          the model object.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     */
    @Override
    public <T> StoredResultDocument fillAndSave(final String templateName, final T dto)
            throws TemplateServiceException {
        final ResultDocument result = this.fill(templateName, dto);

        if (result.getContent() != null) {
            return TemplateServiceConfiguration.getInstance().getResultStore().save(result);
        } else {
            final StoredResultDocument resultDocument = new StoredResultDocument(result.getFileName(), false);
            resultDocument.setTransactionId(result.getTransactionId());

            return resultDocument;
        }
    }

    /**
     * Processes a single template document by filling it with the given model
     * object, converts it to the desired output format and saves it in the
     * configured result store.
     * 
     * @param templateName the template document name in the template repository.
     * @param <T>          the model class.
     * @param dto          the model object.
     * @param format       the desired output format.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     */
    @Override
    public <T> StoredResultDocument fillAndSave(final String templateName, final T dto, final OutputFormat format)
            throws TemplateServiceException {
        final ResultDocument result = this.fill(null, templateName, dto, format);

        return TemplateServiceConfiguration.getInstance().getResultStore().save(result);
    }

    /**
     * Processes a single template document by filling it with the given model
     * object, converts it to the desired output format and saves it in the
     * configured result store.
     *
     * @param transactionId The tranasction id, if defined
     * @param templateName  the template document name in the template repository.
     * @param <T>           the model class.
     * @param dto           the model object.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     */
    @Override
    public <T> StoredResultDocument fillAndSave(final String transactionId, final String templateName, final T dto)
            throws TemplateServiceException {
        final ResultDocument result = this.fill(transactionId, templateName, dto);

        return TemplateServiceConfiguration.getInstance().getResultStore().save(result);
    }

    /**
     * Processes a single template document by filling it with the given model
     * object, converts it to the desired output format and saves it in the
     * configured result store.
     * 
     * @param transactionId The tranasction id, if defined
     * @param templateName  the template document name in the template repository.
     * @param <T>           the model class.
     * @param dto           the model object.
     * @param format        the desired output format.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     */
    @Override
    public <T> StoredResultDocument fillAndSave(final String transactionId, final String templateName, final T dto,
            final OutputFormat format) throws TemplateServiceException {
        final ResultDocument result = this.fill(transactionId, templateName, dto, format);

        return TemplateServiceConfiguration.getInstance().getResultStore().save(result);
    }

    /**
     * Processes a single template document by filling it with the given model
     * object, converts it to the desired output format and saves it in the
     * configured result store.
     * 
     * @param documentStructure the document structure descriptor.
     * @param values            the model objects.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     */
    @Override
    public StoredGenerationResult fillAndSave(final DocumentStructure documentStructure, final ValueSet values)
            throws TemplateServiceException {
        final GenerationResult generationResult = this.fill(null, documentStructure, values);

        final List<StoredResultDocument> ResultDocuments = generationResult.getResults().parallelStream()
                .map(t -> TemplateServiceConfiguration.getInstance().getResultStore().save(t))
                .collect(Collectors.toList());

        return new StoredGenerationResult(generationResult.getTransactionId(),
                ResultDocuments);
    }

    /**
     * Processes a single template document by filling it with the given model
     * object, converts it to the desired output format and saves it in the
     * configured result store.
     * 
     * @param transactionId     the transaction id.
     * @param documentStructure the document structure descriptor.
     * @param values            the model objects.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     */
    @Override
    public StoredGenerationResult fillAndSave(final String transactionId, final DocumentStructure documentStructure,
            final ValueSet values) throws TemplateServiceException {
        final GenerationResult generationResult = this.fill(transactionId, documentStructure, values);

        final List<StoredResultDocument> ResultDocuments = generationResult.getResults().parallelStream()
                .map(t -> TemplateServiceConfiguration.getInstance().getResultStore().save(t))
                .collect(Collectors.toList());

        return new StoredGenerationResult(generationResult.getTransactionId(),
                ResultDocuments);
    }

    /**
     * Processes a single template document by filling it with the given model
     * object, converts it to the desired output format and saves it in the
     * configured result store.
     * 
     * @param documentStructureFile the document structure id in the document
     *                              structure repository.
     * @param values                the model objects.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     */
    @Override
    public StoredGenerationResult fillAndSaveDocumentStructureByName(final String documentStructureFile,
            final ValueSet values) throws TemplateServiceException {
        final DocumentStructure ds = TemplateServiceConfiguration.getInstance().getDocumentStructureRepository()
                .getDocumentStructure(documentStructureFile);
        return this.fillAndSave(ds, values);

    }

    /**
     * Processes a single template document by filling it with the given model
     * object, converts it to the desired output format and saves it in the
     * configured result store.
     * 
     * @param transactionId         the transaction id.
     * @param documentStructureFile the document structure id in the document
     *                              structure repository.
     * @param values                the model objects.
     * @throws TemplateServiceException thrown on processing related errors during
     *                                  template retrieval or fill.
     */
    @Override
    public StoredGenerationResult fillAndSaveDocumentStructureByName(final String transactionId,
            final String documentStructureFile, final ValueSet values) throws TemplateServiceException {
        final DocumentStructure ds = TemplateServiceConfiguration.getInstance().getDocumentStructureRepository()
                .getDocumentStructure(documentStructureFile);
        return this.fillAndSave(transactionId, ds, values);

    }

    /**
     * Converts a result document generated for a document structure to the given
     * format.
     * 
     * @param documentStructure the document structure descriptor.
     * @param actTemplate       the actual template element.
     * @param actFilledDocument the actual processing result.
     * @return the converted document if the conversion is supported and was
     *         successful.
     * @throws TemplateProcessException thrown on processing related errors.
     */
    private ResultDocument convertToOutputFormat(final DocumentStructure documentStructure,
            final TemplateElement actTemplate, final ResultDocument actFilledDocument) throws TemplateProcessException {

        OutputStream actResult;
        String actResultFileName = null;
        if (actFilledDocument.getContent() != null
                && !documentStructure.getOutputFormat().isSameFormat(actTemplate.getFormat())) {
            switch (documentStructure.getOutputFormat()) {
            case UNCHANGED:
                actResult = actFilledDocument.getContent();
                actResultFileName = actFilledDocument.getFileName();
                break;
            case PDF:
                LOGGER.trace("Output: PDF");
                switch (actTemplate.getFormat()) {
                case DOCX:
                    actResult = new DocxToPdfConverter()
                            .convert(FileSystemHelper.getInputStream(actFilledDocument.getContent()));
                    actResultFileName = getOutputFileName(actFilledDocument.getFileName(), OutputFormat.PDF);
                    break;
                case XLSX:
                    /*
                     * final String msg = String.format("Invalid document structure. " +
                     * "The current template cannot be converted to the desired format: " +
                     * "template: %s/%s, source format: %s - target format: %s",
                     * actTemplate.getTemplateElementId(), actTemplate.getTemplateName(),
                     * actTemplate.getFormat(), documentStructure.getOutputFormat()); throw new
                     * TemplateProcessException("89688dfe-7b60-453d-ab2a-90f8e9605cdf", msg);
                     * 
                     */

                    // Keeping the format if not convertible
                    actResult = actFilledDocument.getContent();
                    actResultFileName = actFilledDocument.getFileName();

                    break;
                default:
                    actResult = actFilledDocument.getContent();
                    actResultFileName = actFilledDocument.getFileName();
                    LOGGER.trace("  No conversion needed for the actual template.");
                }
                break;
            case DOCX:
                actResult = actFilledDocument.getContent();
                actResultFileName = actFilledDocument.getFileName();
                LOGGER.trace("  No conversion needed for the actual template.");
                break;
            default:
                final String msg = String.format("Unhandled output format: [%s].", documentStructure.getOutputFormat());
                LOGGER.error(msg);
                throw new TemplateProcessException("a520da67-15b9-4fac-8f2f-15b68c13815b", msg);
            }
        } else {
            actResult = actFilledDocument.getContent();
            actResultFileName = actFilledDocument.getFileName();
        }

        return new ResultDocument(actResultFileName, actResult);
    }

    /**
     * Returns local template context.
     * 
     * @param globalContext the actual global context.
     * @param localContext  the current context for evaluating the actual
     *                      placeholder.
     * @return the template context with the context accessible at the current
     *         location (object in the local context plus contents of the global
     *         context).
     */
    private TemplateContext getLocalTemplateContext(final Optional<TemplateContext> globalContext,
            final Optional<TemplateContext> localContext) {
        final TemplateContext result = new TemplateContext();
        globalContext.ifPresent(templateContext -> result.getCtx().putAll(templateContext.getCtx()));
        localContext.ifPresent(templateContext -> result.getCtx().putAll(templateContext.getCtx()));

        return result;
    }

    /**
     * Returns an input stream for the given output stream.
     * @param out the output stream.
     * @return the input stream.
     */
    protected static InputStream getInputStream(final OutputStream out) {
        return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());

    }

}
