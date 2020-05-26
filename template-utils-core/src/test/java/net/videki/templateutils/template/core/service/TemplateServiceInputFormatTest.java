package net.videki.templateutils.template.core.service;

import net.videki.templateutils.template.core.processor.TemplateProcessorRegistry;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;

public class TemplateServiceInputFormatTest {

    private Map<InputFormat, InputTemplateProcessor> mockProcessors;

    private TemplateService ts = TemplateServiceRegistry.getInstance();

    public static class DocxMockProcessor implements InputTemplateProcessor {
        @Override
        public InputFormat getInputFormat() {
            return InputFormat.DOCX;
        }

        @Override
        public <T> OutputStream fill(String templateFileName, T dto) {
            return new ByteArrayOutputStream();
        }
    }

    @Test
    public void processorTestDocxOK() {
        this.mockProcessors = new EnumMap<>(InputFormat.class);
        this.mockProcessors.put(InputFormat.DOCX, new DocxMockProcessor());

        TemplateProcessorRegistry.setProcessors(mockProcessors);

        final OutputStream result;
        try {
            result = ts.fill("myTemplate.docx", ContractDataFactory.createContract());
        } catch (TemplateServiceException e) {
            e.printStackTrace();

            Assert.assertFalse(false);
            return;
        }
        Assert.assertNotNull(result);
    }

    @Test
    public void docxProcessorTestNotRegistered() {
        this.mockProcessors = new EnumMap<>(InputFormat.class);
        this.mockProcessors.put(InputFormat.DOCX, new DocxMockProcessor());

        TemplateProcessorRegistry.setProcessors(mockProcessors);

        try {
            ts.fill("myTemplate.rtf", ContractDataFactory.createContract());
            Assert.assertFalse(false);
        } catch (TemplateProcessException e) {
            Assert.assertEquals("c14d63df-8db2-45a2-bf21-e62fe60a23a0", e.getCode());
        } catch (TemplateServiceException e) {
            e.printStackTrace();

            Assert.assertFalse(false);
        }
    }

    @Test
    public void templateRegistryDocxProcessorTestUnhandledInputFormat() {
        this.mockProcessors = new EnumMap<>(InputFormat.class);
        this.mockProcessors.put(InputFormat.DOCX, new DocxMockProcessor());

        TemplateProcessorRegistry.setProcessors(mockProcessors);

        try {
            ts.fill("myTemplate.rtf", new Object());
            Assert.assertFalse(false);
        } catch (TemplateProcessException e) {
            Assert.assertEquals("c14d63df-8db2-45a2-bf21-e62fe60a23a0", e.getCode());
            //d320e547-b4c6-45b2-bdd9-19ac0b699c97
        } catch (TemplateServiceException e) {
            e.printStackTrace();

            Assert.assertFalse(false);
        }
    }

}
