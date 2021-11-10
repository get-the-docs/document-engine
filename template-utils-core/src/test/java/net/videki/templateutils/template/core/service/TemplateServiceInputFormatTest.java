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

package net.videki.templateutils.template.core.service;

import net.videki.templateutils.template.core.documentstructure.StoredResultDocument;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.fail;

public class TemplateServiceInputFormatTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceInputFormatTest.class);
    private final TemplateService ts = TemplateServiceRegistry.getInstance();

    @Test
    public void processorDocxOKTest() {

        try {
            final StoredResultDocument result = ts.fillAndSave("unittests/docx/SimpleContract_v1_21-pojo.docx",
                    ContractDataFactory.createContract());

            Assert.assertTrue(result.isGenerated());
        } catch (TemplateServiceException e) {
            e.printStackTrace();

            Assert.assertFalse(false);
            return;
        }

    }


    @Test
    public void processorDocxToPdfOKTest() {

        try {
            final StoredResultDocument result = ts.fillAndSave("unittests/docx/SimpleContract_v1_21-pojo.docx",
                    ContractDataFactory.createContract(), OutputFormat.PDF);

            Assert.assertTrue(result.isGenerated());
        } catch (TemplateServiceException e) {
            LOGGER.error("Error generating the template. ", e);

            fail();
            return;
        }
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
