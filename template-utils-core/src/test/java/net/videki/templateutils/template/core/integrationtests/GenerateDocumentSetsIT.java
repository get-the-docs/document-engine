package net.videki.templateutils.template.core.integrationtests;

import net.videki.templateutils.template.core.context.TemplateContext;
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
import net.videki.templateutils.template.test.dto.contract.Contract;
import net.videki.templateutils.template.test.dto.doc.DocumentProperties;
import net.videki.templateutils.template.test.dto.officer.Officer;
import net.videki.templateutils.template.test.dto.organization.OrganizationUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import static org.junit.Assert.*;

public class GenerateDocumentSetsIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateDocumentSetsIT.class);

    private static final Locale LC_HU = new Locale("hu", "HU");

    private static final String TL_COVER_KEY = "cover";
    private static final String TL_CONTRACT_KEY = "contract";
    private static final String TL_TERMS_KEY = "terms";
    private static final String TL_CONDITIONS_KEY = "terms";

    private final TemplateService templateService = TemplateServiceRegistry.getInstance();

    @Test
    public void generateSeparateDocumentsIT() {
        try {
            final ValueSet testData = getTestData("generateSeparateDocumentsIT");

            final StoredGenerationResult result = this.templateService
                    .fillAndSaveDocumentStructureByName(
                            "contract/vintage/contract-vintage_v02-separate.yml",
                            testData);

            assertArrayEquals(new Boolean[]{true, true, true}, result.getResults()
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

            assertArrayEquals(new Boolean[]{true, true, true}, result.getResults()
                    .stream()
                    .map(StoredResultDocument::isGenerated)
                    .toArray(Boolean[]::new));
        } catch (TemplateNotFoundException | TemplateServiceException e) {
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
        result
                .addContext(TL_COVER_KEY, getCoverData(transactionId))
                .addContext(TL_CONTRACT_KEY, getContractTestData())
                .addDefaultContext(TL_TERMS_KEY, null)
                .addContext(TL_CONDITIONS_KEY, getContractTestData())
                .withLocale(LC_HU)
                ;

        return result;
    }

}
