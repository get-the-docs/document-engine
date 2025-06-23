package org.getthedocs.documentengine.core.integrationtests.docx;

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
import org.getthedocs.documentengine.core.documentstructure.StoredResultDocument;
import org.getthedocs.documentengine.core.service.OutputFormat;
import org.getthedocs.documentengine.core.service.TemplateService;
import org.getthedocs.documentengine.core.service.TemplateServiceRegistry;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
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
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DocxTemplateIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocxTemplateIT.class);

    private final String inputDir = "unittests/docx";

    final String JSON_DIR = "/values/integrationtests";

    private static TemplateService ts = TemplateServiceRegistry.getInstance();

    private String getDataForTestCase(final String tcName, final String fileName) {
        final String dataFile = JSON_DIR + File.separator +
                this.getClass().getSimpleName() + File.separator +
                tcName + File.separator + fileName;
        LOGGER.debug("Test json data file path: {}", dataFile);

        return getTestDataFromFile(dataFile);
    }

    private String getTestDataFromFile(final String fileName) {

        String result = "";

        try {
            final File file = new ClassPathResource(fileName).getFile();
            final BufferedReader br = new BufferedReader(new FileReader(file));

            String actLine;
            final StringBuilder tmp = new StringBuilder();
            while ((actLine = br.readLine()) != null) {
                tmp.append(actLine);
            }

            result = tmp.toString();
        } catch (final IOException e) {
            LOGGER.warn("Could not read test data file: {}", e.getMessage());
        }

        return result;
    }

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

    @Test
    public void simpleTemplateWithJsonContextTest() {
        final String fileName = "contract_v09_hu-jsonpath.docx";
        final String resultFileName = "result-" + fileName;

        final String tranId = UUID.randomUUID().toString();
        final String data = this.getDataForTestCase("simpleTemplateWithJsonContextTest", "contractdata.json");

        try {
            final StoredResultDocument result = ts.fillAndSave(tranId, inputDir + File.separator + fileName, data);

            LOGGER.info("Done - Result file: {}.", resultFileName);
            Assert.assertTrue(result.isGenerated());

        } catch (final TemplateServiceException e) {
            LOGGER.error("Error generating the result file.", e);

            fail();
        }

        assertTrue(true);
    }

}
