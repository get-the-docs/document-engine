package net.videki.templateutils.template.core.service.docx;

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

import net.videki.templateutils.template.core.context.dto.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.v1.StoredResultDocument;
import net.videki.templateutils.template.core.service.OutputFormat;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import net.videki.templateutils.template.test.dto.OfficerDataFactory;
import net.videki.templateutils.template.test.dto.OrgUnitDataFactory;
import net.videki.templateutils.template.test.dto.contract.Contract;
import net.videki.templateutils.template.test.dto.officer.Officer;
import net.videki.templateutils.template.test.dto.organization.OrganizationUnit;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DocxTemplateTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocxTemplateTest.class);

    private final String inputDir = "unittests/docx";

    private static TemplateService ts = TemplateServiceRegistry.getInstance();

    @Test
    public void simpleTemplateTest() {
        final String fileName = "SimpleContract_v1_21.docx";
        final String resultFileName = "result-" + fileName;

        final Contract dto = ContractDataFactory.createContract();
        final OrganizationUnit orgUnit = OrgUnitDataFactory.createOrgUnit();

        final Officer officer = OfficerDataFactory.createOfficer();

        final TemplateContext context = new TemplateContext();
        context.getCtx().put("org", orgUnit);
        context.getCtx().put("officer", officer);
        context.getCtx().put("contract", dto);

        try {
            final StoredResultDocument result = ts.fillAndSave(inputDir + File.separator + fileName, context, OutputFormat.DOCX);

            LOGGER.info("Done - Result file: {}.", resultFileName);
            Assert.assertTrue(result.isGenerated());

        } catch (TemplateServiceException e) {
            LOGGER.error("Error generating the result file.", e);

            fail();
        }

        assertTrue(true);
    }

}
