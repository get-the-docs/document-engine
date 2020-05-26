package net.videki.templateutils.template.core.processor;

import net.videki.templateutils.template.core.processor.converter.Converter;
import net.videki.templateutils.template.core.processor.converter.pdf.DocxToPdfConverter;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.OutputFormat;
import net.videki.templateutils.template.core.service.TemplateServiceInputFormatTest;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;
import java.util.HashMap;
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
