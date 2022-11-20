package net.videki.templateutils.template.core.processor.input.xlsx;

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

import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import net.videki.templateutils.template.test.dto.OfficerDataFactory;
import net.videki.templateutils.template.test.dto.OrgUnitDataFactory;
import net.videki.templateutils.template.test.dto.contract.Contract;
import net.videki.templateutils.template.test.dto.officer.Officer;
import net.videki.templateutils.template.test.dto.organization.OrganizationUnit;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class JxlsInputTemplateProcessorTest {
    private InputTemplateProcessor processor = new JxlsInputTemplateProcessor();

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
        } catch (TemplateNotFoundException e) {
            Assert.assertEquals("3985eb36-6274-4870-af3a-c73a5c499873", e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
