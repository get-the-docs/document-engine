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
import net.videki.templateutils.template.core.documentstructure.StoredGenerationResult;
import net.videki.templateutils.template.core.documentstructure.StoredResultDocument;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import net.videki.templateutils.template.test.dto.DocDataFactory;
import net.videki.templateutils.template.test.dto.OfficerDataFactory;
import net.videki.templateutils.template.test.dto.OrgUnitDataFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.*;

public class JsonpathPlaceholderTemplateContextIT extends TestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonpathPlaceholderTemplateContextIT.class);

    private static final Locale LC_HU = new Locale("hu", "HU");

    private static final String TL_ORG_KEY = "org";
    private static final String TL_OFFICER_KEY = "officer";
    private static final String TL_DOC_KEY = "doc";
    private static final String TL_CONTRACT_KEY = "contract";

    private final TemplateService templateService = TemplateServiceRegistry.getInstance();

    @Test
    public void singleTemplateWordWithJsonpathPlaceholdersShouldBeEvaluated() {
        try {
            final String transactionId = UUID.randomUUID().toString();
            final String template = "unittests/docx/SimpleContract_v1_21-jsonpath.docx";
            final TemplateContext testData = getTestDataFromJson(transactionId);

            final StoredResultDocument result = this.templateService.fillAndSave(transactionId, template, testData);

            assertTrue(result.isGenerated());
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("Error creating the result documents.", e);
            fail();
        }
    }

    @Test
    public void singleTemplateXlsWithJsonpathPlaceholdersShouldBeEvaluated() {
        try {
            final String transactionId = UUID.randomUUID().toString();
            final String template = "unittests/xlsx/xlsTemplate_v11-jsonpath.xlsx";
            final TemplateContext testData = getTestDataFromJson(transactionId);

            final StoredResultDocument result = this.templateService.fillAndSave(transactionId, template, testData);

            assertTrue(result.isGenerated());
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("Error creating the result documents.", e);
            fail();
        }
    }

    @Test
    public void documentStructurePojoPlaceholdersWithJsonpathDTOsShouldBeEvaluated() {
        try {
            final String transactionId = UUID.randomUUID().toString();
            final String documentStructureId = "integrationtests/placeholder-pojo.yml";
            final ValueSet testData = getValueSetForTestCase("valueset.json");

            final StoredGenerationResult result =
                    this.templateService.fillAndSaveDocumentStructureByName(transactionId, documentStructureId, testData);

            assertArrayEquals(new Boolean[]{true, true, true, true}, result.getResults()
                    .stream()
                    .map(StoredResultDocument::isGenerated)
                    .toArray(Boolean[]::new));
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("Error creating the result documents.", e);
            fail();
        }
    }

    @Test
    public void documentStructureJsonPathPlaceholdersWithJsonpathDTOsShouldBeEvaluated() {
        try {
            final String transactionId = UUID.randomUUID().toString();
            final String documentStructureId = "integrationtests/placeholder-jsonpath.yml";
            final ValueSet testData = getValueSetForTestCase("valueset.json");

            final StoredGenerationResult result =
                    this.templateService.fillAndSaveDocumentStructureByName(transactionId, documentStructureId, testData);

            assertArrayEquals(new Boolean[]{true, true, true, true}, result.getResults()
                    .stream()
                    .map(StoredResultDocument::isGenerated)
                    .toArray(Boolean[]::new));
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("Error creating the result documents.", e);
            fail();
        }
    }

    private TemplateContext getTestDataFromJson(final String transactionId) {

        final TemplateContext result = new TemplateContext();
        result.withContext(TL_ORG_KEY, OrgUnitDataFactory.createOrgUnit())
                .withContext(TL_OFFICER_KEY, OfficerDataFactory.createOfficer())
                .withContext(TL_DOC_KEY, DocDataFactory.createDocData(transactionId))
                .withContext(TL_CONTRACT_KEY, ContractDataFactory.createContract())
                .build();

        return result;
    }

    private ValueSet getTestDataJsonpathAsValueSet(final String transactionId) {

        final ValueSet result = new ValueSet();
        result.withContext(TL_ORG_KEY, OrgUnitDataFactory.createOrgUnit())
                .withContext(TL_OFFICER_KEY, OfficerDataFactory.createOfficer())
                .withContext(TL_DOC_KEY, DocDataFactory.createDocData(transactionId))
                .withContext(TL_CONTRACT_KEY, ContractDataFactory.createContract())
                .withLocale(LC_HU)
                .build();

        return result;
    }
}
