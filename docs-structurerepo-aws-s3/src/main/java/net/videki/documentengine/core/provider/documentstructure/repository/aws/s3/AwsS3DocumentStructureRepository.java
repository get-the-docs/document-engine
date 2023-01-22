package net.videki.documentengine.core.provider.documentstructure.repository.aws.s3;

/*-
 * #%L
 * docs-structurerepo-aws-s3
 * %%
 * Copyright (C) 2022 Levente Ban
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
import net.videki.documentengine.core.documentstructure.DocumentStructure;
import net.videki.documentengine.core.provider.documentstructure.DocumentStructureRepository;
import net.videki.documentengine.core.provider.documentstructure.builder.DocumentStructureBuilder;
import net.videki.documentengine.core.provider.documentstructure.builder.yaml.YmlDocStructureBuilder;
import net.videki.documentengine.core.provider.persistence.Page;
import net.videki.documentengine.core.provider.persistence.Pageable;
import net.videki.documentengine.core.service.exception.TemplateProcessException;
import net.videki.documentengine.core.service.exception.TemplateServiceConfigurationException;
import net.videki.documentengine.core.service.exception.TemplateServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AWS S3 based document structure repository implementation.
 * @author Levente Ban
 */
@Slf4j
public class AwsS3DocumentStructureRepository implements DocumentStructureRepository {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3DocumentStructureRepository.class);

    /**
     * AWS S3 bucket name configuration property key in the system properties (see document-engine.properties).
     */
    private static final String DOCSTRUCTURE_REPOSITORY_PROVIDER_BUCKET_NAME = "repository.documentstructure.provider.aws.s3.bucketname";
    /**
     * AWS S3 bucket name configuration property key in the system properties (see document-engine.properties).
     */
    public static final String DOCSTRUCTURE_REPOSITORY_PROVIDER_BUCKET_NAME_ENV_VAR = "GETTHEDOCS_REPO_DOCSTRUCTURE_AWS_S3_BUCKETNAME";
    /**
     * AWS S3 object prefix in the bucket (see document-engine.properties).
     */
    private static final String DOCSTRUCTURE_REPOSITORY_PROVIDER_BUCKET_PREFIX = "repository.documentstructure.provider.aws.s3.prefix";

    /**
     * The configured document structure builder.
     */
    private DocumentStructureBuilder documentStructureBuilder;
    /**
     * The bucket name.
     */
    private String bucketName;
    /**
     * The prefix in the given bucket.
     */
    private String prefix;

    /**
     * Initializes the repository based on the system properties caught.
     *
     * @param props the system properties (see document-engine.properties)
     * @throws TemplateServiceConfigurationException thrown in case of configuration
     *                                               errors.
     */
    @Override
    public void init(final Properties props) throws TemplateServiceConfigurationException {
        if (props == null) {
            throw new TemplateServiceConfigurationException("36ed4a31-b887-4ce0-b132-f46b1c904a12",
                    "Null or invalid template properties caught.");
        }

        this.bucketName = (String) props.get(DOCSTRUCTURE_REPOSITORY_PROVIDER_BUCKET_NAME);
        final String bucketNameFromEnv = System.getenv(DOCSTRUCTURE_REPOSITORY_PROVIDER_BUCKET_NAME_ENV_VAR);
        if (bucketNameFromEnv != null) {
            this.bucketName = bucketNameFromEnv;
        }
        this.prefix = (String) props.get(DOCSTRUCTURE_REPOSITORY_PROVIDER_BUCKET_PREFIX);

        LOGGER.info("Checking document structure repository access...");
        try {
            final S3Client s3 = S3ClientFactory.getS3Client(this.bucketName);

            final ListObjectsV2Response response =
                    s3.listObjectsV2(
                            ListObjectsV2Request.builder()
                                    .bucket(this.bucketName)
                                    .prefix(this.prefix)
                                    .build());

            this.documentStructureBuilder = loadDocumentStructureBuilder(props);

            LOGGER.info("Document structure repository available: AWS S3/{}.", response.name());
        } catch (final NoSuchBucketException | NoSuchKeyException e) {
            final String msg = "Invalid bucket name or template path.";
            LOGGER.error("Could not initialize template repository. " + msg, e);
            throw new TemplateServiceConfigurationException("cbdfa022-115b-4801-9407-e8cc22fcf4a2", msg);
        } catch (final S3Exception e) {
            final String msg = "Error querying the given S3 bucket.";
            LOGGER.error("Could not initialize template repository. " + msg, e);
            throw new TemplateServiceConfigurationException("f8bb679a-3b91-4ba5-a890-c23e24ccfd7c", msg);
        }

    }

    /**
     * Returns the actual document structure list, if the repository provides this
     * feature.
     *
     * @param page the requested page to return
     * @throws TemplateServiceException thrown in case of query error
     * @return the document structure list
     */
    @Override
    public Page<DocumentStructure> getDocumentStructures(final Pageable page) throws TemplateServiceException {
        if (page == null) {
            throw new TemplateProcessException("cb8807ce-5183-4247-bb0b-a1bc16b4eb86",
                    "Null page object caught.");
        }

        try {
            final Page<DocumentStructure> result = new Page<>();

            final S3Client s3 = S3ClientFactory.getS3Client(this.bucketName);
            final String delimitedPrefix = this.prefix.endsWith("/") ? this.prefix : this.prefix + "/";

            final ListObjectsV2Request listObjectsRequest =
                    ListObjectsV2Request.builder()
                            .bucket(this.bucketName)
                            .prefix(delimitedPrefix)
                            .delimiter("/")
                            .build();

            final ListObjectsV2Response listObjectsResponse = s3.listObjectsV2(listObjectsRequest);

            final List<DocumentStructure> items = listObjectsResponse.contents().stream()
                    .map(t -> t.toBuilder().key(t.key().replaceFirst(delimitedPrefix, "")).build())
                    .sorted(Comparator.comparing(S3Object::key))
                    .map(t -> new DocumentStructure(t.key()))
                    .collect(Collectors.toList());

            if (page.isPaged()) {

                final int endIndex = Math.min(page.getOffset() + page.getSize(), Math.max(items.size() - 1, 0));

                if (page.getOffset() < items.size() && endIndex < items.size()) {
                    result.setData(items.subList(page.getOffset(), endIndex + 1));
                }
                result.setNumber(page.getPage());
                result.setSize(result.getData().size());
                result.setTotalElements((long) items.size());
                result.setTotalPages((int) Math.ceil(Float.valueOf(result.getTotalElements()) / page.getSize()));
            } else {
                result.setData(items);
            }
            return result;

        } catch (final S3Exception e) {
            final String msg = String.format("Error retrieving the document structure list - baseDir: [%s]",
                    this.prefix);
            LOGGER.error(msg, e);

            throw new TemplateServiceException("61c681ec-6451-435a-b82e-90ac9faf9340", msg);
        }
    }

    /**
     * Returns a document structure by its id in the current repository (a
     * descriptor named as the document structure id).
     *
     * @param documentStructureFile the document structure id (is equal to the
     *                              filename).
     * @return the document structure descriptor if the provided id was found.
     * @throws TemplateServiceConfigurationException thrown in case of repository
     *                                               configuration errors.
     */
    @Override
    public DocumentStructure getDocumentStructure(final String documentStructureFile)
            throws TemplateServiceConfigurationException {

        if (documentStructureFile == null) {
            final String msg = String.format("DocumentStructure id not provided. ");
            LOGGER.error(msg);

            throw new TemplateProcessException("bf61ec46-ef0a-44bf-bf4f-64cad7b34b5d", msg);
        }

        InputStream dsAsStream;
        try {
            final String pathToFile = this.prefix + "/" + documentStructureFile;

            final S3Client s3 = S3ClientFactory.getS3Client(this.bucketName);

            dsAsStream = s3.getObject(GetObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(pathToFile)
                    .build());

        } catch (final S3Exception e) {
            dsAsStream = null;
        }

        if (dsAsStream == null) {
            final String msg = String.format("DocumentStructure not found. File: %s. ", documentStructureFile);
            LOGGER.error(msg);

            throw new TemplateProcessException("8bfba3ce-f1f3-48cd-8995-71256a7628f1", msg);
        } else {
            LOGGER.debug("DocumentStructure found. File: {}. ", documentStructureFile);

            return this.documentStructureBuilder.build(dsAsStream);
        }
    }

    /**
     * Factory method to initialize the descriptor builder implementation.
     *
     * @param props the system properties (see document-engine.properties)
     * @return the builder instance
     */
    protected DocumentStructureBuilder loadDocumentStructureBuilder(final Properties props) {
        DocumentStructureBuilder documentStructureBuilder = new YmlDocStructureBuilder();

        String repositoryProvider = "<Not configured or could not read properties file>";
        try {
            repositoryProvider = (String) props.get(DOCUMENT_STRUCTURE_BUILDER);

            if (repositoryProvider != null) {
                documentStructureBuilder = (DocumentStructureBuilder) this.getClass().getClassLoader()
                        .loadClass(repositoryProvider).getDeclaredConstructor().newInstance();
            } else {
                final String msg = "Document structure builder not specified, "
                        + "using built-in YmlDocStructureBuilder.";
                LOGGER.info(msg);
            }
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
                 | InvocationTargetException e) {
            final String msg = String.format(
                    "Error loading document structure builder: %s, " + "using built-in YmlDocStructureBuilder.",
                    repositoryProvider);
            LOGGER.error(msg, e);
        }

        return documentStructureBuilder;
    }

}
