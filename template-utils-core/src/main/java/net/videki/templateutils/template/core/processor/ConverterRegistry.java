package net.videki.templateutils.template.core.processor;

import net.videki.templateutils.template.core.processor.converter.Converter;
import net.videki.templateutils.template.core.processor.converter.pdf.DocxToPdfConverter;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.OutputFormat;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

public class ConverterRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);

    private static Map<InputFormat, Map<OutputFormat, Converter>> converters = new EnumMap<>(InputFormat.class);

    static {
        init();
    }

    public static void setConverters(Map<InputFormat, Map<OutputFormat, Converter>> param) {
        converters.clear();
        converters.putAll(param);
    }

    protected static void init() {
        converters.clear();

        final Map<OutputFormat, Converter> docxConverters = new EnumMap<>(OutputFormat.class);
        docxConverters.put(OutputFormat.PDF, new DocxToPdfConverter());
        converters.put(InputFormat.DOCX, docxConverters);

    }

    public static Converter getConverter(final InputFormat inputFormat, final OutputFormat outputFormat) {
        Converter result = null;

        Map<OutputFormat, Converter> convertersForInputFormat = converters.get(inputFormat);

        if (convertersForInputFormat != null) {
            result = convertersForInputFormat.get(outputFormat);

        }

        if (convertersForInputFormat == null || result == null) {
            final String msg = String.format("No converter found for the source format to the given output type. " +
                    "Source: %s, Target: %s.", inputFormat, outputFormat);
            LOGGER.error(msg);
            throw new TemplateProcessException("9a27e2a0-6260-40d7-ac10-ad158f356e16", msg);

        }

        return result;

    }

}
