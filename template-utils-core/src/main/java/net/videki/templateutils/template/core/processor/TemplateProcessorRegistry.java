package net.videki.templateutils.template.core.processor;

import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.processor.input.noop.NoopTemplateProcessor;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Template processor registry.
 * The container holds the processors for the supported input formats to impersonate the them. 
 * @author Levente Ban
 */
public class TemplateProcessorRegistry {
    /**
     * Logger.
     */
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

    public static void resetProcessors() {
        synchronized (TemplateProcessorRegistry.processors) {
            init();
        }
    }

    protected static void init() {
        processors.clear();

        processors.putAll(TemplateServiceConfiguration.getInstance().getInputProcessors());
    }

    public static InputTemplateProcessor getInputTemplateProcessor(final InputFormat format) {
        InputTemplateProcessor result;

        result = processors.get(format);

        if (result == null) {
            final String supportedFormats = processors.keySet()
                    .stream()
                    .map(s -> s.getStrValue()).collect(Collectors.joining(", "));
            final String msg = String.format(
                    "Unhandled input format %s. Has been a new one defined? Supporetd formats are: %s",
                    format, supportedFormats);
            LOGGER.error(msg);
            throw new TemplateProcessException("d320e547-b4c6-45b2-bdd9-19ac0b699c97", msg);
        }
        return result;
    }

    public static InputTemplateProcessor getNoopProcessor() {
        return noopProcessor;
    }

}
