package net.videki.templateutils.template.core.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import com.google.common.base.Strings;
import net.videki.templateutils.template.core.configuration.util.FileSystemHelper;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
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

    TemplateServiceImpl() {

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
    public <T> OutputStream fill(final String templateName, final T dto) throws TemplateServiceException {

        if (Strings.isNullOrEmpty(templateName) || dto == null ) {
            throw new TemplateServiceConfigurationException("070f463e-743f-4cb2-a651-bd11e844728d",
                    String.format("%s - templateFileName: %s, dto: %s",
                            MSG_INVALID_PARAMETERS, templateName, dto) );
        }

        TemplateContext context = getContextObject(dto);

        final InputFormat format = InputFormat.getInputFormatForFileName(templateName);
        InputTemplateProcessor processor = TemplateProcessorRegistry.getInputTemplateProcessor(format);

        return processor.fill(templateName, context);

    }

    @Override
    public <T> OutputStream fill(final String templateName, final T dto, final OutputFormat outputFormat)
            throws TemplateServiceException {

        if (Strings.isNullOrEmpty(templateName) || dto == null || outputFormat == null ) {
            throw new TemplateServiceConfigurationException("c936e550-8b0e-4577-bffa-7f36b211d981",
                    String.format("%s - templateFileName: %s, dto: %s, outputFormat: %s",
                            MSG_INVALID_PARAMETERS, templateName, dto, outputFormat) );
        }

        OutputStream result = null;

        final OutputStream filledDoc = fill(templateName, dto);

        switch (outputFormat) {
            case DOCX:
                result = filledDoc;
                break;
            case PDF:
                final InputStream filledInputStream = FileSystemHelper.getInputStream(filledDoc);
                result = new DocxToPdfConverter().convert(filledInputStream);
                break;
            default:
                LOGGER.warn("Unhandled output format {}. Has been a new one defined?", outputFormat);
        }

        return result;
    }

    @Override
    public List<OutputStream> fill(final DocumentStructure documentStructure, final ValueSet values)
            throws TemplateServiceException {

        if (documentStructure == null || values == null ) {
            throw new TemplateServiceConfigurationException("bdaa9376-28b4-4718-9859-2ef5d88ab3b0",
                    String.format("%s - documentStructure: %s, values: %s",
                            MSG_INVALID_PARAMETERS, documentStructure, values) );
        }

        List<OutputStream> results = new LinkedList<>();

        final Optional<TemplateContext> globalContext = values.getGlobalContext();

        LOGGER.debug("Start processing document structure: element size: {}",
                documentStructure.getElements().size());

        for (final TemplateElement actTemplate : documentStructure.getElements()) {

            LOGGER.debug("Processing template: friendly name: {} template name: {}, id: {}.",
                    actTemplate.getFriendlyName(), actTemplate.getTemplateName(), actTemplate.getTemplateElementId());

            final TemplateElementId actTemplateElementId = actTemplate.getTemplateElementId();
            final Optional<TemplateContext> actContext = values.getContext(actTemplateElementId);

            final OutputStream actFilledDocument =
                    this.fill(actTemplate.getTemplateName(),
                            getLocalTemplateContext(globalContext, actContext));

            final OutputStream actResult = convertToOutputFormat(documentStructure, actTemplate, actFilledDocument);

            if (actResult != null) {
               for (int i = 0, count = actTemplate.getCount(); i < count; i++) {
                    results.add(actResult);
                }
            }

            LOGGER.debug(String.format("End processing document structure. " +
                            "Result document list size: %s", results.size()));

        }

        return results;
    }

    private OutputStream convertToOutputFormat(DocumentStructure documentStructure, TemplateElement actTemplate,
                                               OutputStream actFilledDocument)
            throws TemplateProcessException {
        OutputStream actResult;
        if (!documentStructure.getOutputFormat().isSameFormat(actTemplate.getFormat())) {
            switch (documentStructure.getOutputFormat()) {
                case UNCHANGED:
                    actResult = actFilledDocument;
                    break;
                case PDF:
                    LOGGER.trace("Output: PDF");
                    switch (actTemplate.getFormat()) {
                        case DOCX:
                            actResult = new DocxToPdfConverter()
                                    .convert(FileSystemHelper.getInputStream(actFilledDocument));
                            break;
                        case XLSX:
                            final String msg = String.format("Invalid document structure. " +
                                    "The current template cannot be converted to the desired format: " +
                                            "template: %s/%s, source format: %s - target format: %s",
                                    actTemplate.getFriendlyName(),
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

    private TemplateContext getLocalTemplateContext(Optional<TemplateContext> globalContext,
                                                    Optional<TemplateContext> localContext) {
        final TemplateContext result = new TemplateContext();
        globalContext.ifPresent(templateContext -> result.getCtx().putAll(templateContext.getCtx()));
        localContext.ifPresent(templateContext -> result.getCtx().putAll(templateContext.getCtx()));

        return result;
    }


}
