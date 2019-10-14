package net.videki.templateutils.template.core.service;

import net.videki.templateutils.template.core.configuration.util.FileSystemHelper;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.DocumentResult;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.service.docx.DocxTemplateTest;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
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

import java.io.*;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class FullExampleIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocxTemplateTest.class);

    private static final Locale LC_HU = new Locale("hu", "HU");

    private static final String TL_COVER_KEY = "cover";
    private static final String TL_COVER_FILE = "01-cover_v03.docx";

    private static final String TL_CONTRACT_KEY = "contract";
    private static final String TL_CONTRACT_FILE_HU = "02-contract_v09_hu.docx";
    private static final String TL_CONTRACT_FILE_EN = "02-contract_v09_en.docx";

    private static final String TL_TERMS_KEY = "terms";
    private static final String TL_TERMS_FILE = "03-terms_v02.docx";

    private static final String TL_CONDITIONS_KEY = "terms";
    private static final String TL_CONDITIONS_FILE = "04-conditions_v11.xlsx";

    private final String inputDir = "/full-example";
    private final String projectOutDir = System.getProperty("user.dir") + "/build/test-results/test";

    private static TemplateService ts = TemplateServiceRegistry.getInstance();

    @Test
    public void fillStructuredTemplateTest() {
        final Contract dto = ContractDataFactory.createContract();
        final OrganizationUnit orgUnit = OrgUnitDataFactory.createOrgUnit();

        final Officer officer = OfficerDataFactory.createOfficer();

        final TemplateContext context = new TemplateContext();
        context.getCtx().put("org", orgUnit);
        context.getCtx().put("officer", officer);
        context.getCtx().put("contract", dto);

        List<DocumentResult> resultDocs;
        try {
            resultDocs = ts.fill(getDocStructure(), getValueSet());

            for (final DocumentResult actResult : resultDocs) {
                LOGGER.info("Result file: {}/{}.", projectOutDir, actResult.getFileName());

                try (FileOutputStream o =
                             new FileOutputStream(FileSystemHelper.getFullPath(projectOutDir, actResult.getFileName()))) {

                    o.write(((ByteArrayOutputStream) actResult.getContent()).toByteArray());
                    o.flush();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException e) {
                    System.out.println("error:");
                    e.printStackTrace();
                } finally {
                    try {
                        actResult.getContent().close();
                    } catch (IOException e) {
                        System.out.println("error:");
                        e.printStackTrace();
                    }
                }
            }

            LOGGER.info("Done.");
        } catch (TemplateServiceException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

    private TemplateContext getCoverData() {
        final OrganizationUnit orgUnit = OrgUnitDataFactory.createOrgUnit();
        final Officer officer = OfficerDataFactory.createOfficer();
        final DocumentProperties documentProperties = DocDataFactory.createDocData();

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

    private DocumentStructure getDocStructure() throws TemplateServiceConfigurationException {
        final DocumentStructure result = new DocumentStructure();

        result.getElements().add(
                new TemplateElement(TL_COVER_KEY, FileSystemHelper.getFullPath(inputDir, TL_COVER_FILE))
                    .withDefaultLocale(LC_HU));

        result.getElements().add(
                new TemplateElement(TL_CONTRACT_KEY)
                    .withTemplateName(FileSystemHelper.getFullPath(inputDir, TL_CONTRACT_FILE_HU), LC_HU)
                    .withTemplateName(FileSystemHelper.getFullPath(inputDir, TL_CONTRACT_FILE_EN), Locale.ENGLISH)
                    .withDefaultLocale(LC_HU)
        );

        result.getElements().add(
                new TemplateElement(TL_TERMS_KEY, FileSystemHelper.getFullPath(inputDir, TL_TERMS_FILE))
                    .withDefaultLocale(LC_HU));

        result.getElements().add(
                new TemplateElement(TL_CONDITIONS_KEY, FileSystemHelper.getFullPath(inputDir, TL_CONDITIONS_FILE))
                    .withDefaultLocale(LC_HU));

        return result;
    }

    private ValueSet getValueSet() {

        final ValueSet result = new ValueSet()
                .addContext(TL_COVER_KEY, getCoverData())
                .addContext(TL_CONTRACT_KEY, getContractTestData())
                .addDefaultContext(TL_TERMS_KEY, null)
                .addContext(TL_CONDITIONS_KEY, getContractTestData())
                .withLocale(Locale.ENGLISH)
                ;

        return result;
    }

}
