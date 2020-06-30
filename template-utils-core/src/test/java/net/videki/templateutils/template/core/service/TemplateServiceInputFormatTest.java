package net.videki.templateutils.template.core.service;

import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.OutputStream;

public class TemplateServiceInputFormatTest {

    private final TemplateService ts = TemplateServiceRegistry.getInstance();

    @Test
    public void processorDocxOKTest() {

        final OutputStream result;
        try {
            result = ts.fill("templates/docx/SimpleContract_v1_21-pojo.docx",
                    ContractDataFactory.createContract());
        } catch (TemplateServiceException e) {
            e.printStackTrace();

            Assert.assertFalse(false);
            return;
        }
        Assert.assertNotNull(result);
    }


    @Test
    public void processorDocxToPdfOKTest() {

        final OutputStream result;
        try {
            result = ts.fill("templates/docx/SimpleContract_v1_21-pojo.docx",
                    ContractDataFactory.createContract(), OutputFormat.PDF);
        } catch (TemplateServiceException e) {
            e.printStackTrace();

            Assert.assertFalse(false);
            return;
        }
        Assert.assertNotNull(result);
    }

    @Test
    public void docxProcessorTestNotRegistered() {

        try {
            ts.fill("myTemplate.rtf", ContractDataFactory.createContract());
            Assert.assertFalse(false);
        } catch (TemplateProcessException e) {
            Assert.assertEquals("c14d63df-8db2-45a2-bf21-e62fe60a23a0", e.getCode());
        } catch (TemplateServiceException e) {
            Assert.fail();
        }
    }

    @Test
    public void templateRegistryDocxProcessorTestUnhandledInputFormat() {

        try {
            ts.fill("myTemplate.rtf", new Object());
            Assert.assertFalse(false);
        } catch (TemplateProcessException e) {
            Assert.assertEquals("c14d63df-8db2-45a2-bf21-e62fe60a23a0", e.getCode());
            //d320e547-b4c6-45b2-bdd9-19ac0b699c97
        } catch (TemplateServiceException e) {
            Assert.fail();
        }
    }

}
