package net.videki.templateutils.template.core.provider.documentstructure.builder.yaml;

/*-
 * #%L
 * template-utils-core
 * %%
 * Copyright (C) 2021 Levente Ban
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.v1.DocumentStructureV1;
import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureBuilder;
import net.videki.templateutils.template.core.documentstructure.TemplateElement;
import net.videki.templateutils.template.core.provider.documentstructure.repository.filesystem.FileSystemDocumentStructureRepository;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.core.util.FileSystemHelper;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class YmlDocumentStructureBuilderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(YmlDocumentStructureBuilderTest.class);

    private static final Locale LC_HU = new Locale("hu");

    private static final String TL_COVER_KEY = "cover";
    private static final String TL_COVER_FILE = "01-cover_v03.docx";

    private static final String TL_CONTRACT_KEY = "contract";
    private static final String TL_CONTRACT_FILE_HU = "contract_v09_hu.docx";
    private static final String TL_CONTRACT_FILE_EN = "contract_v09_en.docx";

    private static final String TL_TERMS_KEY = "terms";
    private static final String TL_TERMS_FILE = "terms_v02.docx";

    private static final String TL_CONDITIONS_KEY = "terms";
    private static final String TL_CONDITIONS_FILE = "conditions_v11.xlsx";

    private static final String inputDir = "full-example";
    private final String projectOutDir = System.getProperty("user.dir") + File.separator + "target";

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    static {
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(TemplateElement.class, new TemplateElementDeserializer());

        mapper.registerModule(module);
    }

    @Test
    public void saveDocStructure() {
        try {
            final DocumentStructure templateStructure = getContractDocStructure();

            mapper.writer().writeValue(
                    new File(FileSystemHelper.getFileNameWithPath(projectOutDir, "result.yml")), templateStructure);
        } catch (final TemplateNotFoundException | TemplateServiceException | IOException e) {
            LOGGER.error("Error saving doc structure.", e);

            fail();
        }

    }

    @Test
    public void readTemplateElement() {
        TemplateElement result;



        try {
            result = mapper.reader().readValue(YmlDocStructureBuilder.class.getClassLoader()
                            .getResourceAsStream("documentstructures/template-element.yml"),
                    TemplateElement.class);
                final String msg = String.format("TemplateElement loaded: %s",
                        ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE));

            LOGGER.debug("Template element read successfully: {}", msg);
        } catch (final Exception e) {
            LOGGER.error("Error saving doc structure.", e);

            fail();
        }

    }

    @Test
    public void readDocStructure() {
        try {
            final DocumentStructureBuilder dsBuilder = new YmlDocStructureBuilder();

            final InputStream dsFileAsStream = FileSystemDocumentStructureRepository
                    .class.getClassLoader().getResourceAsStream("contracts/vintage/contract-vintage_v02.yml");

            dsBuilder.build(dsFileAsStream);

        } catch (final TemplateProcessException e) {
            assertEquals("cfb09b69-cb69-4cb9-b7b0-b060c0717cf3", e.getCode());
        } catch (final TemplateServiceException e) {
            LOGGER.error("Error reading doc structure.", e);

            fail();
        }

    }

    private DocumentStructure getContractDocStructure() throws TemplateServiceConfigurationException {
        final DocumentStructure result = new DocumentStructureV1();

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
