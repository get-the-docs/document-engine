package net.videki.templateutils.documentstructure.builder.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.videki.templateutils.documentstructure.builder.core.service.DocumentStructureBuilder;
import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.core.util.FileSystemHelper;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.fail;

public class YmlDocumentStructureBuilderTest {
    private static final Locale LC_HU = new Locale("hu");

    private static final String TL_COVER_KEY = "cover";
    private static final String TL_COVER_FILE = "01-cover_v03.docx";

    private static final String TL_CONTRACT_KEY = "contract";
    private static final String TL_CONTRACT_FILE_HU = "02-contract_v09_hu.docx";
    private static final String TL_CONTRACT_FILE_EN = "02-contract_v09_en.docx";

    private static final String TL_TERMS_KEY = "terms";
    private static final String TL_TERMS_FILE = "03-terms_v02.docx";

    private static final String TL_CONDITIONS_KEY = "terms";
    private static final String TL_CONDITIONS_FILE = "04-conditions_v11.xlsx";

    private static final String inputDir = "/full-example";
    private final String projectOutDir = System.getProperty("user.dir") + "/target";

    private final TemplateServiceConfiguration templateServiceConfiguration = TemplateServiceConfiguration.getInstance();

//    @Test
    public void saveDocStructure() {
        try {
            final DocumentStructure templateStructure = getContractDocStructure();

            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.writeValue(
                    new File(FileSystemHelper.getFileNameWithPath(projectOutDir, "result.yml")), templateStructure);
        } catch (TemplateNotFoundException | TemplateServiceException | IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void readTemplateElement() {
        TemplateElement result;

        SimpleModule module = new SimpleModule();
        module.addDeserializer(TemplateElement.class, new TemplateElementDeserializer());
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            mapper.registerModule(module);

            try {
                result = mapper.readValue(YmlDocStructureBuilder.class.getResourceAsStream("/template-element.yml"),
                        TemplateElement.class);
                    final String msg = String.format("TemplateElement loaded: %s",
                            ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE));

                    System.out.println(msg);

            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

    }

    @Test
    public void readDocStructure() {
        try {
            final DocumentStructureBuilder dsBuilder = new YmlDocStructureBuilder();

            final DocumentStructure ds =
                    dsBuilder.build("/contract-vintage_v02.yml");

        } catch (TemplateNotFoundException | TemplateServiceException e) {
            e.printStackTrace();
            fail();
        }

    }

    private DocumentStructure getContractDocStructure() throws TemplateServiceConfigurationException {
        final DocumentStructure result = new DocumentStructure();

        result.getElements().add(
                new TemplateElement(TL_COVER_KEY, FileSystemHelper.getFileNameWithPath(inputDir, TL_COVER_FILE))
                    .withDefaultLocale(LC_HU));

        result.getElements().add(
                new TemplateElement(TL_CONTRACT_KEY)
                    .withTemplateName(FileSystemHelper.getFileNameWithPath(inputDir, TL_CONTRACT_FILE_HU), LC_HU)
                    .withTemplateName(FileSystemHelper.getFileNameWithPath(inputDir, TL_CONTRACT_FILE_EN), Locale.ENGLISH)
                    .withDefaultLocale(LC_HU)
        );

        result.getElements().add(
                new TemplateElement(TL_TERMS_KEY, FileSystemHelper.getFileNameWithPath(inputDir, TL_TERMS_FILE))
                    .withDefaultLocale(LC_HU));

        result.getElements().add(
                new TemplateElement(TL_CONDITIONS_KEY, FileSystemHelper.getFileNameWithPath(inputDir, TL_CONDITIONS_FILE))
                    .withDefaultLocale(LC_HU));

        return result;
    }

}
