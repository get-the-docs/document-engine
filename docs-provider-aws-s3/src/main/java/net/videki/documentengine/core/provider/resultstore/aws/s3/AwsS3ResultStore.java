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
import net.videki.documentengine.core.documentstructure.descriptors.StoredResultDocumentStatus;
import net.videki.documentengine.core.provider.aws.s3.S3ClientFactory;
import net.videki.documentengine.core.provider.aws.s3.S3Repository;
import net.videki.documentengine.core.provider.resultstore.ResultStore;
import net.videki.documentengine.core.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AWS S3 based template repository implementation.
 * @author Levente Ban
 */
@Slf4j
public class AwsS3ResultStore implements ResultStore, S3Repository {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3ResultStore.class);

    /**
     * AWS S3 bucket name configuration property key in the system properties (see document-engine.properties).
     */
    private static final String RESULTSTORE_PROVIDER_BUCKET_NAME = "repository.result.provider.aws.s3.bucketname";
    /**
     * AWS S3 bucket region configuration property key in the system properties (see document-engine.properties).
     */
    private static final String RESULTSTORE_REPOSITORY_PROVIDER_REGION = "repository.result.provider.aws.s3.region";
    /**
     * AWS S3 bucket name configuration property key in the system properties (see document-engine.properties).
     */
    public static final String RESULTSTORE_PROVIDER_BUCKET_NAME_ENV_VAR = "GETTHEDOCS_REPO_RESULT_AWS_S3_BUCKETNAME";
    /**
     * AWS S3 object prefix in the bucket (see document-engine.properties).
     */
    private static final String RESULTSTORE_PROVIDER_BUCKET_PREFIX = "repository.result.provider.aws.s3.prefix";
    /**
     * AWS S3 object flag file name for transaction existence.
     */
    static final String RESULTSTORE_PROVIDER_FLAGFILE = ".touchfile.txt";

    /**
     * The bucket name.
     */
    private String bucketName;
    /**
     * The bucket region.
     */
    private String region;
    /**
     * The prefix in the given bucket.
     */
    private String prefix;

    /**
     * Entry point for repo initialization.
     * @param props the system properties (see document-engine.properties)
     */
    @Override
    public void init(final Properties props) throws TemplateServiceConfigurationException {
        if (props == null) {
            throw new TemplateServiceConfigurationException("e1d5bc55-0632-41d4-855f-9432938a1db6",
                    "Null or invalid template properties caught.");
        }

        this.bucketName = (String) props.get(RESULTSTORE_PROVIDER_BUCKET_NAME);
        this.region = (String) props.get(RESULTSTORE_REPOSITORY_PROVIDER_REGION);
        final String bucketNameFromEnv = System.getenv(RESULTSTORE_PROVIDER_BUCKET_NAME_ENV_VAR);
        if (bucketNameFromEnv != null) {
            this.bucketName = bucketNameFromEnv;
        }
        this.prefix = (String) props.get(RESULTSTORE_PROVIDER_BUCKET_PREFIX);

        initResultStoreDir();
    }

    private void initResultStoreDir() throws TemplateServiceConfigurationException {
        LOGGER.info("Checking result store access...");
        try {
            final S3Client s3 = S3ClientFactory.getS3Client(this.bucketName, this.region);

            final ListObjectsV2Response response =
                    s3.listObjectsV2(
                            ListObjectsV2Request.builder()
                                    .bucket(this.bucketName)
                                    .prefix(this.prefix)
                                    .build());

            LOGGER.info("Result store available: AWS S3.");
        } catch (final IllegalArgumentException | NoSuchBucketException | NoSuchKeyException e) {
            final String msg = "Invalid bucket name or result document path.";
            LOGGER.error("Could not initialize result store: [{}]." + msg, this.bucketName, e);
            throw new TemplateServiceConfigurationException("ce451961-f4e8-4b8b-8d02-382aea7b9dd5", msg);
        } catch (final S3Exception e) {
            final String msg = "Error querying the given S3 bucket.";
            LOGGER.error("Could not initialize result store. " + msg, e);
            throw new TemplateServiceConfigurationException("dfd21f3f-9366-4f71-81cb-3f96bd5b713a", msg);
        }
    }

    /**
     * Registers a transaction in the result store to let it appear in the queries.
     * @param transactionId the transaction id
     * @return success flag.
     */
    @Override
    public boolean registerTransaction(final String transactionId) {
        String transactionDir = transactionId;
        if (transactionDir == null) {
            transactionDir = "";
        }
        final String resultDir = this.prefix + "/" + transactionDir;

        final String pathToFile = this.prefix + "/"
                + transactionDir + (!transactionDir.equals("") ? "/" : "") + RESULTSTORE_PROVIDER_FLAGFILE;
        boolean actSuccessFlag = false;
        try {
            final S3Client s3 = S3ClientFactory.getS3Client(this.bucketName, this.region);

            final ListObjectsV2Response transactionCheckResponse =
                    s3.listObjectsV2(
                            ListObjectsV2Request.builder()
                                    .bucket(this.bucketName)
                                    .prefix(pathToFile)
                                    .build());

            if (transactionCheckResponse.contents().size() > 0) {
                LOGGER.debug("The transaction dir was already present:" + resultDir);

                return false;
            }

            final PutObjectResponse putObjectResponse = s3.putObject(PutObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(pathToFile)
                    .build(), RequestBody.fromBytes(transactionDir.getBytes()));

            if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
                actSuccessFlag = true;
            }
        } catch (final S3Exception e) {
            LOGGER.error("Error registering the transaction.", e);
        }

        return actSuccessFlag;
    }

    /**
     * Entry point to save a single result document in the repository.
     *
     * @param resultDocument the result document descriptor.
     * @return the descriptor with the storage results (related transaction, file
     *         location, etc.).
     */
    @Override
    public StoredResultDocument save(final ResultDocument resultDocument) {
        if (resultDocument == null || resultDocument.getContent() == null) {
            throw new TemplateProcessException("99c2fc57-1e79-4568-ba78-27197de8ae43", "No result caught.");
        }

        String transactionDir = resultDocument.getTransactionId();
        if (transactionDir == null) {
            transactionDir = "";
        }
        final String pathToFile = this.prefix + "/" + transactionDir + (!transactionDir.equals("") ? "/" : "")
                + resultDocument.getFileName();
        boolean actSuccessFlag = false;
        try {
            final S3Client s3 = S3ClientFactory.getS3Client(this.bucketName, this.region);

            final PutObjectResponse putObjectResponse = s3.putObject(PutObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(pathToFile)
                    .build(), RequestBody.fromBytes(((ByteArrayOutputStream) resultDocument.getContent()).toByteArray()));

            if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
                actSuccessFlag = true;
            }
        } catch (final S3Exception e) {
            LOGGER.error("Error saving the result file.", e);
        }

        final var result = new StoredResultDocument(resultDocument.getTransactionId(), resultDocument.getFileName(), actSuccessFlag);
        result.setStatus(StoredResultDocumentStatus.AVAILABLE);

        return result;
    }

    /**
     * Entryp point to save a multi document result (the results of a document
     * structure generation).
     *
     * @param generationResult the generation result container.
     * @return the storage result descriptor.
     */
    @Override
    public StoredGenerationResult save(final GenerationResult generationResult) {
        final List<StoredResultDocument> storedResults = new LinkedList<>();

        if (generationResult == null) {
            throw new TemplateProcessException("46c0f44f-18d0-4237-b864-cbd95c7d12f9", "No result caught.");
        }
        final String generationResultTransactionId = generationResult.getTransactionId();

        for (final ResultDocument actResult : generationResult.getResults()) {
            if (actResult == null) {
                throw new TemplateProcessException("6a2ef445-f17b-41dd-9191-b67256cc8aad", "No result caught.");
            }
            if (actResult.getContent() != null) {
                final ResultDocument effectiveResultDocument = actResult.clone();
                effectiveResultDocument.setTransactionId(
                        generationResultTransactionId == null ? actResult.getTransactionId() : generationResultTransactionId);

                storedResults.add(save(actResult));
            } else {
                LOGGER.warn(
                        "Result document or part not generated - possible due errors. "
                                + "TransactionId: {}, fileName: {}",
                        actResult.getTransactionId(), actResult.getFileName());
            }
        }

        LOGGER.info("Done.");

        final StoredGenerationResult result = new StoredGenerationResult(storedResults);
        if (generationResultTransactionId != null) {
            result.setTransactionId(generationResultTransactionId);
        }

        return result;
    }

    /**
     * Returns a given result document and/or its binary if requested. For document
     * structures query the generation result for the transaction id, then request
     * its result documents.
     *
     * @param transactionId the requested transaction, if found.
     * @throws TemplateServiceException thrown in case of query error.
     * @return the template document.
     */
    @Override
    public Optional<StoredGenerationResult> getGenerationResultByTransactionId(final String transactionId)
            throws TemplateServiceException {
        try {

            final String basePath = this.prefix + "/" + transactionId;
            final String delimitedPrefix = this.prefix.endsWith("/") ? this.prefix : this.prefix + "/";

            final S3Client s3 = S3ClientFactory.getS3Client(this.bucketName, this.region);

            final ListObjectsV2Response resultDocsResponse =
                    s3.listObjectsV2(
                            ListObjectsV2Request.builder()
                                    .bucket(this.bucketName)
                                    .prefix(basePath)
                                    .build());

            if (resultDocsResponse.contents().isEmpty()) {
                return Optional.empty();
            }


                final List<StoredResultDocument> items = resultDocsResponse.contents().stream()
                    .map(t -> t.toBuilder().key(t.key().replaceFirst(delimitedPrefix, "")).build())
                    .filter(t -> !RESULTSTORE_PROVIDER_FLAGFILE.equals(t.key()))
                    .sorted(Comparator.comparing(S3Object::key))
                    .map(t -> new StoredResultDocument(t.key(), true))
                    .collect(Collectors.toList());

                final StoredGenerationResult result = new StoredGenerationResult(items);
                result.setTransactionId(transactionId);

                return Optional.of(result);
        } catch (final S3Exception e) {
            final String msg = String.format("Error retrieving the result documents - baseDir: [%s]", this.prefix);
            LOGGER.error(msg, e);

            throw new TemplateServiceException("a6d79a61-60a9-44ef-99fc-c0a21ca85092", msg);
        }
    }

    /**
     * Returns a given result document and/or its binary if requested. For document
     * structures query
     *
     * @param transactionId the requested transaction, if found.
     * @throws TemplateServiceException thrown in case of query error.
     * @return the template document.
     */
    @Override
    public Optional<StoredResultDocument> getResultDocumentByTransactionId(final String transactionId,
                                                                           final String resultDocument,
                                                                           final boolean withBinary) throws TemplateServiceException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getResultDocumentByTransactionId - {}/{}. binary: {}", transactionId, resultDocument,
                    withBinary);
        }

        StoredResultDocument result;
        try (final InputStream is = getDocumentBinary(transactionId, resultDocument)) {

            if (is != null) {
                if (withBinary) {
                    result = new StoredResultDocument(resultDocument, true, is.readAllBytes());
                } else {
                    result = new StoredResultDocument(resultDocument, true, null);
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("getResultDocumentByTransactionId - result document not found: {}/{}", transactionId,
                            resultDocument);
                }
                throw new ResultNotFoundException("bd832ab8-9f4c-4128-89c0-d6cad5fe0ded",
                        String.format("Result document not found: %s/%s", transactionId, resultDocument));
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("getResultDocumentByTransactionId end - {}/{}. result: {}", transactionId, resultDocument,
                        result);
            }
            return Optional.of(result);

        } catch (final ResultNotFoundException e) {
            return Optional.empty();
        } catch (final IOException e) {
            throw new TemplateServiceException("4a056c03-d8d1-4edf-9764-199e8ff763ba",
                    String.format("Error reading the document - transactionId: %s, resultDocument: %s.", transactionId,
                            resultDocument));
        }

    }

    /**
     * Returns a result document for a given transaction and name.
     *
     * @param transactionId  the transaction id.
     * @param resultDocument the document name.
     * @return the document as stream if found.
     */
    public InputStream getDocumentBinary(final String transactionId, final String resultDocument) {
        InputStream result;

        final String pathToFile = this.prefix + "/" + transactionId + "/" + resultDocument;
        try {
            final S3Client s3 = S3ClientFactory.getS3Client(this.bucketName, this.region);

            result = s3.getObject(GetObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(pathToFile)
                    .build());
        } catch (final S3Exception e) {
            result = null;
        }

        if (result == null) {
            LOGGER.error("Result document not found. File: {}. ", pathToFile);
        } else {
            LOGGER.debug("Result document found. File: {}. ", pathToFile);
        }

        return result;

    }

    public String getBucketName() {
        return this.bucketName;
    }

    public String getPrefix() {
        return this.prefix;
    }

}
