package org.getthedocs.documentengine.core.provider.resultstore.filesystem;

/*-
 * #%L
 * docs-core
 * %%
 * Copyright (C) 2025 Levente Ban
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

import org.getthedocs.documentengine.core.documentstructure.GenerationResult;
import org.getthedocs.documentengine.core.documentstructure.ResultDocument;
import org.getthedocs.documentengine.core.documentstructure.StoredGenerationResult;
import org.getthedocs.documentengine.core.documentstructure.StoredResultDocument;
import org.getthedocs.documentengine.core.provider.resultstore.ResultStore;
import org.getthedocs.documentengine.core.service.exception.TemplateProcessException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

import static org.junit.Assert.*;

public class FileSystemResultStoreTest {

    @TempDir
    static Path tempFolder;

    @Test
    public void saveNullInputDocumentResultTest() {
        final ResultStore resultStore = new FileSystemResultStore();

        try {
            resultStore.save((ResultDocument) null);
        } catch (final TemplateProcessException e) {
            if (!"cb05a816-dea2-4498-823a-33fb4ece1565".equals(e.getCode())) {
                fail();
            }

        }
    }

    @Test
    public void saveNullInputGenerationResultTest() {
        final ResultStore resultStore = new FileSystemResultStore();

        try {
            resultStore.save((GenerationResult) null);
        } catch (final TemplateProcessException e) {
            if (!"0d383b14-c3ef-430d-9007-3add27e086d8".equals(e.getCode())) {
                fail();
            }

        }
    }

    @Test
    public void testRegisterTransactionIdAndGetNonExistingShouldReturnEmpty() {
        final ResultStore resultStore = new FileSystemResultStore();
        final Properties properties = new Properties();
        properties.put(FileSystemResultStore.RESULT_REPOSITORY_PROVIDER_BASEDIR, "target/resultstore");

        final String transactionId = "testTransactionId";

        try {
            resultStore.init(properties);

            resultStore.registerTransaction(transactionId);

            final Optional<StoredGenerationResult> result = resultStore
                    .getGenerationResultByTransactionId("someOtherTransactionId");

            assertFalse(result.isPresent());

        } catch (final TemplateServiceException e) {
            fail();
        }
    }

    @Test
    public void testRegisterTransactionIdShouldReturnStoredGenerationResult() {
        final ResultStore resultStore = new FileSystemResultStore();
        final Properties properties = new Properties();
        properties.put(FileSystemResultStore.RESULT_REPOSITORY_PROVIDER_BASEDIR, tempFolder.toString());

        final String transactionId = "testTransactionId";

        try {
            resultStore.init(properties);

            resultStore.registerTransaction(transactionId);

            final Optional<StoredGenerationResult> result = resultStore
                    .getGenerationResultByTransactionId(transactionId);

            assertTrue(result.isPresent());

        } catch (final TemplateServiceException e) {
            fail();
        }
    }

    @Test
    public void testGetResultDocumentByTransactionId_withBinaryContent() {
        final ResultStore resultStore = new FileSystemResultStore();
        final Properties properties = new Properties();
        properties.put(FileSystemResultStore.RESULT_REPOSITORY_PROVIDER_BASEDIR, tempFolder.toString());

        final String transactionId = "testTransactionId";
        final String fileName = "testFile.txt";
        final byte[] fileContentBytes = "Test content".getBytes();

        try (final ByteArrayOutputStream fileContent = new ByteArrayOutputStream(fileContentBytes.length)) {
            // Write the test result content to the ByteArrayOutputStream
            fileContent.write(fileContentBytes);
            fileContent.flush();

            resultStore.init(properties);

            // Save the ResultDocument
            resultStore.registerTransaction(transactionId);
            ResultDocument resultDocument = new ResultDocument(transactionId, fileName, fileContent);
            resultStore.save(resultDocument);

            // Retrieve the ResultDocument with binary content
            Optional<StoredResultDocument> retrievedDocument = resultStore.getResultDocumentByTransactionId(transactionId, fileName, true);

            assertTrue(retrievedDocument.isPresent());
            assertArrayEquals(fileContentBytes, retrievedDocument.get().getBinary());
        } catch (IOException | TemplateServiceException e) {
            fail();
        }
    }

    @Test
    public void testGetResultDocumentByTransactionId_withoutBinaryContent() {
        final ResultStore resultStore = new FileSystemResultStore();
        final Properties properties = new Properties();
        properties.put(FileSystemResultStore.RESULT_REPOSITORY_PROVIDER_BASEDIR, tempFolder.toString());

        final String transactionId = "testTransactionId";
        final String fileName = "testFile.txt";
        final byte[] fileContentBytes = "Test content".getBytes();

        try (final ByteArrayOutputStream fileContent = new ByteArrayOutputStream(fileContentBytes.length)) {
            // Write the test result content to the ByteArrayOutputStream
            fileContent.write(fileContentBytes);
            fileContent.flush();

            resultStore.init(properties);

            // Save the ResultDocument
            resultStore.registerTransaction(transactionId);
            ResultDocument resultDocument = new ResultDocument(transactionId, fileName, fileContent);
            resultStore.save(resultDocument);

            // Retrieve the ResultDocument without binary content
            Optional<StoredResultDocument> retrievedDocument = resultStore
                    .getResultDocumentByTransactionId(transactionId, fileName, false);

            assertTrue(retrievedDocument.isPresent());
            assertNull(retrievedDocument.get().getBinary());
        } catch (IOException | TemplateServiceException e) {
            fail();
        }
    }

    @Test
    public void testGetResultDocumentByTransactionId_nonExistentDocument() {
        final ResultStore resultStore = new FileSystemResultStore();
        final Properties properties = new Properties();
        properties.put(FileSystemResultStore.RESULT_REPOSITORY_PROVIDER_BASEDIR, tempFolder.toString());

        final String transactionId = "testTransactionId";
        final String nonExistentFileName = "nonExistentFile.txt";

        try {
            resultStore.init(properties);

            resultStore.registerTransaction(transactionId);

            // Attempt to retrieve a non-existent document
            Optional<StoredResultDocument> retrievedDocument = resultStore
                    .getResultDocumentByTransactionId(transactionId, nonExistentFileName, true);

            assertFalse(retrievedDocument.isPresent());
        } catch (TemplateServiceException e) {
            fail();
        }
    }
}
