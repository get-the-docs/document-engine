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

import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.service.InputFormat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

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
