package net.videki.templateutils.template.core.documentstructure;

import net.videki.templateutils.template.core.TestHelper;
import net.videki.templateutils.template.core.configuration.util.FileSystemHelper;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import net.videki.templateutils.template.test.dto.OfficerDataFactory;
import net.videki.templateutils.template.test.dto.OrgUnitDataFactory;
import net.videki.templateutils.template.test.dto.contract.Contract;
import net.videki.templateutils.template.test.dto.officer.Officer;
import net.videki.templateutils.template.test.dto.organization.OrganizationUnit;

import org.junit.Test;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentStructureTest {

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

        final TemplateElement docElement = new TemplateElement(TEMPLATE_CONTRACT, "SimpleContract_v1_21.docx");
        docElement
                .withCount(1)
                .withDefaultLocale(new Locale("HU"));

        structure.getElements().add(docElement);

        assertEquals(structure.getElements().size(), 1);
    }

    @Test
    public void docCountForTemplateElementSingleTwoCopiesOkTest() {
        boolean testResult;

        final String fileName = "SimpleContract_v1_21.docx";

        final DocumentStructure structure = new DocumentStructure();

        final TemplateElement docElement =
                new TemplateElement(TEMPLATE_CONTRACT, FileSystemHelper.getFullPath(inputDir, fileName));
        docElement
                .withCount(2)
                .withDefaultLocale(new Locale("HU"));

        structure.getElements().add(docElement);

        final ValueSet values = new ValueSet();
        values.getValues().put(docElement.getTemplateElementId(), getContractTestData());

        List<OutputStream> resultDocs = null;
        try {
            resultDocs = ts.fill(structure, values);

            testResult = (resultDocs.size() == 2);
        } catch (TemplateNotFoundException | TemplateServiceException e) {
            testResult = false;
        } finally {
            TestHelper.closeResults(resultDocs);
        }

        assertTrue(testResult);

    }

    @Test
    public void docCountForTemplateElementStructureEmptyTest() {
        boolean testResult;

        final DocumentStructure structure = new DocumentStructure();

        final ValueSet values = new ValueSet();

        List<OutputStream> resultDocs = null;
        try {
            resultDocs = ts.fill(structure, values);

            testResult = (0 == resultDocs.size());
        } catch (TemplateNotFoundException e) {
            testResult = true;
        } catch (TemplateServiceException e) {
            e.printStackTrace();

            testResult = false;
        } finally {
            TestHelper.closeResults(resultDocs);
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
