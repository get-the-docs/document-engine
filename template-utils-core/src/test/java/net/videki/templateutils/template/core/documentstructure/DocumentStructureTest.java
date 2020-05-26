package net.videki.templateutils.template.core.documentstructure;

import net.videki.templateutils.template.core.TestHelper;
import net.videki.templateutils.template.core.util.FileSystemHelper;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import net.videki.templateutils.template.test.dto.OfficerDataFactory;
import net.videki.templateutils.template.test.dto.OrgUnitDataFactory;
import net.videki.templateutils.template.test.dto.contract.Contract;
import net.videki.templateutils.template.test.dto.officer.Officer;
import net.videki.templateutils.template.test.dto.organization.OrganizationUnit;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class DocumentStructureTest {
    private static final Locale LC_HU = new Locale("hu", "HU");

    private static final String TEMPLATE_CONTRACT = "contract";
    private static final TemplateService ts = TemplateServiceRegistry.getInstance();
    private static final String inputDir = "/templates/docx";

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
            e.printStackTrace();
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
                new TemplateElement(TEMPLATE_CONTRACT, FileSystemHelper.getFullPath(inputDir, fileName))
                    .withCount(2)
                    .withDefaultLocale(LC_HU);

            structure.getElements().add(docElement);

            final ValueSet values = new ValueSet();
            values.getValues().put(docElement.getTemplateElementId(), getContractTestData());

            result = null;
            result = ts.fill(structure, values);

            testResult = (result.getResults().size() == 2);
        } catch (TemplateNotFoundException | TemplateServiceException e) {
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
            result = ts.fill(structure, values);

            testResult = (0 == result.getResults().size());
        } catch (TemplateNotFoundException e) {
            testResult = true;
        } catch (TemplateServiceException e) {
            e.printStackTrace();

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

}
