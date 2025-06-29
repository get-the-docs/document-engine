package org.getthedocs.documentengine.core.processor;

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

import org.getthedocs.documentengine.core.processor.input.InputTemplateProcessor;
import org.getthedocs.documentengine.core.processor.input.noop.NoopTemplateProcessor;
import org.getthedocs.documentengine.core.service.InputFormat;
import org.getthedocs.documentengine.core.service.TemplateService;
import org.getthedocs.documentengine.core.service.exception.TemplateProcessException;
import org.getthedocs.documentengine.core.configuration.TemplateServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Template processor registry. The container holds the processors for the
 * supported input formats to impersonate the them.
 * 
 * @author Levente Ban
 */
public class TemplateProcessorRegistry {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);

    /**
     * The registered input processors.
     */
    private static Map<InputFormat, InputTemplateProcessor> processors = new EnumMap<>(InputFormat.class);

    /**
     * A built-in pass-through processor to produce a template untouched as a result
     * document.
     */
    private static InputTemplateProcessor noopProcessor = new NoopTemplateProcessor();
    static {
        init();
    }

    /**
     * Sets the template processor set to the provided set.
     * 
     * @param param the list of template processors.
     */
    public static void setProcessors(Map<InputFormat, InputTemplateProcessor> param) {
        processors.clear();
        processors.putAll(param);
    }

    /**
     * Reinitializes the template processors.
     */
    public static void resetProcessors() {
        synchronized (TemplateProcessorRegistry.processors) {
            init();
        }
    }

    /**
     * Internal initializer.
     */
    protected static void init() {
        processors.clear();

        processors.putAll(TemplateServiceConfiguration.getInstance().getInputProcessors());
    }

    /**
     * Returns the input processor for a given format, if registered.
     * 
     * @param format the input format.
     * @return the template processor, if present.
     */
    public static InputTemplateProcessor getInputTemplateProcessor(final InputFormat format) {
        InputTemplateProcessor result;

        result = processors.get(format);

        if (result == null) {
            final String supportedFormats = processors.keySet().stream().map(s -> s.getStrValue())
                    .collect(Collectors.joining(", "));
            final String msg = String.format(
                    "Unhandled input format %s. Has been a new one defined? Supporetd formats are: %s", format,
                    supportedFormats);
            LOGGER.error(msg);
            throw new TemplateProcessException("d320e547-b4c6-45b2-bdd9-19ac0b699c97", msg);
        }
        return result;
    }

    /**
     * Returns the built-in identity processor.
     * 
     * @return the processor implementation.
     */
    public static InputTemplateProcessor getNoopProcessor() {
        return noopProcessor;
    }

}
