/*
 * Copyright (c) 2021. Levente Ban
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.videki.templateutils.template.core.processor;

import net.videki.templateutils.template.core.processor.converter.Converter;
import net.videki.templateutils.template.core.processor.converter.pdf.docx4j.DocxToPdfConverter;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.OutputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;
import java.util.Map;

public class ConverterRegistryTest {

    @Test
    public void inputTemplateProcessorDocxTest() {
        InputTemplateProcessor p = TemplateProcessorRegistry.getInputTemplateProcessor(InputFormat.DOCX);

        Assert.assertNotNull(p);
    }

    @Test
    public void inputTemplateProcessorXlsxTest() {
        InputTemplateProcessor p = TemplateProcessorRegistry.getInputTemplateProcessor(InputFormat.XLSX);

        Assert.assertNotNull(p);
    }

    @Test
    public void inputTemplateProcessorReAssignUseUnregistered() {
        Map<InputFormat, Map<OutputFormat, Converter>> mockConverters = new EnumMap<>(InputFormat.class);
        final Map<OutputFormat, Converter> mockOutputConverters = new EnumMap<>(OutputFormat.class);
        mockOutputConverters.put(OutputFormat.PDF, new DocxToPdfConverter());
        mockConverters.put(InputFormat.XLSX, mockOutputConverters);

        try {
            Converter converterBefore =
                    ConverterRegistry.getConverter(InputFormat.DOCX, OutputFormat.PDF);

            Assert.assertNotNull(converterBefore);

            ConverterRegistry.setConverters(mockConverters);

            ConverterRegistry.init();

        } catch (TemplateProcessException e) {
            Assert.assertEquals("9a27e2a0-6260-40d7-ac10-ad158f356e16", e.getCode());

        }
    }

    @After
    public void resetProcessors() {
        TemplateProcessorRegistry.init();
    }
}
