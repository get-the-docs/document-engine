package net.videki.templateutils.template.core.processor.input.docx;

import net.videki.templateutils.template.core.configuration.util.FileSystemHelper;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.processor.input.PlaceholderEvalException;
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

public class DocxInputTemplateProcessorTest {

    private InputTemplateProcessor processor = new DocxInputTemplateProcessor();

    @Test
    public void getInputFormatTest() {
        Assert.assertEquals(InputFormat.DOCX, processor.getInputFormat());
    }

    @Test
    public void fillValidTemplateMatchingFormat() {

        final String inputDir = "/templates/docx";

        final String fileName = "SimpleContract_v1_21-pojo.docx";
        this.processor.fill(FileSystemHelper.getFullPath(inputDir, fileName), getContractTestData());
    }

    @Test
    public void fillTemplateNotFound() {

        final String inputDir = "/templates/docx";

        final String fileName = "this_template_file_does_not_exist.docx";

        try (final OutputStream ignore = processor.fill(FileSystemHelper.getFullPath(inputDir, fileName),
                getContractTestData())) {

            Assert.assertFalse(false);
        } catch (TemplateNotFoundException e) {
            Assert.assertEquals("e12c71e9-f27f-48ba-b600-2a0a071c5958", e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fillValidTemplatePlaceholderError() {

        final String inputDir = "/templates/docx";

        final String fileName = "SimpleContract_v1_21-pojo-placeholder_error.docx";

        try (final OutputStream ignore = processor.fill(FileSystemHelper.getFullPath(inputDir, fileName),
                getContractTestData())) {

            Assert.assertFalse(false);
        } catch (PlaceholderEvalException e) {
            Assert.assertEquals("ff03cf41-25fb-463a-829d-e2b411df4c16", e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TemplateContext getContractTestData() {
        final Contract dto = ContractDataFactory.createContract();
        final OrganizationUnit orgUnit = OrgUnitDataFactory.createOrgUnit();
        final Officer officer = OfficerDataFactory.createOfficer();

        final TemplateContext context = new TemplateContext();
        context.getCtx().put(TemplateContext.CONTEXT_ROOT_KEY_MODEL, dto);

        return context;
    }
}
