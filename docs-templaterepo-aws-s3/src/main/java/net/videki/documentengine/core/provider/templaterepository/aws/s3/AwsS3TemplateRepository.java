package net.videki.documentengine.core.provider.templaterepository.aws.s3;

/*-
 * #%L
 * docs-templaterepo-aws-s3
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
import net.videki.documentengine.core.provider.persistence.Page;
import net.videki.documentengine.core.provider.persistence.Pageable;
import net.videki.documentengine.core.provider.templaterepository.TemplateRepository;
import net.videki.documentengine.core.service.exception.TemplateNotFoundException;
import net.videki.documentengine.core.service.exception.TemplateProcessException;
import net.videki.documentengine.core.service.exception.TemplateServiceConfigurationException;
import net.videki.documentengine.core.service.exception.TemplateServiceException;
import net.videki.documentengine.core.template.descriptors.TemplateDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AWS S3 based template repository implementation.
 * @author Levente Ban
 */
@Slf4j
public class AwsS3TemplateRepository implements TemplateRepository {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3TemplateRepository.class);

    /**
     * AWS S3 bucket name configuration property key in the system properties (see document-engine.properties).
     */
    private static final String TEMPLATE_REPOSITORY_PROVIDER_BUCKET_NAME = "repository.template.provider.aws.s3.bucketname";
    /**
     * AWS S3 bucket name configuration property key in the system properties (see document-engine.properties).
     */
    private static final String TEMPLATE_REPOSITORY_PROVIDER_BUCKET_NAME_ENV_VAR = "GETTHEDOCS_REPO_TEMPLATE_AWS_S3_BUCKETNAME";
    /**
     * AWS S3 object prefix in the bucket (see document-engine.properties).
     */
    private static final String TEMPLATE_REPOSITORY_PROVIDER_BUCKET_PREFIX = "repository.template.provider.aws.s3.prefix";

    /**
     * The bucket name.
     */
    private String bucketName;
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
            throw new TemplateServiceConfigurationException("0944c01d-ab2d-4960-a7a4-0e143e82935d",
                    "Null or invalid template properties caught.");
        }

        this.bucketName = (String) props.get(TEMPLATE_REPOSITORY_PROVIDER_BUCKET_NAME);
        final String bucketNameFromEnv = System.getenv(TEMPLATE_REPOSITORY_PROVIDER_BUCKET_NAME_ENV_VAR);
        if (bucketNameFromEnv != null) {
            this.bucketName = bucketNameFromEnv;
        }
        this.prefix = (String) props.get(TEMPLATE_REPOSITORY_PROVIDER_BUCKET_PREFIX);

        initTemplateRepositoryDir();
    }

    private void initTemplateRepositoryDir() throws TemplateServiceConfigurationException {
        LOGGER.info("Checking template repository access...");
        try {
            final S3Client s3 = S3ClientFactory.getS3Client(this.bucketName);

            final ListObjectsV2Response response =
                    s3.listObjectsV2(
                            ListObjectsV2Request.builder()
                                    .bucket(this.bucketName)
                                    .prefix(this.prefix)
                                    .build());

            LOGGER.info("Template repository available: {}.", response.name());
        } catch (final NoSuchBucketException | NoSuchKeyException e) {
            final String msg = "Invalid bucket name or template path.";
            LOGGER.error("Could not initialize template repository. " + msg, e);
            throw new TemplateServiceConfigurationException("833e27f3-6813-43f5-aafd-cd511aca3ae0", msg);
        } catch (final S3Exception e) {
            final String msg = "Error querying the given S3 bucket.";
            LOGGER.error("Could not initialize template repository. " + msg, e);
            throw new TemplateServiceConfigurationException("9bd97feb-1387-40f3-bc82-01961e26bf5f", msg);
        }
    }

    /**
     * Returns the requested page of the actual template documents. 
     * @param page the requested page.
     * @return the request page of templates if found. 
     * @throws TemplateServiceException thrown in case of query or repo configuration errors.
     */
    @Override
    public Page<TemplateDocument> getTemplates(final Pageable page) throws TemplateServiceException{

        log.debug("getTemplates - Template repository bucket name: [{}], prefix: [{}], page request: [{}]",
                this.bucketName, this.prefix, page);

        try {
            final Page<TemplateDocument> result = new Page<>();

            final S3Client s3 = S3ClientFactory.getS3Client();

            final ListObjectsV2Request listObjectsRequest =
                    ListObjectsV2Request.builder()
                            .bucket(this.bucketName)
                            .prefix(this.prefix)
                            .delimiter("/")
                            .build();

            final ListObjectsV2Response listObjectsResponse = s3.listObjectsV2(listObjectsRequest);

            final List<TemplateDocument> items = listObjectsResponse.contents().stream()
                    .map(t -> t.toBuilder().key(t.key().replaceFirst(this.prefix + "/", "")).build())
                    .sorted(Comparator.comparing(S3Object::key))
                    .map(t -> new TemplateDocument(t.key()))
                    .collect(Collectors.toList());

            if (page != null && page.isPaged()) {

                final int endIndex = Math.min(page.getOffset() + page.getSize(), Math.max(items.size() - 1, 0));

                if (page.getOffset() < items.size() && endIndex < items.size()) {
                    result.setData(items.subList(page.getOffset(), endIndex));
                }
                result.setNumber(page.getPage());
                result.setSize(page.getSize());
                result.setTotalElements((long) items.size());
                result.setTotalPages((int) Math.ceil(Float.valueOf(result.getTotalElements()) / page.getSize()));
            } else {
                result.setData(items);
            }

            return result;
        } catch (final S3Exception e) {
            final  String msg = String.format("Error retrieving the template list - baseDir: [%s]", this.prefix);
            LOGGER.error(msg, e);

            throw new TemplateServiceException("3bf5d7f4-7071-4be2-9597-b9b882ecd7ce", msg);
        }
    }

    /**
     * Returns a template descriptor for the given id (template name).
     * The template version is omitted in this implementation.
     * @param id the template name (relative path from the prefix).
     * @param version document version - NOT USED in this implementation.
     * @param withBinary true to return the template binary also.
     * @return the template document if found.
     */
    @Override
    public Optional<TemplateDocument> getTemplateDocumentById(final String id, final String version, final boolean withBinary) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getTemplateDocumentById - {} (version handling not supported, omitting). binary: {}", id, withBinary);
        }

        final TemplateDocument result = new TemplateDocument(id, version);

        try (final InputStream is = getTemplate(id)) {

            if (is != null) {
                if (withBinary) {
                    result.setBinary(is.readAllBytes());
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("getTemplateDocumentById - template not found: {}", id);
                }
                throw new TemplateNotFoundException("41735738-3b96-4a97-b31b-de70ec55506c", String.format("Template not found: [%s]", id));
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("getTemplateDocumentById end - {}. result: {}", id, result);
            }
            return Optional.of(result);

        } catch (final TemplateNotFoundException e) {
            return Optional.empty();
        } catch (final IOException e) {
            throw new TemplateProcessException("e86e5b8c-ded2-4141-bad6-566ef87b2fb1", String.format("Error reading the template - id: [%s]", id));
        }
    }

    /**
     * Returns a template for a given template name.
     * @param templateFile the template name (relative path from the prefix).
     * @return the template document as stream if found.
     */
    public InputStream getTemplate(final String templateFile) {
        InputStream result;

        final String pathToFile = this.prefix + "/" + templateFile;
        try {
            final S3Client s3 = S3ClientFactory.getS3Client(this.bucketName);

            result = s3.getObject(GetObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(pathToFile)
                    .build());

        } catch (final S3Exception e) {
            result = null;
        }

        if (result == null) {
            LOGGER.error("Template not found. File: {}. ", pathToFile);
        } else {
            LOGGER.debug("Template found. File: {}. ", pathToFile);
        }

        return result;

    }

}
