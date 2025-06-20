package org.getthedocs.documentengine.core.processor.input.xlsx;

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

import org.getthedocs.documentengine.core.context.TemplateContext;
import org.getthedocs.documentengine.core.processor.input.InputTemplateProcessor;
import org.getthedocs.documentengine.core.service.InputFormat;
import org.getthedocs.documentengine.core.service.exception.TemplateNotFoundException;
import org.getthedocs.documentengine.test.dto.ContractDataFactory;
import org.getthedocs.documentengine.test.dto.OfficerDataFactory;
import org.getthedocs.documentengine.test.dto.OrgUnitDataFactory;
import org.getthedocs.documentengine.test.dto.contract.Contract;
import org.getthedocs.documentengine.test.dto.officer.Officer;
import org.getthedocs.documentengine.test.dto.organization.OrganizationUnit;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class JxlsInputTemplateProcessorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JxlsInputTemplateProcessorTest.class);
    private final InputTemplateProcessor processor = new JxlsInputTemplateProcessor();

    @Test
    public void getInputFormatTest() {
        Assert.assertEquals(InputFormat.XLSX, processor.getInputFormat());
    }

    @Test
    public void fillValidTemplateMatchingFormat() {

        final String inputDir = "unittests/xlsx";

        final String fileName = "xlsTemplate_v11.xlsx";
        this.processor.fill(inputDir + File.separator + fileName,
                getContractTestData());
    }

    @Test
    public void fillTemplateNotFound() {

        final String inputDir = "unittests/xlsx";

        final String fileName = "this_template_file_does_not_exist.xlsx";

        try (final OutputStream ignore = processor.fill(inputDir + File.separator + fileName,
                getContractTestData())) {

            Assert.assertFalse(false);
        } catch (final TemplateNotFoundException e) {
            Assert.assertEquals("3985eb36-6274-4870-af3a-c73a5c499873", e.getCode());
        } catch (final IOException e) {
            LOGGER.error("fillTemplateNotFound error", e);
        }
    }

    @Test
    public void fillValidTemplatePlaceholderError() {

        final String inputDir = "unittests/xlsx";

        final String fileName = "xlsTemplate_v11-expression_error.xlsx";

        try (final OutputStream ignore = processor.fill(inputDir + File.separator + fileName,
                getContractTestData())) {

            Assert.assertTrue(true);
        } catch (IOException | NoSuchMethodError e) {
            LOGGER.error("fillValidTemplatePlaceholderError error", e);
            Assert.assertFalse(false);
        }
    }

    private TemplateContext getContractTestData() {
        final Contract contract = ContractDataFactory.createContract();
        final OrganizationUnit orgUnit = OrgUnitDataFactory.createOrgUnit();
        final Officer officer = OfficerDataFactory.createOfficer();

        final TemplateContext context = new TemplateContext();
        context.getCtx().put("contract", contract);
        context.getCtx().put("org", orgUnit);
        context.getCtx().put("officer", officer);

        return context;
    }

}
