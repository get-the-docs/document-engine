package org.getthedocs.documentengine.core.provider.templaterepository.filesystem;

import org.getthedocs.documentengine.core.provider.persistence.Page;
import org.getthedocs.documentengine.core.provider.persistence.Pageable;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.getthedocs.documentengine.core.template.descriptors.TemplateDocument;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileSystemTemplateRepositoryTest {

    @Test
    public void testTemplateRepositoryInitCreatesNonexistingBasePath() {
        // Arrange
        final String basePath = "target/newdir";
        final Properties properties = new Properties();
        properties.put(FileSystemTemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BASEDIR, basePath);

        FileSystemTemplateRepository repository = new FileSystemTemplateRepository();
        try {
            repository.init(properties);

            assertTrue(Files.exists(Path.of(basePath)));
        } catch (TemplateServiceConfigurationException e) {
            fail();
        }

    }

    @Test
    public void testTemplateRepositoryInitHandlesInvalidBasePath() throws IOException {
        // Arrange
        final Properties properties = new Properties();
        properties.put(FileSystemTemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BASEDIR, "/invalid?&`/dir");
        FileSystemTemplateRepository repository = new FileSystemTemplateRepository();

        // Act & Assert
        TemplateServiceException exception = assertThrows(TemplateServiceException.class, () ->
                repository.init(properties));
        assertEquals("9bd97feb-1387-40f3-bc82-01961e26bf5f", exception.getCode());
    }

    @Test
    public void testGetTemplatesWithoutPagesShallReturnAll() throws IOException {
        // Arrange
        final String basePath = "target/newdir";
        final Properties properties = new Properties();
        properties.put(FileSystemTemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BASEDIR, basePath);
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
        final String basePath = "target/newdir";
        final Properties properties = new Properties();
        properties.put(FileSystemTemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BASEDIR, basePath);
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

}