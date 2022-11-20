package net.videki.templateutils.documentstructure.builder.core.documentstructure.repository.filesystem;

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

import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.provider.documentstructure.repository.ConfigurableDocumentStructureRepository;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;

public class FileSystemConfigurableDocumentStructureRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemConfigurableDocumentStructureRepositoryTest.class);

    @Test
    public void getDocumentStructureOptions() {
        try {
            final var optionsRepo =
                    (ConfigurableDocumentStructureRepository) TemplateServiceConfiguration
                    .getInstance()
                    .getDocumentStructureRepository();
            final var dso =
                    optionsRepo.getDocumentStructureOptions("contracts/contract_v02-options.yml");

            Assert.assertNotNull(dso);
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            LOGGER.error("getDocumentStructureOptions error", e);
            fail();
        }

    }

    @Test
    public void getDocumentStructureOptionsNonExisting() {
        try {
            final var optionsRepo =
                    (ConfigurableDocumentStructureRepository) TemplateServiceConfiguration
                            .getInstance()
                            .getDocumentStructureRepository();
            final var dso =
                    optionsRepo.getDocumentStructureOptions("contracts/contract_v02-options-there_is_no_such_file.yml");

        } catch (final TemplateProcessException e) {
            Assert.assertEquals("0578f1ec-a33b-4a73-af71-a14f0e55c0b9", e.getCode());

        } catch (final TemplateServiceException e) {
            LOGGER.error("getDocumentStructureOptions error", e);
            fail();
        }

    }

}
