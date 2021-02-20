package net.videki.templateutils.template.core.documentstructure.descriptors;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.util.FileSystemHelper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.fail;

public class TemplateElementTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateElementTest.class);

    private static final Locale LC_HU = new Locale("hu", "HU");

    private static final String inputDirDocStructureContracts = "/integrationtests/contracts";
    private static final String TL_CONTRACT_KEY = "contract";
    private static final String TL_CONTRACT_FILE_HU = "contract_v09_hu.docx";
    private static final String TL_CONTRACT_FILE_EN = "contract_v09_en.docx";

    @Test
    public void constructorNameCount() {

        try {
            final var te = new TemplateElement(TL_CONTRACT_KEY, 2)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                            LC_HU)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_EN,
                            Locale.ENGLISH)
                    .withDefaultLocale(LC_HU)
                    .withLocales(List.of(Locale.ITALIAN, Locale.CANADA));

            Assert.assertEquals(
                    inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                    te.getTemplateName(new Locale("es")));
        } catch (final Exception e) {
            LOGGER.error("Wrong template caught.", e);

            fail();
        }
    }

    @Test
    public void inputFormatTest() {

        try {
            final var te = new TemplateElement(TL_CONTRACT_KEY, 2)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                            LC_HU)
                    .withDefaultLocale(LC_HU);

            Assert.assertEquals(
                    InputFormat.DOCX,
                    te.getFormat());
        } catch (final Exception e) {
            LOGGER.error("Wrong template caught.", e);

            fail();
        }
    }

    @Test
    public void divergentFormatTest() {

        try {
            final var te = new TemplateElement(TL_CONTRACT_KEY, 2)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                            LC_HU)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + "contract_v09_en.xlsx",
                            Locale.ENGLISH)
                    .withDefaultLocale(LC_HU);

            Assert.assertEquals(
                    InputFormat.DOCX,
                    te.getFormat());
        } catch (final TemplateServiceConfigurationException e) {
            Assert.assertEquals("7d1ef0fd-839d-4190-94b3-bf84a597c9ab", e.getCode());
        } catch (final Exception e) {
            LOGGER.error("Unexpected exception caught.", e);

            fail();
        }
    }

    @Test
    public void builderTestLocaleDefaultOk() {

        try {
            final var te = new TemplateElement(TL_CONTRACT_KEY)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                            LC_HU)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_EN,
                            Locale.ENGLISH)
                    .withDefaultLocale(LC_HU);

            Assert.assertEquals(
                    inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                    te.getTemplateName(LC_HU));
        } catch (final Exception e) {
            LOGGER.error("Wrong template caught.", e);

            fail();
        }
    }

    @Test
    public void builderTestLocaleSpecificOk() {

        try {
            final var te = new TemplateElement(TL_CONTRACT_KEY)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                            LC_HU)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_EN,
                            Locale.ENGLISH)
                    .withDefaultLocale(LC_HU);

            Assert.assertEquals(
                    inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_EN,
                    te.getTemplateName(Locale.ENGLISH));
        } catch (final Exception e) {
            LOGGER.error("Wrong template caught.", e);

            fail();
        }
    }

    @Test
    public void builderTestLocaleNonExisting() {

        try {
            final var te = new TemplateElement(TL_CONTRACT_KEY)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                            LC_HU)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_EN,
                            Locale.ENGLISH)
                    .withDefaultLocale(LC_HU);

            Assert.assertEquals(
                    inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                    te.getTemplateName(Locale.GERMAN));
        } catch (final Exception e) {
            LOGGER.error("Wrong template caught.", e);

            fail();
        }
    }

    @Test
    public void builderTestLocaleNullShouldReturnDefault() {

        try {
            final var te = new TemplateElement(TL_CONTRACT_KEY)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                            LC_HU)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_EN,
                            Locale.ENGLISH)
                    .withDefaultLocale(LC_HU);

            Assert.assertEquals(
                    inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                    te.getTemplateName(null));
        } catch (final Exception e) {
            LOGGER.error("Wrong template caught.", e);

            fail();
        }
    }

    @Test
    public void builderTestWithLocalesShouldSetDefaultLocaleTemplate() {

        try {
            final var te = new TemplateElement(TL_CONTRACT_KEY)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                            LC_HU)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_EN,
                            Locale.ENGLISH)
                    .withDefaultLocale(LC_HU)
                    .withLocales(List.of(Locale.ITALIAN, Locale.CANADA));

            Assert.assertEquals(
                    inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                    te.getTemplateName(Locale.CANADA));
        } catch (final Exception e) {
            LOGGER.error("Wrong template caught.", e);

            fail();
        }
    }

    @Test
    public void builderTestWithLocalesShouldSetDefaultLocaleLangOnly() {

        try {
            final var te = new TemplateElement(TL_CONTRACT_KEY)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                            LC_HU)
                    .withTemplateName(
                            inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_EN,
                            Locale.ENGLISH)
                    .withDefaultLocale(LC_HU)
                    .withLocales(List.of(Locale.ITALIAN, Locale.CANADA));

            Assert.assertEquals(
                    inputDirDocStructureContracts + File.separator + TL_CONTRACT_FILE_HU,
                    te.getTemplateName(new Locale("es")));
        } catch (final Exception e) {
            LOGGER.error("Wrong template caught.", e);

            fail();
        }
    }

}
