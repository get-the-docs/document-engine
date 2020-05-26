package net.videki.templateutils.template.core.processor.input.xlsx;

import net.videki.templateutils.template.core.util.FileSystemHelper;
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

import java.io.IOException;
import java.io.OutputStream;

public class XlsxInputTemplateProcessorTest {
    private InputTemplateProcessor processor = new XlsxInputTemplateProcessor();

    @Test
    public void getInputFormatTest() {
        Assert.assertEquals(InputFormat.XLSX, processor.getInputFormat());
    }

    @Test
    public void fillValidTemplateMatchingFormat() {

        final String inputDir = "/templates/xlsx";

        final String fileName = "xlsTemplate_v11.xlsx";
        this.processor.fill(FileSystemHelper.getFullPath(inputDir, fileName),
                getContractTestData());
    }

    @Test
    public void fillTemplateNotFound() {

        final String inputDir = "/templates/xlsx";

        final String fileName = "this_template_file_does_not_exist.xlsx";

        try (final OutputStream ignore = processor.fill(FileSystemHelper.getFullPath(inputDir, fileName),
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

        final String inputDir = "/templates/xlsx";

        final String fileName = "xlsTemplate_v11-expression_error.xlsx";

        try (final OutputStream ignore = processor.fill(FileSystemHelper.getFullPath(inputDir, fileName),
                getContractTestData())) {

            Assert.assertTrue(true);
        } catch (IOException e) {
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
