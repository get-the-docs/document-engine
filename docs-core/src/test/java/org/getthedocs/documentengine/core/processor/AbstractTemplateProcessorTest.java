package org.getthedocs.documentengine.core.processor;

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

import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class AbstractTemplateProcessorTest {

    @Test
    public void getTemplate() {
        final String fileName = "unittests/docx/SimpleContract_v1_21-pojo.docx";
        try {
            InputStream i = AbstractTemplateProcessor.getTemplate(fileName);

            assertNotNull(i);
        } catch (TemplateServiceConfigurationException e) {
            fail();
        }
    }
}
