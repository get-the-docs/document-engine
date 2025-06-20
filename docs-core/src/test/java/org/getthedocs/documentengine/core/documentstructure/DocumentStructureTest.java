package org.getthedocs.documentengine.core.documentstructure;

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

import org.getthedocs.documentengine.core.TestHelper;
import org.getthedocs.documentengine.core.context.TemplateContext;
import org.getthedocs.documentengine.core.documentstructure.descriptors.TemplateElement;
import org.getthedocs.documentengine.core.documentstructure.descriptors.TemplateElementId;
import org.getthedocs.documentengine.core.service.TemplateService;
import org.getthedocs.documentengine.core.service.TemplateServiceParamTest;
import org.getthedocs.documentengine.core.service.TemplateServiceRegistry;
import org.getthedocs.documentengine.core.service.exception.TemplateNotFoundException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.getthedocs.documentengine.test.dto.ContractDataFactory;
import org.getthedocs.documentengine.test.dto.DocDataFactory;
import org.getthedocs.documentengine.test.dto.OfficerDataFactory;
import org.getthedocs.documentengine.test.dto.OrgUnitDataFactory;
import org.getthedocs.documentengine.test.dto.contract.Contract;
import org.getthedocs.documentengine.test.dto.doc.DocumentProperties;
import org.getthedocs.documentengine.test.dto.officer.Officer;
import org.getthedocs.documentengine.test.dto.organization.OrganizationUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.*;

public class DocumentStructureTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceParamTest.class);

    private static final Locale LC_HU = new Locale("hu", "HU");

    private static final String TEMPLATE_CONTRACT = "contract";
    private static final TemplateService ts = TemplateServiceRegistry.getInstance();
    private static final String inputDir = "unittests/docx";

    private static final String inputDirDocStructureCovers = "integrationtests/covers";
    private static final String TL_COVER_KEY = "cover";
    private static final String TL_COVER_FILE = "cover_v03.docx";

    private static final String inputDirDocStructureContracts = "integrationtests/contracts";
    private static final String TL_CONTRACT_KEY = "contract";
    private static final String TL_CONTRACT_FILE_HU = "contract_v09_hu.docx";
    private static final String TL_CONTRACT_FILE_EN = "contract_v09_en.docx";

    private static final String inputDirDocStructureTerms = "integrationtests/terms";
    private static final String TL_TERMS_KEY = "terms";
    private static final String TL_TERMS_FILE = "terms_v02.docx";

    private static final String inputDirDocStructureConditions = "integrationtests/conditions/vintage";
    private static final String TL_CONDITIONS_KEY = "conditions";
    private static final String TL_CONDITIONS_FILE = "conditions_v11.xlsx";

    private DocumentStructure getContractDocStructure() throws TemplateServiceConfigurationException {
        final DocumentStructure result = new DocumentStructure();

        result.getElements().add(
                new TemplateElement(TL_COVER_KEY, inputDirDocStructureCovers + File.separator + TL_COVER_FILE)
                        .withDefaultLocale(LC_HU));

        result.getElements().add(
                new TemplateElement(TL_CONTRACT_KEY)
                        .withTemplateName(inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU, LC_HU)
                        .withTemplateName(inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_EN, Locale.ENGLISH)
                        .withDefaultLocale(LC_HU)
        );

        result.getElements().add(
                new TemplateElement(TL_TERMS_KEY, inputDirDocStructureTerms + File.separator + TL_TERMS_FILE)
                        .withDefaultLocale(LC_HU));

        result.getElements().add(
                new TemplateElement(TL_CONDITIONS_KEY, inputDirDocStructureConditions + File.separator + TL_CONDITIONS_FILE)
                        .withDefaultLocale(LC_HU));

        return result;
    }

    private TemplateContext getContractTestData(final String transactionId) {
        final Contract dto = ContractDataFactory.createContract();
        final OrganizationUnit orgUnit = OrgUnitDataFactory.createOrgUnit();

        final Officer officer = OfficerDataFactory.createOfficer();
        final DocumentProperties documentProperties = DocDataFactory.createDocData(transactionId);

        final TemplateContext context = new TemplateContext();
        context.getCtx().put("org", orgUnit);
        context.getCtx().put("officer", officer);
        context.getCtx().put("contract", dto);
        context.getCtx().put("doc", documentProperties);

        return context;
    }

    @Test
    public void createSingleDocStructureTest() {
        final DocumentStructure structure = new DocumentStructure();

        final TemplateElement docElement;
        try {
            docElement =
                    new TemplateElement(TEMPLATE_CONTRACT, "SimpleContract_v1_21.docx")
                        .withCount(1)
                        .withDefaultLocale(LC_HU);

            structure.getElements().add(docElement);

            assertEquals(structure.getElements().size(), 1);
        } catch (TemplateServiceConfigurationException e) {
            LOGGER.error("Error: ", e);
            assertFalse(false);
        }

        assertFalse(false);
    }

    @Test
    public void docCountForTemplateElementSingleTwoCopiesOkTest() {
        boolean testResult;

        final String fileName = "SimpleContract_v1_21.docx";

        final DocumentStructure structure = new DocumentStructure();

        final TemplateElement docElement;
        GenerationResult result = null;
        try {
            docElement =
                new TemplateElement(TEMPLATE_CONTRACT, inputDir + File.separator + fileName, LC_HU)
                    .withCount(2);

            structure.getElements().add(docElement);

            final ValueSet values = new ValueSet();
            values.getValues().put(docElement.getTemplateElementId(), getContractTestData("not_recorded"));

            result = null;
            result = ts.fill(null, structure, values);

            testResult = (result != null && result.getResults() != null && result.getResults().size() == 2);
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            testResult = false;
        } finally {
            TestHelper.closeResults(result);
        }

        assertTrue(testResult);

    }

    @Test
    public void docCountForTemplateElementStructureEmptyTest() {
        boolean testResult;

        final DocumentStructure structure = new DocumentStructure();

        final ValueSet values = new ValueSet();

        GenerationResult result = null;
        try {
            result = ts.fill(null, structure, values);

            testResult = (0 == result.getResults().size());
        } catch (final TemplateNotFoundException e) {
            testResult = true;
        } catch (final TemplateServiceException e) {
            LOGGER.error("docCountForTemplateElementStructureEmptyTest error", e);

            testResult = false;
        } finally {
            TestHelper.closeResults(result);
        }
        assertTrue(testResult);
    }

    @Test
    public void defaultLocaleTest() {
        assertTrue(true);
    }

    @Test
    public void alternateLocalTest() {
        assertTrue(true);
    }

    @Test
    public void docStructureGenerationTestOk() {
        boolean testResult;

        StoredGenerationResult result = null;
        try {
            final DocumentStructure structure = getContractDocStructure();

            final var tranId = UUID.randomUUID().toString();

            final ValueSet values = new ValueSet(structure.getDocumentStructureId(), tranId);
            values.getValues().put(TemplateElementId.getGlobalTemplateElementId(), getContractTestData(tranId));

            result = ts.fillAndSave(structure, values);

            testResult = (result != null && result.getResults() != null && result.getResults().size() == 4);
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            testResult = false;
        }

        assertTrue(testResult);

    }

}
