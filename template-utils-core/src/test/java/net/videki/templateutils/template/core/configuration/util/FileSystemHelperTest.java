package net.videki.templateutils.template.core.configuration.util;

import org.junit.Assert;
import org.junit.Test;

public class FileSystemHelperTest {
    @Test
    public void getFileNameTest() {
        final String inputDir = "/full-example/MyFileName.docx";

        Assert.assertEquals("MyFileName.docx", FileSystemHelper.getFileName(inputDir));
    }
}
