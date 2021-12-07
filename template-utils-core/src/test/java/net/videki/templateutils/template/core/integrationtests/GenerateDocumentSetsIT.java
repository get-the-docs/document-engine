package net.videki.templateutils.template.core.integrationtests;

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

import net.videki.templateutils.template.core.TestHelper;
import net.videki.templateutils.template.core.context.dto.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.*;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import net.videki.templateutils.template.test.dto.DocDataFactory;
import net.videki.templateutils.template.test.dto.OfficerDataFactory;
import net.videki.templateutils.template.test.dto.OrgUnitDataFactory;
import net.videki.templateutils.template.test.dto.contract.Contract;
import net.videki.templateutils.template.test.dto.doc.DocumentProperties;
import net.videki.templateutils.template.test.dto.officer.Officer;
import net.videki.templateutils.template.test.dto.organization.OrganizationUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.*;

public class GenerateDocumentSetsIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateDocumentSetsIT.class);

    private static final Locale LC_HU = new Locale("hu", "HU");

    private static final String TL_COVER_KEY = "cover";
    private static final String TL_CONTRACT_KEY = "contract";
    private static final String TL_TERMS_KEY = "terms";
    private static final String TL_CONDITIONS_KEY = "conditions";

    private final TemplateService templateService = TemplateServiceRegistry.getInstance();

    @Test
    public void generateSeparateDocumentsDontSaveIT() {
        GenerationResult result = new GenerationResult(null);
        try {
            final ValueSet testData = getTestData("generateSeparateDocumentsIT");

            result = this.templateService
                    .fillDocumentStructureByName(null, 
                            "contract/vintage/contract-vintage_v02-separate.yml",
                            testData);

            assertArrayEquals(new Boolean[]{true, true, true, true}, result.getResults()
                    .stream()
                    .map(t -> t.getContent() != null)
                    .toArray(Boolean[]::new));
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("Error creating the result documents.", e);
            fail();
        } finally {
            result.getResults()
                    .forEach(t -> {
                        try {
                            LOGGER.debug("Closing generated result...");
                            t.close();
                        } catch(final Exception e) {
                            LOGGER.error("Error closing a generated result");
                        }
                    });
        }

        assertTrue(true);
    }

    @Test
    public void generateSeparateDocumentsIT() {
        try {
            final ValueSet testData = getTestData("generateSeparateDocumentsIT");

            final StoredGenerationResult result = this.templateService
                    .fillAndSaveDocumentStructureByName(
                            "contract/vintage/contract-vintage_v02-separate.yml",
                            testData);

            assertArrayEquals(new Boolean[]{true, true, true, true}, result.getResults()
                    .stream()
                    .map(StoredResultDocument::isGenerated)
                    .toArray(Boolean[]::new));
        } catch (TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("Error creating the result documents.", e);
            fail();
        }

        assertTrue(true);
    }

    @Test
    public void generateSeparateDocumentsWithTransactionIdIT() {
        try {
            final ValueSet testData = getTestData("generateSeparateDocumentsWithTransactionIdIT");
            final String transactionId = UUID.randomUUID().toString();

            LOGGER.debug(String.format("generateSeparateDocumentsWithTransactionIdIT - transaction id: [%s]", transactionId));

            final StoredGenerationResult result = this.templateService
                    .fillAndSaveDocumentStructureByName(transactionId,
                            "contract/vintage/contract-vintage_v02-separate.yml",
                            testData);

            assertTrue(TestHelper.isDocumentFilled(result.getResults()));

            assertArrayEquals(new Boolean[]{true, true, true, true}, result.getResults()
                    .stream()
                    .map(StoredResultDocument::isGenerated)
                    .toArray(Boolean[]::new));
        } catch (TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("Error creating the result documents.", e);
            fail();
        }

        assertTrue(true);
    }

    @Test
    public void generateSinglePdfDocumentIT() {
        try {
            final ValueSet testData = getTestData("generateSinglePdfDocumentIT");

            final StoredGenerationResult result = this.templateService
                    .fillAndSaveDocumentStructureByName(
                            "contract/vintage/contract-vintage_v02-concatenated_pdf.yml",
                            testData);

            assertArrayEquals(new Boolean[]{true, true, true, true}, result.getResults()
                    .stream()
                    .map(StoredResultDocument::isGenerated)
                    .toArray(Boolean[]::new));
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("Error creating the result document.", e);
            fail();
        }

        assertTrue(true);
    }

    private TemplateContext getCoverData(final String transactionId) {
        final OrganizationUnit orgUnit = OrgUnitDataFactory.createOrgUnit();
        final Officer officer = OfficerDataFactory.createOfficer();
        final DocumentProperties documentProperties = DocDataFactory.createDocData(transactionId);

        final TemplateContext context = new TemplateContext();
        context.getCtx().put("org", orgUnit);
        context.getCtx().put("officer", officer);
        context.getCtx().put("doc", documentProperties);

        return context;
    }

    private TemplateContext getContractTestData() {
        final Contract dto = ContractDataFactory.createContract();
        final OrganizationUnit orgUnit = OrgUnitDataFactory.createOrgUnit();
        final Officer officer = OfficerDataFactory.createOfficer();

        final TemplateContext context = new TemplateContext();
        context.getCtx().put("org", orgUnit);
        context.getCtx().put("officer", officer);
        context.getCtx().put("contract", dto);

        return context;
    }

    private ValueSet getTestData(final String transactionId) {

        final ValueSet result = new ValueSet(transactionId);
        result.addContext(getCoverData(transactionId))
                .addContext(getContractTestData())
                .addContext(getContractTestData())
                .withLocale(LC_HU)
                .build()
                ;

        return result;
    }

}
