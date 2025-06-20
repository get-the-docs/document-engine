package org.getthedocs.documentengine.documentstructure;

/*-
 * #%L
 * docs-documentstructure-builder
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

import org.getthedocs.documentengine.core.provider.documentstructure.builder.DocumentStructureOptionsBuilder;
import org.getthedocs.documentengine.core.documentstructure.descriptors.DocumentStructureOptions;
import org.getthedocs.documentengine.core.documentstructure.descriptors.OptionalTemplateElement;
import org.getthedocs.documentengine.core.documentstructure.descriptors.TemplateElementOption;
import org.getthedocs.documentengine.core.provider.documentstructure.builder.yaml.YmlConfigurableDocStructureBuilder;
import org.getthedocs.documentengine.core.documentstructure.DocumentStructure;
import org.getthedocs.documentengine.core.documentstructure.descriptors.TemplateElement;
import org.getthedocs.documentengine.core.provider.documentstructure.repository.filesystem.FileSystemDocumentStructureRepository;
import org.getthedocs.documentengine.core.service.exception.TemplateNotFoundException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Optional;

import static org.junit.Assert.fail;

public class DocumentStructureOptionsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentStructureOptionsTest.class);

    @Test
    public void readDocStructureOptions() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlConfigurableDocStructureBuilder();

            final InputStream dsOptionsFileAsStream = FileSystemDocumentStructureRepository
                    .class.getClassLoader().getResourceAsStream("documentstructures/contracts/contract_v02-options.yml");
            dsBuilder.buildOptions(dsOptionsFileAsStream);

        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("readDocStructureOptions error", e);
            fail();
        }

    }

    @Test
    public void readDocStructureNonExistentFile() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlConfigurableDocStructureBuilder();

            dsBuilder.buildOptions(null);

        } catch (final TemplateServiceConfigurationException e) {
            Assert.assertEquals("bf4dbf44-8b7b-4cba-b449-906083eabf0d", e.getCode());
        } catch (final Exception e) {
            LOGGER.error("readDocStructureNonExistentFile error", e);
            fail();
        }

    }

    @Test
    public void docStructureOptionsCheckFriendlyNameOk() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlConfigurableDocStructureBuilder();

            final InputStream dsOptionsFileAsStream = FileSystemDocumentStructureRepository
                    .class
                    .getClassLoader()
                    .getResourceAsStream("documentstructures/contracts/contract_v02-options.yml");
            final DocumentStructureOptions dso =
                    dsBuilder.buildOptions(dsOptionsFileAsStream);

            Assert.assertTrue(dso.getElements() != null && !dso.getElements().isEmpty() &&
                    dso.getElementIdByFriendlyName("conditions-vintage_basic_underaged").isPresent());
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("docStructureOptionsCheckFriendlyNameOk error", e);
            fail();
        }
    }

    @Test
    public void docStructureOptionsCheckOptionalTemplateElementOk() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlConfigurableDocStructureBuilder();

            final InputStream dsOptionsFileAsStream = FileSystemDocumentStructureRepository
                    .class
                    .getClassLoader()
                    .getResourceAsStream("documentstructures/contracts/contract_v02-options.yml");
            final DocumentStructureOptions dso = dsBuilder.buildOptions(dsOptionsFileAsStream);

            Assert.assertNotNull(dso.getElements().get(0));

        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("docStructureOptionsCheckOptionalTemplateElementOk error", e);
            fail();
        }
    }

    @Test
    public void docStructureOptionsReplaceElementWithOptionOk() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlConfigurableDocStructureBuilder();

            final InputStream dsFileAsStream = FileSystemDocumentStructureRepository
                    .class
                    .getClassLoader()
                    .getResourceAsStream("documentstructures/contracts/contract-vintage_v02.yml");
            final InputStream dsOptionsFileAsStream = FileSystemDocumentStructureRepository
                    .class
                    .getClassLoader()
                    .getResourceAsStream("documentstructures/contracts/contract_v02-options.yml");

            final DocumentStructure ds = dsBuilder.build(dsFileAsStream);
            final DocumentStructureOptions dso = dsBuilder.buildOptions(dsOptionsFileAsStream);

            final Optional<TemplateElement> baseElement = ds.getElementByFriendlyName("conditions");
            final Optional<OptionalTemplateElement> option =
                    dso.getElementByFriendlyName("conditions-vintage_basic_underaged");
            if (baseElement.isPresent() && option.isPresent()) {
                final TemplateElement baseElementData = baseElement.get();
                final OptionalTemplateElement optionData = option.get();
                optionData.applyElement(baseElement.get());

                Assert.assertEquals("conditions-vintage_basic_underaged",
                        baseElementData.getTemplateElementId().getId());
            } else {
                fail();
            }

        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("docStructureOptionsReplaceElementWithOptionOk error", e);
            fail();
        }
    }

    @Test
    public void docStructureOptionsReplaceElementWithOptionAsTemplateElementOk() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlConfigurableDocStructureBuilder();

            final InputStream dsFileAsStream = FileSystemDocumentStructureRepository
                    .class
                    .getClassLoader()
                    .getResourceAsStream("documentstructures/contracts/contract-vintage_v02.yml");
            final InputStream dsOptionsFileAsStream = FileSystemDocumentStructureRepository
                    .class
                    .getClassLoader()
                    .getResourceAsStream("documentstructures/contracts/contract_v02-options.yml");
            final DocumentStructure ds = dsBuilder.build(dsFileAsStream);
            final DocumentStructureOptions dso = dsBuilder.buildOptions(dsOptionsFileAsStream);

            final Optional<TemplateElement> baseElement = ds.getElementByFriendlyName("conditions");
            final Optional<OptionalTemplateElement> option =
                    dso.getElementByFriendlyName("conditions-vintage_basic_underaged");
            if (baseElement.isPresent() && option.isPresent()) {
                final OptionalTemplateElement optionData = option.get();
                optionData.applyElement(baseElement.get());

                Assert.assertEquals("conditions-vintage_basic_underaged",
                        optionData.asTemplateElement().getTemplateElementId().getId());

            } else {
                fail();
            }

        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("docStructureOptionsReplaceElementWithOptionAsTemplateElementOk error", e);
            fail();
        }
    }

    @Test
    public void docStructureOptionsGetElementOption() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlConfigurableDocStructureBuilder();

            final InputStream dsFileAsStream = FileSystemDocumentStructureRepository
                    .class
                    .getClassLoader()
                    .getResourceAsStream("documentstructures/contracts/contract-vintage_v02.yml");
            final InputStream dsOptionsFileAsStream = FileSystemDocumentStructureRepository
                    .class
                    .getClassLoader()
                    .getResourceAsStream("documentstructures/contracts/contract_v02-options.yml");
            final DocumentStructure ds = dsBuilder.build(dsFileAsStream);
            final DocumentStructureOptions dso = dsBuilder.buildOptions(dsOptionsFileAsStream);

            final Optional<TemplateElement> baseElement = ds.getElementByFriendlyName("conditions");
            final Optional<OptionalTemplateElement> option =
                    dso.getElementByFriendlyName("conditions-vintage_basic_underaged");
            if (baseElement.isPresent() && option.isPresent()) {
                final OptionalTemplateElement optionData = option.get();
                optionData.applyElement(baseElement.get());

                Assert.assertEquals(TemplateElementOption.REPLACE, optionData.getOption());

            } else {
                fail();
            }

        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("docStructureOptionsGetElementOption error", e);
            fail();
        }
    }
}
