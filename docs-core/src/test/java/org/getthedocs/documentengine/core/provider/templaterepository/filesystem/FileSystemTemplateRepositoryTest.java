package org.getthedocs.documentengine.core.provider.templaterepository.filesystem;

/*-
 * #%L
 * docs-core
 * %%
 * Copyright (C) 2023 - 2025 Levente Ban
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

import org.getthedocs.documentengine.core.provider.persistence.Page;
import org.getthedocs.documentengine.core.provider.persistence.Pageable;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.getthedocs.documentengine.core.template.descriptors.TemplateDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class FileSystemTemplateRepositoryTest {

    @TempDir
    static Path tempFolder;

    @Test
    public void testTemplateRepositoryInitCreatesNonexistingBasePath() {
        // Arrange
        final Properties properties = new Properties();
        properties.put(FileSystemTemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BASEDIR, tempFolder.toString());

        FileSystemTemplateRepository repository = new FileSystemTemplateRepository();
        try {
            repository.init(properties);

            assertTrue(Files.exists(Path.of(tempFolder.toUri())));
        } catch (TemplateServiceConfigurationException e) {
            fail();
        }

    }

    @Test
    public void testTemplateRepositoryInitHandlesInvalidBasePath() {
        // Arrange
        final Properties properties = new Properties();
        properties.put(FileSystemTemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BASEDIR, "/invalid?&`/dir");
        FileSystemTemplateRepository repository = new FileSystemTemplateRepository();

        // Act & Assert
        TemplateServiceException exception = assertThrows(TemplateServiceConfigurationException.class, () ->
                repository.init(properties));
    }

    @Test
    public void testGetTemplatesWithoutPagesShallReturnAll() {
        // Arrange
        final Properties properties = new Properties();
        properties.put(FileSystemTemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BASEDIR, tempFolder.toString());
        FileSystemTemplateRepository repository = new FileSystemTemplateRepository();

        // Act & Assert
        try {
            repository.init(properties);
            final Page<TemplateDocument> items = repository.getTemplates(null);

            assertNotNull(items);

        } catch (TemplateServiceException e) {
            fail();
        }

    }

    @Test
    public void testGetTemplatesWithNoPagedQueryShallReturnAll() throws IOException {
        // Arrange
        final Properties properties = new Properties();
        properties.put(FileSystemTemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BASEDIR, tempFolder.toString());
        FileSystemTemplateRepository repository = new FileSystemTemplateRepository();

        final Pageable pageable = new Pageable();
        pageable.setPaged(false); // No paging

        // Act & Assert
        try {
            repository.init(properties);
            final Page<TemplateDocument> items = repository.getTemplates(pageable);

            assertNotNull(items);
            assertEquals(0, items.getData().size()); // Expecting no templates since the directory is empty

        } catch (final TemplateServiceException e) {
            fail();
        }
    }

    @Test
    public void testGetTemplateByIdShallReturnTemplate() {
        // Arrange
        final String basePath = "target/test-classes/templates/unittests";
        final Properties properties = new Properties();
        properties.put(FileSystemTemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BASEDIR, basePath);
        FileSystemTemplateRepository repository = new FileSystemTemplateRepository();

        // Act & Assert
        try {
            repository.init(properties);
            final Optional<TemplateDocument> template = repository
                    .getTemplateDocumentById("docx/contract_v09_en.docx", null, true);

            assertTrue(template.isPresent());

            final TemplateDocument doc = template.get();
            assertEquals("docx/contract_v09_en.docx", doc.getTemplateName());
            assertTrue(doc.getBinary().length > 0);

        } catch (final TemplateServiceException e) {
            fail();
        }
    }


    @Test
    public void testGetTemplateByWithInvalidTemplateNameShallReturnOptionalEmpty() {
        // Arrange
        final String basePath = "target/test-classes/templates/unittests";
        final Properties properties = new Properties();
        properties.put(FileSystemTemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BASEDIR, basePath);
        FileSystemTemplateRepository repository = new FileSystemTemplateRepository();

        // Act & Assert
        try {
            repository.init(properties);
            final Optional<TemplateDocument> template = repository
                    .getTemplateDocumentById("docx/i_dont_exist.docx", null, true);

            assertFalse(template.isPresent());

        } catch (final TemplateServiceException e) {
            fail();
        }
    }
}
