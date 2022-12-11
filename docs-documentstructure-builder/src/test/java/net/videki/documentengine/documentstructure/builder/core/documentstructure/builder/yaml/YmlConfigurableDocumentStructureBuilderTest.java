package net.videki.documentengine.documentstructure.builder.core.documentstructure.builder.yaml;

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

import net.videki.documentengine.core.provider.documentstructure.builder.DocumentStructureOptionsBuilder;
import net.videki.documentengine.core.provider.documentstructure.builder.yaml.YmlConfigurableDocStructureBuilder;
import net.videki.documentengine.core.provider.documentstructure.repository.filesystem.FileSystemDocumentStructureRepository;
import net.videki.documentengine.core.service.exception.TemplateNotFoundException;
import net.videki.documentengine.core.service.exception.TemplateServiceException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static org.junit.Assert.fail;

public class YmlConfigurableDocumentStructureBuilderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(YmlConfigurableDocumentStructureBuilderTest.class);

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

}
