package net.videki.documentengine.core.service;

/*-
 * #%L
 * docs-core
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

import net.videki.documentengine.core.service.exception.TemplateProcessException;
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
    public void notSameFormatNullObjectTest() {
        Assert.assertFalse(InputFormat.DOCX.isSameFormat(null));
    }

    @Test
    public void notSameFormatTest() {
        Assert.assertFalse(InputFormat.DOCX.isSameFormat(OutputFormat.PDF));
    }

    @Test
    public void getInputFormatForFileNameUnhandledInputFormatTest() {

        try {
            Assert.assertEquals(InputFormat.getInputFormatForFileName("myTestFile.qwe"), InputFormat.DOCX);
        } catch (final TemplateProcessException e) {
            Assert.assertEquals("c14d63df-8db2-45a2-bf21-e62fe60a23a0", e.getCode());
        }
    }

    @Test
    public void getStrValueTest() {
        final InputFormat iFormat= InputFormat.valueOf(InputFormat.DOCX.name());

        Assert.assertEquals("DOCX", iFormat.getStrValue());
    }


    @Test
    public void getInputFormatForFileNameNoExtensionTest() {
        try {
            Assert.assertEquals(InputFormat.getInputFormatForFileName("myTestFile"), InputFormat.DOCX);
        } catch (final TemplateProcessException e) {
            Assert.assertEquals("c14d63df-8db2-45a2-bf21-e62fe60a23a0", e.getCode());
        }
    }

}
