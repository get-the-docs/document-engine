package net.videki.templateutils.template.core.processor.input.docx;

/*-
 * #%L
 * template-utils-core
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

import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.processor.input.PlaceholderEvalException;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import net.videki.templateutils.template.test.dto.contract.Contract;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class DocxStamperInputTemplateProcessorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocxStamperInputTemplateProcessorTest.class);
    private InputTemplateProcessor processor = new DocxStamperInputTemplateProcessor();

    @Test
    public void getInputFormatTest() {
        Assert.assertEquals(InputFormat.DOCX, processor.getInputFormat());
    }

    @Test
    public void fillValidTemplateMatchingFormat() {

        final String inputDir = "unittests/docx";

        final String fileName = "SimpleContract_v1_21-pojo.docx";
        this.processor.fill(inputDir + File.separator + fileName, getContractTestData());
    }

    @Test
    public void fillTemplateNotFound() {

        final String inputDir = "unittests/docx";

        final String fileName = "this_template_file_does_not_exist.docx";

        try (final OutputStream ignore = processor.fill(inputDir + File.separator + fileName,
                getContractTestData())) {

            Assert.assertFalse(false);
        } catch (final TemplateNotFoundException e) {
            Assert.assertEquals("e12c71e9-f27f-48ba-b600-2a0a071c5958", e.getCode());
        } catch (final IOException e) {
            LOGGER.error("fillTemplateNotFound error", e);
        }
    }

    @Test
    public void fillValidTemplatePlaceholderError() {

        final String inputDir = "unittests/docx";

        final String fileName = "SimpleContract_v1_21-pojo-placeholder_error.docx";

        try (final OutputStream ignore = processor.fill(inputDir + File.separator + fileName,
                getContractTestData())) {

            Assert.assertFalse(false);
        } catch (PlaceholderEvalException e) {
            Assert.assertEquals("ff03cf41-25fb-463a-829d-e2b411df4c16", e.getCode());
        } catch (IOException e) {
            LOGGER.error("fillValidTemplatePlaceholderError error", e);
        }
    }

    private TemplateContext getContractTestData() {
        final Contract dto = ContractDataFactory.createContract();
//        final OrganizationUnit orgUnit = OrgUnitDataFactory.createOrgUnit();
//        final Officer officer = OfficerDataFactory.createOfficer();

        final TemplateContext context = new TemplateContext();
        context.addValueObject(dto);
//        context.addValueObject(orgUnit);
//        context.addValueObject(officer);

        System.out.println(context.toJson());
        return context;
    }
}
