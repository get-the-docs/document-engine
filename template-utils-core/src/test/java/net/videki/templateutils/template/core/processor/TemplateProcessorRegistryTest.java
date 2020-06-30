package net.videki.templateutils.template.core.processor;

import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.TemplateServiceInputFormatTest;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;
import java.util.Map;

public class TemplateProcessorRegistryTest {

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
/*
    @Test
    public void inputTemplateProcessorReAssignUseUnregistered() {
        Map<InputFormat, InputTemplateProcessor> mockProcessors = new EnumMap<>(InputFormat.class);
        mockProcessors.put(InputFormat.DOCX, new TemplateServiceInputFormatTest.DocxMockProcessor());

        try {
            InputTemplateProcessor processorBefore =
                    TemplateProcessorRegistry.getInputTemplateProcessor(InputFormat.XLSX);

            Assert.assertNotNull(processorBefore);

            TemplateProcessorRegistry.setProcessors(mockProcessors);

        } catch (TemplateProcessException e) {
            Assert.assertEquals("d320e547-b4c6-45b2-bdd9-19ac0b699c97", e.getCode());

        }
    }
*/
    @After
    public void resetProcessors() {
        TemplateProcessorRegistry.init();
    }
}
