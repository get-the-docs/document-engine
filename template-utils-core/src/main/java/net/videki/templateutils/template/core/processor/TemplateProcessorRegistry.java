package net.videki.templateutils.template.core.processor;

import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.processor.input.docx.DocxInputTemplateProcessor;
import net.videki.templateutils.template.core.processor.input.noop.NoopTemplateProcessor;
import net.videki.templateutils.template.core.processor.input.xlsx.XlsxInputTemplateProcessor;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

public class TemplateProcessorRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);

    private static Map<InputFormat, InputTemplateProcessor> processors = new EnumMap<>(InputFormat.class);
    private static InputTemplateProcessor noopProcessor = new NoopTemplateProcessor();
    static {
        init();
    }

    public static void setProcessors(Map<InputFormat, InputTemplateProcessor> param) {
        processors.clear();
        processors.putAll(param);
    }

    protected static void init() {
        processors.clear();

        processors.put(InputFormat.DOCX, new DocxInputTemplateProcessor());
        processors.put(InputFormat.XLSX, new XlsxInputTemplateProcessor());

    }

    public static InputTemplateProcessor getInputTemplateProcessor(final InputFormat format) {
        InputTemplateProcessor result;

        result = processors.get(format);

        if (result == null) {
            final String msg = String.format("Unhandled input format %s. Has been a new one defined?", format);
            LOGGER.error(msg);
            throw new TemplateProcessException("d320e547-b4c6-45b2-bdd9-19ac0b699c97", msg);
        }
        return result;
    }

    public static InputTemplateProcessor getNoopProcessor() {
        return noopProcessor;
    }

}
