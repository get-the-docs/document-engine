package net.videki.templateutils.template.core.service;

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
        Assert.assertTrue(InputFormat.DOCX.isSameFormat(OutputFormat.PDF));
    }
}
