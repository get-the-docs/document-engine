package net.videki.templateutils.template.core.processor;

/*-
 * #%L
 * template-utils-core
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

import net.videki.templateutils.template.core.processor.converter.Converter;
import net.videki.templateutils.template.core.processor.converter.pdf.docx4j.DocxToPdfConverter;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.OutputFormat;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

/**
 * Registry to hold the configured document converters like docx-pdf (Input
 * format, output format pairs).
 * 
 * @author Levente Ban
 */
public class ConverterRegistry {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);

    /**
     * The internal converter map.
     */
    private static Map<InputFormat, Map<OutputFormat, Converter>> converters = new EnumMap<>(InputFormat.class);

    static {
        init();
    }

    /**
     * Sets the converter map to the given set of converters.
     * 
     * @param param the set of converters.
     */
    public static void setConverters(Map<InputFormat, Map<OutputFormat, Converter>> param) {
        converters.clear();
        converters.putAll(param);
    }

    /**
     * Initializes the converter registry.
     */
    protected static void init() {
        converters.clear();

        final Map<OutputFormat, Converter> docxConverters = new EnumMap<>(OutputFormat.class);
        docxConverters.put(OutputFormat.PDF, new DocxToPdfConverter());
        converters.put(InputFormat.DOCX, docxConverters);

    }

    /**
     * Returns the converter for a given input format, if registered.
     * 
     * @param inputFormat  the source format the converter accepts.
     * @param outputFormat the target output format the converter converts to.
     * @return the converter, if found.
     */
    public static Converter getConverter(final InputFormat inputFormat, final OutputFormat outputFormat) {
        Converter result = null;

        Map<OutputFormat, Converter> convertersForInputFormat = converters.get(inputFormat);

        if (convertersForInputFormat != null) {
            result = convertersForInputFormat.get(outputFormat);

        }

        if (convertersForInputFormat == null || result == null) {
            final String msg = String.format(
                    "No converter found for the source format to the given output type. " + "Source: %s, Target: %s.",
                    inputFormat, outputFormat);
            LOGGER.error(msg);
            throw new TemplateProcessException("9a27e2a0-6260-40d7-ac10-ad158f356e16", msg);

        }

        return result;

    }

}
