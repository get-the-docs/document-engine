package net.videki.documentengine.core.provider.resultstore.aws.s3;

/*-
 * #%L
 * docs-provider-aws-s3
 * %%
 * Copyright (C) 2023 Levente Ban
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

import lombok.extern.slf4j.Slf4j;
import net.videki.documentengine.core.documentstructure.GenerationResult;
import net.videki.documentengine.core.documentstructure.ResultDocument;
import net.videki.documentengine.core.documentstructure.StoredGenerationResult;
import net.videki.documentengine.core.documentstructure.StoredResultDocument;
import net.videki.documentengine.core.provider.aws.s3.S3ClientFactory;
import net.videki.documentengine.core.provider.resultstore.ResultStore;
import net.videki.documentengine.core.service.TemplateServiceRegistry;
import net.videki.documentengine.core.service.exception.TemplateProcessException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@Slf4j
public class AwsS3ResultStoreTest {

    private static ResultStore resultStore;
    private static String TESTBUCKET;
    private static final String BASEDIR;

    private static final String TEST_TRANSACTION_PREFIX = "AwsS3ResultStoreTest-";
    static {
        BASEDIR = "testfiles/results/";
    }

    @BeforeClass
    public static void initialize() {

        resultStore = TemplateServiceRegistry.getConfiguration().getResultStore();
        TESTBUCKET = ((AwsS3ResultStore)resultStore).getBucketName();

        try {
            final S3Client s3 = S3ClientFactory.getS3Client(Region.EU_CENTRAL_1);

            final String tran1 = "c52b7d10-7446-4e0f-bcc1-3b871fcc7bcb";
            final String tran2 = "21a281ca-9805-416b-ae3d-1a7134580262";
            final String tran3 = "e1bf1f13-7238-4b1a-8874-9bdcf67d506d";

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(TESTBUCKET)
                            .key(BASEDIR + tran1 + "/" + AwsS3ResultStore.RESULTSTORE_PROVIDER_FLAGFILE)
                            .build(), RequestBody.fromBytes(tran1.getBytes()));

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(TESTBUCKET)
                            .key(BASEDIR + tran1 + "/" + "supportedfile2.docx")
                            .build(), RequestBody.fromBytes("Test content - supported file 2.".getBytes()));

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(TESTBUCKET)
                            .key(BASEDIR + tran2 + "/" + AwsS3ResultStore.RESULTSTORE_PROVIDER_FLAGFILE)
                            .build(), RequestBody.fromBytes(tran2.getBytes()));

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(TESTBUCKET)
                            .key(BASEDIR + tran1 + "/" + "anotherfile.docx")
                            .build(), RequestBody.fromBytes("Test content - supported file for tran2.".getBytes()));

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(TESTBUCKET)
                            .key(BASEDIR + tran3 + "/" + AwsS3ResultStore.RESULTSTORE_PROVIDER_FLAGFILE)
                            .build(), RequestBody.fromBytes(tran3.getBytes()));

            log.info("Test data created successfully.");
        } catch (final S3Exception e) {
            log.warn("Error deleting the test data: {}", e.getMessage());
        }
    }

    @AfterClass
    public static void teardown()	{

        try {
            final S3Client s3 = S3ClientFactory.getS3Client(TESTBUCKET, Region.EU_CENTRAL_1.toString());

            final ListObjectsV2Response testFiles = s3.listObjectsV2(ListObjectsV2Request.builder()
                    .bucket(TESTBUCKET)
                    .prefix(BASEDIR + TEST_TRANSACTION_PREFIX + "*")
                    .build());

            final List<ObjectIdentifier> listObjects = new ArrayList<>(testFiles.keyCount());
            for (var testFile : testFiles.contents()) {
                listObjects.add(ObjectIdentifier.builder().key(testFile.key()).build());
            }

            final DeleteObjectsResponse deleteObjectResponse = s3.deleteObjects(DeleteObjectsRequest.builder()
                    .bucket(TESTBUCKET)
                    .delete(Delete.builder()
                            .objects(listObjects)
                            .build())
                    .build()
            );

            log.info("Deleted test files: {}/{}.", testFiles.keyCount(), deleteObjectResponse.deleted().size());
        } catch (final S3Exception e) {
            log.warn("Error deleting the test data: {}", e.getMessage());
        }
    }

    @Test
    public void registerTransactionNonExising() {
        final String transactionId = TEST_TRANSACTION_PREFIX + UUID.randomUUID();

        final boolean successFlag = resultStore.registerTransaction(transactionId);

        assertTrue(successFlag);
    }

    @Test
    public void registerTransactionExising() {
        final String transactionId = TEST_TRANSACTION_PREFIX + UUID.randomUUID();

        final boolean successFlagForFirstCheckin = resultStore.registerTransaction(transactionId);
        final boolean successFlagForSecondTry = resultStore.registerTransaction(transactionId);

        assertTrue(successFlagForFirstCheckin);
        assertFalse(successFlagForSecondTry);
    }

    @Test
    public void storeResultDocumentWithNullInput() {
        final String transactionId = TEST_TRANSACTION_PREFIX + UUID.randomUUID();

        try(final OutputStream data = new ByteArrayOutputStream()) {
            data.write("Test result generation content".getBytes());

            final ResultDocument resultDocument = new ResultDocument(transactionId, "generatedFile.docx", null);
            final StoredResultDocument storedResult = resultStore.save(resultDocument);

            assertEquals(transactionId, resultDocument.getTransactionId());
            assertFalse(storedResult.isGenerated());

        } catch (final TemplateProcessException e) {
            assertEquals("99c2fc57-1e79-4568-ba78-27197de8ae43", e.getCode());
        } catch (final IOException e) {
            fail();
        }
    }

    @Test
    public void storeResultDocumentWithInput() {
        final String transactionId = TEST_TRANSACTION_PREFIX + UUID.randomUUID();

        try(final OutputStream data = new ByteArrayOutputStream()) {
            data.write("Test result generation content".getBytes());

            final ResultDocument resultDocument = new ResultDocument(transactionId, "generatedFile.docx", data);
            final StoredResultDocument storedResult = resultStore.save(resultDocument);

            assertEquals(transactionId, resultDocument.getTransactionId());
            assertTrue(storedResult.isGenerated());

        } catch (final IOException e) {
            fail();
        }
    }

    @Test
    public void storeGenerationResultWithNullInput() {

        try {
            resultStore.save((GenerationResult) null);
        } catch (final TemplateProcessException e) {
            assertEquals("46c0f44f-18d0-4237-b864-cbd95c7d12f9", e.getCode());
        }
    }

    @Test
    public void storeGenerationResultWithInput() {
        final String transactionId = TEST_TRANSACTION_PREFIX + UUID.randomUUID();
        final int docCount = 3;

        final List<ResultDocument> resultDocs = new ArrayList<>(docCount);
        for (int i = 0; i < docCount; i++) {
            try (final OutputStream data = new ByteArrayOutputStream()) {
                data.write(("Test result generation content - [" + i + "]").getBytes());

                resultDocs.add(new ResultDocument(transactionId, "generatedFile.docx", data));

            } catch (final IOException e) {
                fail();
            }
        }

        final GenerationResult generationResult = new GenerationResult(resultDocs);
        generationResult.setTransactionId(transactionId);

        final StoredGenerationResult storedResult = resultStore.save(generationResult);

        final Boolean[] allDocsWereGenerated = new Boolean[docCount];
        Arrays.fill(allDocsWereGenerated, Boolean.TRUE);

        assertEquals(transactionId, storedResult.getTransactionId());
        assertArrayEquals(allDocsWereGenerated, storedResult.getResults().stream().map(StoredResultDocument::isGenerated).toArray());

    }

    @Test
    public void storeGenerationResultWithInputWithoutTransactionId() {
        final String transactionId = TEST_TRANSACTION_PREFIX + UUID.randomUUID();
        final int docCount = 3;

        final List<ResultDocument> resultDocs = new ArrayList<>(docCount);
        for (int i = 0; i < docCount; i++) {
            try (final OutputStream data = new ByteArrayOutputStream()) {
                data.write(("Test result generation content - [" + i + "]").getBytes());

                resultDocs.add(new ResultDocument(transactionId, "generatedFile.docx", data));

            } catch (final IOException e) {
                fail();
            }
        }

        final GenerationResult generationResult = new GenerationResult(resultDocs);
        generationResult.setTransactionId(null);

        final StoredGenerationResult storedResult = resultStore.save(generationResult);

        final Boolean[] allDocsWereGenerated = new Boolean[docCount];
        Arrays.fill(allDocsWereGenerated, Boolean.TRUE);

        assertNotNull(storedResult.getTransactionId());
        assertArrayEquals(allDocsWereGenerated, storedResult.getResults().stream().map(StoredResultDocument::isGenerated).toArray());

    }

}
