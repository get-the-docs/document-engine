package org.getthedocs.documentengine.core.provider.documentstructure.repository.filesystem;

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

import org.getthedocs.documentengine.core.documentstructure.DocumentStructure;
import org.getthedocs.documentengine.core.provider.persistence.Page;
import org.getthedocs.documentengine.core.provider.persistence.Pageable;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileSystemDocumentStructureRepositoryTest {

    @TempDir
    static Path tempFolder;

    @Test
    public void testGetDocumentStructuresWithPagedRequest() throws Exception {
        Properties props = new Properties();
        props.put(FileSystemDocumentStructureRepository.DOCUMENT_STRUCTURE_PROVIDER_BASEDIR, tempFolder.toString());

        Path file1 = Files.createFile(tempFolder.resolve("doc1.json"));
        Path file2 = Files.createFile(tempFolder.resolve("doc2.json"));

        FileSystemDocumentStructureRepository repository = new FileSystemDocumentStructureRepository();
        repository.init(props);
        repository = Mockito.spy(repository);

        Pageable pageable = mock(Pageable.class);
        when(pageable.isPaged()).thenReturn(true);
        when(pageable.getOffset()).thenReturn(0);
        when(pageable.getSize()).thenReturn(10);
        when(pageable.getPage()).thenReturn(1);

        doReturn(tempFolder.toString()).when(repository).getBasePath();

        Page<DocumentStructure> result = repository.getDocumentStructures(pageable);

        assertEquals(10, result.getSize());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
        assertEquals("doc1.json", result.getData().get(0).getDocumentStructureId());

        Files.delete(file1);
        Files.delete(file2);
    }

    @Test
    public void testGetDocumentStructuresWithUnpagedRequest() throws Exception {
        Properties props = new Properties();
        props.put(FileSystemDocumentStructureRepository.DOCUMENT_STRUCTURE_PROVIDER_BASEDIR, tempFolder.toString());

        Path file1 = Files.createFile(tempFolder.resolve("doc1.json"));
        Path file2 = Files.createFile(tempFolder.resolve("doc2.json"));

        FileSystemDocumentStructureRepository repository = new FileSystemDocumentStructureRepository();
        repository.init(props);
        repository = Mockito.spy(repository);

        doReturn(tempFolder.toString()).when(repository).getBasePath();

        Page<DocumentStructure> result = repository.getDocumentStructures(null);

        assertEquals(2, result.getData().size());
        assertEquals("doc1.json", result.getData().get(0).getDocumentStructureId());
        assertEquals("doc2.json", result.getData().get(1).getDocumentStructureId());

        Files.delete(file1);
        Files.delete(file2);
    }

    @Test
    public void testGetDocumentStructuresThrowsExceptionOnIoError() {
        String basedir = "invalid/basedir";
        Properties props = new Properties();
        props.put(FileSystemDocumentStructureRepository.DOCUMENT_STRUCTURE_PROVIDER_BASEDIR, basedir);

        FileSystemDocumentStructureRepository repository = new FileSystemDocumentStructureRepository();
        try {
            repository.init(props);
        } catch (Exception e) {
            fail();
        }

        assertThrows(TemplateServiceException.class, () -> {
            repository.getDocumentStructures(null);
        });
    }
}
