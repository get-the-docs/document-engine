package net.videki.templateutils.template.core.service;

import net.videki.templateutils.template.core.TestHelper;
import net.videki.templateutils.template.core.documentstructure.*;
import net.videki.templateutils.template.core.util.FileSystemHelper;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.test.dto.ContractDataFactory;
import net.videki.templateutils.template.test.dto.OfficerDataFactory;
import net.videki.templateutils.template.test.dto.OrgUnitDataFactory;
import net.videki.templateutils.template.test.dto.contract.Contract;
import net.videki.templateutils.template.test.dto.officer.Officer;
import net.videki.templateutils.template.test.dto.organization.OrganizationUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.*;

public class TemplateServiceParamTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceParamTest.class);
    private static final TemplateService ts = TemplateServiceRegistry.getInstance();
    private static final Locale LC_HU = new Locale("hu", "HU");

    @Test
    public void fillNoParamsSingleDocTemplateAndDtoTest() {

        String resultCode;

        try (final ResultDocument ignore = ts.fill((String)null, null)) {

        } catch (TemplateServiceConfigurationException e) {
            resultCode = e.getCode();

            assertEquals("070f463e-743f-4cb2-a651-bd11e844728d", resultCode);
        } catch (Exception e) {
            fail();
        }


    }

    @Test
    public void fillNoParamsSingleDocTemplateAndDtoAndFormatTest() {
        String resultCode;

        try(final ResultDocument ignored = ts.fill(null, null, null)) {
        } catch (TemplateServiceConfigurationException e) {
            resultCode = e.getCode();

            assertEquals("c936e550-8b0e-4577-bffa-7f36b211d981", resultCode);
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    public void fillNoParamsSingleDocStructAndValuesTest() {
        String resultCode;

        try {
            ts.fill((DocumentStructure) null, null);

            LOGGER.error("Error: TemplateServiceConfigurationException expected.");

            fail();
        } catch (TemplateServiceConfigurationException e) {
            resultCode = e.getCode();

            assertEquals("bdaa9376-28b4-4718-9859-2ef5d88ab3b0", resultCode);
        } catch (Exception e) {
            LOGGER.error("Error:", e);

            fail();
        }
    }

    @Test
    public void fillSimpleTemplatePojoTest() {

        final String inputDir = "unittests/docx";
        final String projectOutDir = System.getProperty("user.dir") + "/target/test-classes";

        final String fileName = "SimpleContract_v1_21-pojo.docx";
        final String resultFileName = "fillSimpleTemplatePojoTest-" + fileName;

        final Contract dto = ContractDataFactory.createContract();

        try {
            final StoredResultDocument result = ts.fillAndSave(inputDir + File.separator + fileName, dto, OutputFormat.DOCX);

            LOGGER.info("Done. - Result file: {}/{}.", projectOutDir, resultFileName);

            assertNotNull(result);
        } catch (Exception e) {
            LOGGER.error("Error: ", e);

            fail();
        }
    }

    @Test
    public void fillSimpleTemplateMapTest() {

        final String inputDir = "unittests/docx";
        final String projectOutDir = System.getProperty("user.dir") + "/target/test-classes";

        final String fileName = "SimpleContract_v1_21.docx";
        final String resultFileName = "fillSimpleTemplateMapTest-" + fileName;

        try {
            final StoredResultDocument result = ts.fillAndSave(inputDir + File.separator + fileName,
                    getContractTestData().getCtx(),
                    OutputFormat.DOCX);

            LOGGER.info("Done. - Result file: {}/{}.", projectOutDir, resultFileName);

            assertTrue(result.isGenerated());
        } catch (Exception e) {
            LOGGER.error("Error:", e);

            fail();
        }
    }

    @Test
    public void fillSimpleTemplateTemplateContextTest() {

        final String inputDir = "unittests/docx";
        final String projectOutDir = System.getProperty("user.dir") + "/target/test-classes";

        final String fileName = "SimpleContract_v1_21.docx";
        final String resultFileName = "fillSimpleTemplateTemplateContextTest-" + fileName;

        try {
            final StoredResultDocument result = ts.fillAndSave(inputDir + File.separator + fileName,
                    getContractTestData(), OutputFormat.DOCX);

            LOGGER.info("Done. - Result file: {}/{}.", projectOutDir, resultFileName);

            assertNotNull(result);
        } catch (Exception e) {
            LOGGER.error("Error:", e);

            fail();
        }

    }

    @Test(expected = net.videki.templateutils.template.core.service.exception.TemplateNotFoundException.class)
    public void fillSimpleTemplateNonexistingTemplateFile() throws Exception {
        final String inputDir = "unittests/docx";

        final String fileName = "there_is_no_such_template_file.docx";

        ts.fill(inputDir + File.separator + fileName, getContractTestData(), OutputFormat.DOCX);

        fail();
    }

    @Test
    public void fillSimpleTemplateViaDocumentStructureTest() {

        final String inputDir = "unittests/docx";

        final String fileName = "SimpleContract_v1_21.docx";

        final DocumentStructure structure = new DocumentStructure();
        final TemplateElement docElement;
        GenerationResult result = null;
        try {
            docElement =
                new TemplateElement("contract", inputDir + File.separator + fileName, LC_HU)
                    .withCount(1);

            structure.getElements().add(docElement);

            final ValueSet values = new ValueSet("fillSimpleTemplateViaDocumentStructureTest-" +
                    UUID.randomUUID().toString());
            values.getValues().put(docElement.getTemplateElementId(), getContractTestData());

            result = ts.fill(structure, values);

            LOGGER.info("Done.");
            assertNotNull(result);
        } catch (Exception e) {
            LOGGER.error("Error: ", e);

            fail();
        } finally {
            TestHelper.closeResults(result);
        }

    }

    @Test
    public void fillAndSaveNoParamsSingleDocTemplateAndDtoTest() {

        String resultCode;

        try {
            final StoredResultDocument ignore = ts.fillAndSave((String)null, null);
        } catch (TemplateServiceConfigurationException e) {
            resultCode = e.getCode();

            assertEquals("070f463e-743f-4cb2-a651-bd11e844728d", resultCode);
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    public void fillAndSaveNoParamsSingleDocTemplateAndDtoAndFormatTest() {
        String resultCode;

        try {
            final StoredResultDocument ignored = ts.fillAndSave(null, null, null);
        } catch (TemplateServiceConfigurationException e) {
            resultCode = e.getCode();

            assertEquals("c936e550-8b0e-4577-bffa-7f36b211d981", resultCode);
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    public void fillAndSaveNoParamsSingleDocStructAndValuesTest() {
        String resultCode;

        try {
            ts.fillAndSave((DocumentStructure) null, null);

            LOGGER.error("Error: TemplateServiceConfigurationException expected.");

            fail();
        } catch (TemplateServiceConfigurationException e) {
            resultCode = e.getCode();

            assertEquals("bdaa9376-28b4-4718-9859-2ef5d88ab3b0", resultCode);
        } catch (Exception e) {
            LOGGER.error("Error:", e);

            fail();
        }
    }

    @Test
    public void fillAndSaveSimpleTemplatePojoTest() {

        final String inputDir = "unittests/docx";
        final String projectOutDir = System.getProperty("user.dir") + "/target/test-classes";

        final String fileName = "SimpleContract_v1_21-pojo.docx";
        final String resultFileName = "fillAndSaveSimpleTemplatePojoTest-" + fileName;

        final Contract dto = ContractDataFactory.createContract();

        try {
            final StoredResultDocument result = ts.fillAndSave(inputDir + File.separator + fileName, dto, OutputFormat.DOCX);

            LOGGER.info("Done. - Result file: {}/{}.", projectOutDir, resultFileName);

            assertNotNull(result);
        } catch (Exception e) {
            LOGGER.error("Error: ", e);

            fail();
        }
    }

    @Test
    public void fillAndSaveSimpleTemplateMapTest() {

        final String inputDir = "unittests/docx";
        final String projectOutDir = System.getProperty("user.dir") + "/target/test-classes";

        final String fileName = "SimpleContract_v1_21.docx";
        final String resultFileName = "fillAndSaveSimpleTemplateMapTest-" + fileName;

        try {
            final StoredResultDocument result = ts.fillAndSave(inputDir + File.separator + fileName,
                    getContractTestData().getCtx(),
                    OutputFormat.DOCX);

            LOGGER.info("Done. - Result file: {}/{}.", projectOutDir, resultFileName);

            assertNotNull(result);
        } catch (Exception e) {
            LOGGER.error("Error:", e);

            fail();
        }
    }

    @Test
    public void fillAndSaveSimpleTemplateTemplateContextTest() {

        final String inputDir = "unittests/docx";
        final String projectOutDir = System.getProperty("user.dir") + "/target/test-classes";

        final String fileName = "SimpleContract_v1_21.docx";
        final String resultFileName = "fillAndSaveSimpleTemplateTemplateContextTest-" + fileName;

        try {
            final StoredResultDocument result = ts.fillAndSave(inputDir + File.separator + fileName,
                    getContractTestData(), OutputFormat.DOCX);

            LOGGER.info("Done. - Result file: {}/{}.", projectOutDir, resultFileName);

            assertNotNull(result);
        } catch (Exception e) {
            LOGGER.error("Error:", e);

            fail();
        }

    }

    @Test(expected = net.videki.templateutils.template.core.service.exception.TemplateNotFoundException.class)
    public void fillAndSaveSimpleTemplateNonexistingTemplateFileTest() throws Exception {
        final String inputDir = "unittests/docx";

        final String fileName = "there_is_no_such_template_file.docx";

        ts.fill(inputDir + File.separator + fileName, getContractTestData(), OutputFormat.DOCX);

        fail();
    }

    @Test
    public void fillAndSaveSimpleTemplateViaDocumentStructureTest() {

        final String inputDir = "unittests/docx";

        final String fileName = "SimpleContract_v1_21.docx";

        final DocumentStructure structure = new DocumentStructure();
        final TemplateElement docElement;
        GenerationResult result = null;
        try {
            docElement =
                    new TemplateElement("contract", inputDir + File.separator + fileName, LC_HU)
                            .withCount(1);

            structure.getElements().add(docElement);

            final ValueSet values = new ValueSet("fillAndSaveSimpleTemplateTemplateContextTest-" +
                    UUID.randomUUID().toString(), LC_HU);
            values.getValues().put(docElement.getTemplateElementId(), getContractTestData());

            result = ts.fill(structure, values);

            LOGGER.info("Done.");
            assertNotNull(result);
        } catch (Exception e) {
            LOGGER.error("Error: ", e);

            fail();
        } finally {
            TestHelper.closeResults(result);
        }

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

}
