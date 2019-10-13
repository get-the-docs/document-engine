package net.videki.templateutils.template.core.service.docx;

import net.videki.templateutils.template.core.configuration.util.FileSystemHelper;
import net.videki.templateutils.template.core.service.OutputFormat;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import net.videki.templateutils.template.test.dto.OfficerDataFactory;
import net.videki.templateutils.template.test.dto.OrgUnitDataFactory;
import net.videki.templateutils.template.test.dto.contract.Contract;
import net.videki.templateutils.template.test.dto.officer.Officer;
import net.videki.templateutils.template.test.dto.organization.OrganizationUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertTrue;

public class DocxTemplateTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocxTemplateTest.class);

    private final String inputDir = "/templates/docx";
    private final String projectOutDir = System.getProperty("user.dir") + "/build/test-results/test";

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
            OutputStream result = ts.fill(FileSystemHelper.getFullPath(inputDir, fileName), context, OutputFormat.DOCX);

            LOGGER.info("Result file: {}/{}.", projectOutDir, resultFileName);

            FileOutputStream o = new FileOutputStream(FileSystemHelper.getFullPath(projectOutDir, resultFileName));

            o.write(((ByteArrayOutputStream)result).toByteArray());
            o.flush();
            o.close();
            result.close();

            LOGGER.info("Done.");
        } catch (IOException e) {
            System.out.println("error:");
            e.printStackTrace();
        } catch (TemplateServiceException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

}
