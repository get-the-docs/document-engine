package net.videki.templateutils.template.core.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Strings;
import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.documentstructure.*;
import net.videki.templateutils.template.core.util.FileSystemHelper;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;
import net.videki.templateutils.template.core.processor.TemplateProcessorRegistry;
import net.videki.templateutils.template.core.processor.converter.pdf.DocxToPdfConverter;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;

import static net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException.MSG_INVALID_PARAMETERS;

public class TemplateServiceImpl implements TemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);
    private static Logger LOGGER_VALUE;
    private final TemplateServiceConfiguration configuration;

    TemplateServiceImpl() {
        this.configuration = TemplateServiceConfiguration.getInstance();

        String valueLogCategory = this.configuration.getDocStructureLogCategory();
        if (valueLogCategory == null) {
            LOGGER_VALUE = LoggerFactory.getLogger(TemplateService.class);
        } else {
            LOGGER_VALUE = LoggerFactory.getLogger(valueLogCategory);
        }
    }

    private <T> TemplateContext getContextObject(final T dto) {
        TemplateContext context;

        if (dto instanceof Map) {
            LOGGER.debug("Map context caught.");
            context = new TemplateContext();
            for (Object actKey : ((Map) dto).keySet()) {
                context.getCtx().put((String) actKey, ((Map) dto).get(actKey));
            }
        } else if (dto instanceof TemplateContext) {
            LOGGER.debug("TemplateContext context caught.");
            context = (TemplateContext) dto;
        } else {
            LOGGER.debug("POJO context caught.");
            context = new TemplateContext();
            context.getCtx().put(TemplateContext.CONTEXT_ROOT_KEY_MODEL, dto);
        }

        LOGGER.debug("Context object: {}", context.toJson());

        return context;
    }

    @Override
    public <T> ResultDocument fill(final String templateName, final T dto) throws TemplateServiceException {

        if (Strings.isNullOrEmpty(templateName) || dto == null ) {
            throw new TemplateServiceConfigurationException("070f463e-743f-4cb2-a651-bd11e844728d",
                    String.format("%s - templateFileName: %s, dto: %s",
                            MSG_INVALID_PARAMETERS, templateName, dto) );
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
        return new ResultDocument(templateName, processor.fill(templateName, context));

    }

    @Override
    public <T> ResultDocument fill(final String templateName, final T dto, final OutputFormat outputFormat)
            throws TemplateServiceException {

        if (Strings.isNullOrEmpty(templateName) || dto == null || outputFormat == null ) {
            throw new TemplateServiceConfigurationException("c936e550-8b0e-4577-bffa-7f36b211d981",
                    String.format("%s - templateFileName: %s, dto: %s, outputFormat: %s",
                            MSG_INVALID_PARAMETERS, templateName, dto, outputFormat) );
        }

        OutputStream result = null;

        final Optional<ResultDocument> filledDoc = Optional.ofNullable(fill(templateName, dto));

        if (filledDoc.isPresent()) {
            switch (outputFormat) {
                case DOCX:
                    result = filledDoc.get().getContent();
                    break;
                case PDF:
                    final InputStream filledInputStream = FileSystemHelper.getInputStream(filledDoc.get().getContent());
                    result = new DocxToPdfConverter().convert(filledInputStream);
                    break;
                default:
                    LOGGER.warn("Unhandled output format {}. Has been a new one defined?", outputFormat);
            }
        }
        return new ResultDocument(templateName, result);
    }

    @Override
    public GenerationResult fill(final DocumentStructure documentStructure, final ValueSet values)
            throws TemplateServiceException {
        if (documentStructure == null || values == null ) {
            throw new TemplateServiceConfigurationException("bdaa9376-28b4-4718-9859-2ef5d88ab3b0",
                    String.format("%s - documentStructure: %s, values: %s",
                            MSG_INVALID_PARAMETERS, documentStructure, values) );
        }

        final List<ResultDocument> results = new LinkedList<>();
        final GenerationResult result = new GenerationResult(results);
        result.setGenerationStartTime(Instant.now());
        result.setTransactionId(values.getTransactionId());

        final Optional<TemplateContext> globalContext = values.getGlobalContext();

        LOGGER.debug("Start processing document structure: element size: {}",
                documentStructure.getElements().size());

        if (LOGGER_VALUE.isDebugEnabled()) {
            LOGGER_VALUE.debug(String.format("Transaction id: [%s] - %s %s",
                    values.getTransactionId(), documentStructure, values));
        }

        for (final TemplateElement actTemplate : documentStructure.getElements()) {
            LOGGER.debug("Processing template: friendly name: {}, id: {}.",
                    actTemplate.getTemplateName(), actTemplate.getTemplateElementId());

            final TemplateElementId actTemplateElementId = actTemplate.getTemplateElementId();
            final Optional<TemplateContext> actContext = values.getContext(actTemplateElementId);

            LOGGER.debug("Getting context for template: friendly name: {}, context: {}.",
                    actTemplate.getTemplateElementId(), actContext);

            final Optional<OutputStream> actFilledDocument;

            if (actContext.isPresent()) {
                actFilledDocument = Optional.ofNullable(
                        this.fill(actTemplate.getTemplateName(values.getLocale()),
                                getLocalTemplateContext(globalContext, actContext)).getContent());
            } else {
                actFilledDocument =
                        Optional.ofNullable(TemplateProcessorRegistry.getNoopProcessor()
                                .fill(actTemplate.getTemplateName(values.getLocale()), null));
            }

            final Optional<OutputStream> actContent =
                    convertToOutputFormat(documentStructure, actTemplate, actFilledDocument);

            if (actContent.isPresent()) {
                final ResultDocument arContent =
                        new ResultDocument(
                        FileSystemHelper.getFileName(actTemplate.getTemplateName(values.getLocale())),
                        actContent.get());
                arContent.setTransactionId(values.getTransactionId());

                results.addAll(
                        Stream.generate(() -> arContent).limit(actTemplate.getCount()).collect(Collectors.toList()));

            }

            result.setGenerationEndTime(Instant.now());

            LOGGER.debug("Processing template - end: friendly name: {} template name: {}, id: {}.",
                    actTemplate.getTemplateElementId(), actTemplate.getTemplateName(),
                    actTemplate.getTemplateElementId());
        }

        LOGGER.debug(String.format("End processing document structure. " +
                "Result document list size: %s", results.size()));

        return result;
    }

    @Override
    public <T> StoredResultDocument fillAndSave(final String templateName, final T dto) throws TemplateServiceException {
        final ResultDocument result = this.fill(templateName, dto);

        return TemplateServiceConfiguration.getInstance().getResultStore().save(result);
    }

    @Override
    public <T> StoredResultDocument fillAndSave(final String templateName, final T dto, final OutputFormat format) throws TemplateServiceException {
        final ResultDocument result = this.fill(templateName, dto, format);

        return TemplateServiceConfiguration.getInstance().getResultStore().save(result);
    }

    @Override
    public StoredGenerationResult fillAndSave(final DocumentStructure documentStructure, final ValueSet values) throws TemplateServiceException {
        final GenerationResult generationResult = this.fill(documentStructure, values);

        final List<StoredResultDocument> ResultDocuments =
                generationResult.getResults().parallelStream()
                        .map(t -> TemplateServiceConfiguration.getInstance().getResultStore().save(t))
                        .collect(Collectors.toList());

        final StoredGenerationResult result =
                new StoredGenerationResult(generationResult.getTransactionId(), ResultDocuments);

        return result;
    }

    private Optional<OutputStream> convertToOutputFormat(final DocumentStructure documentStructure,
                                                         final TemplateElement actTemplate,
                                                         final Optional<OutputStream> actFilledDocument)
            throws TemplateProcessException {

        Optional<OutputStream> actResult;
        if (actFilledDocument.isPresent() &&
                !documentStructure.getOutputFormat().isSameFormat(actTemplate.getFormat())) {
            switch (documentStructure.getOutputFormat()) {
                case UNCHANGED:
                    actResult = actFilledDocument;
                    break;
                case PDF:
                    LOGGER.trace("Output: PDF");
                    switch (actTemplate.getFormat()) {
                        case DOCX:
                            actResult = Optional.ofNullable(new DocxToPdfConverter()
                                    .convert(FileSystemHelper.getInputStream(actFilledDocument.get())));
                            break;
                        case XLSX:
                            final String msg = String.format("Invalid document structure. " +
                                    "The current template cannot be converted to the desired format: " +
                                            "template: %s/%s, source format: %s - target format: %s",
                                    actTemplate.getTemplateElementId(),
                                    actTemplate.getTemplateName(),
                                    actTemplate.getFormat(),
                                    documentStructure.getOutputFormat());
                            throw new TemplateProcessException("89688dfe-7b60-453d-ab2a-90f8e9605cdf", msg);
                        default:
                            actResult = actFilledDocument;
                            LOGGER.trace("  No conversion needed for the actual template.");
                    }
                    break;
                case DOCX:
                    actResult = actFilledDocument;
                    LOGGER.trace("  No conversion needed for the actual template.");
                    break;
                default:
                    final String msg = String.format("Unhandled output format: [%s].",
                            documentStructure.getOutputFormat());
                    LOGGER.error(msg);
                    throw new TemplateProcessException("a520da67-15b9-4fac-8f2f-15b68c13815b", msg);
            }
        } else {
            actResult = actFilledDocument;
        } return actResult;
    }

    private TemplateContext getLocalTemplateContext(final Optional<TemplateContext> globalContext,
                                                    final Optional<TemplateContext> localContext) {
        final TemplateContext result = new TemplateContext();
        globalContext.ifPresent(templateContext -> result.getCtx().putAll(templateContext.getCtx()));
        localContext.ifPresent(templateContext -> result.getCtx().putAll(templateContext.getCtx()));

        return result;
    }

    protected static InputStream getInputStream(final OutputStream out) {
        return new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());

    }

}
