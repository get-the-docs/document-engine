package net.videki.templateutils.template.core.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class OutputFormatTest {

    @Test
    public void findByValueTest() {
        final OutputFormat outputFormat= OutputFormat.valueOf(OutputFormat.DOCX.name());

        Assert.assertEquals(OutputFormat.DOCX, outputFormat);
    }

    @Test
    public void sameValueTest() {
        Assert.assertTrue(OutputFormat.DOCX.isSameFormat(InputFormat.DOCX));
    }

    @Test
    public void sameFormatDifferentTypeTest() {
        Assert.assertFalse(OutputFormat.DOCX.isSameFormat(new Date()));
    }
}
