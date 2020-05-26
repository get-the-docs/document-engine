package net.videki.templateutils.template.core.service;

import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.junit.Assert;
import org.junit.Test;

public class InputFormatTest {

    @Test
    public void findByValueTest() {
        final InputFormat iFormat= InputFormat.valueOf(InputFormat.DOCX.name());

        Assert.assertEquals(InputFormat.DOCX, iFormat);
    }

    @Test
    public void sameValueTest() {
        Assert.assertTrue(InputFormat.DOCX.isSameFormat(OutputFormat.DOCX));
    }

    @Test
    public void notSameValueTest() {
        Assert.assertFalse(InputFormat.DOCX.isSameFormat(OutputFormat.PDF));
    }

    @Test
    public void getInputFormatForFileNameUnhandledInputFormat() {

        try {
            Assert.assertEquals(InputFormat.getInputFormatForFileName("myTestFile.qwe"), InputFormat.DOCX);
        } catch (TemplateProcessException e) {
            Assert.assertEquals("c14d63df-8db2-45a2-bf21-e62fe60a23a0", e.getCode());
        }
    }

}
