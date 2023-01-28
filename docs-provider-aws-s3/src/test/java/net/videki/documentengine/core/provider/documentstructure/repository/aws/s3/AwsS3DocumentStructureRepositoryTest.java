package net.videki.documentengine.core.provider.documentstructure.repository.aws.s3;

/*-
 * #%L
 * docs-provider-aws-s3
 * %%
 * Copyright (C) 2023 - 2022 Levente Ban
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
import net.videki.documentengine.core.provider.aws.s3.S3ClientFactory;
import net.videki.documentengine.core.provider.documentstructure.DocumentStructureRepository;
import net.videki.documentengine.core.provider.persistence.Page;
import net.videki.documentengine.core.provider.persistence.Pageable;
import net.videki.documentengine.core.service.TemplateServiceRegistry;
import net.videki.documentengine.core.service.exception.TemplateProcessException;
import net.videki.documentengine.core.service.exception.TemplateServiceConfigurationException;
import net.videki.documentengine.core.service.exception.TemplateServiceException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.nio.file.Paths;

import static org.junit.Assert.*;

@Slf4j
public class AwsS3DocumentStructureRepositoryTest {

    private static DocumentStructureRepository documentStructureRepository;
    private static String TESTBUCKET;
    private static final String BASEDIR;
    private static final String TESTFILE = "contract-vintage_v02-separate.yml";

    static {
        BASEDIR = "testfiles/docstructures/";
    }

    @BeforeClass
    public static void initialize() {

        documentStructureRepository = TemplateServiceRegistry.getConfiguration().getDocumentStructureRepository();
        TESTBUCKET = ((AwsS3DocumentStructureRepository)documentStructureRepository).getBucketName();

        final S3Client s3 = S3ClientFactory.getS3Client(Region.EU_CENTRAL_1);

        try(InputStream dsAsStream =
                    Paths.get("target/test-classes/" + TESTFILE).toUri().toURL().openStream()) {

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(TESTBUCKET)
                            .key(BASEDIR + TESTFILE)
                            .build(), RequestBody.fromBytes(dsAsStream.readAllBytes()));
        } catch (final IOException e) {
            log.error("Error preparing test data.");
        }

    }

    @AfterClass
    public static void teardown()	{

    }

    @Test
    public void getDocStructureByIdWithNullShouldThrow() {

        try {
            assertNotNull(documentStructureRepository.getDocumentStructure(null));
        } catch (final TemplateProcessException e) {
            assertEquals("bf61ec46-ef0a-44bf-bf4f-64cad7b34b5d", e.getCode());
        } catch (final TemplateServiceConfigurationException e) {
            log.error("Invalid configuration found.", e);

            fail();
        }
    }

    @Test
    public void getDocStructureByIdExistingShouldReturnStructure() {

        try {
        assertNotNull(documentStructureRepository.getDocumentStructure(TESTFILE));
        } catch (final TemplateServiceException e) {
            fail();
        }
    }

    @Test
    public void getDocStructureByIdNonExistingShouldThrow() {

        try {
            assertNotNull(documentStructureRepository.getDocumentStructure("i_dont_exist.yml"));
        } catch (final TemplateProcessException e) {
            assertEquals("8bfba3ce-f1f3-48cd-8995-71256a7628f1", e.getCode());
        } catch (final TemplateServiceException e) {
            fail();
        }
    }

    @Test
    public void getDocStructuresWithNullShouldThrow() {

        try {
            assertNotNull(documentStructureRepository.getDocumentStructures(null));
        } catch (final TemplateProcessException e) {
            assertEquals("cb8807ce-5183-4247-bb0b-a1bc16b4eb86", e.getCode());
        } catch (final TemplateServiceConfigurationException e) {
            log.error("Invalid configuration found.", e);

            fail();
        } catch (final TemplateServiceException e) {
            log.error("Uncaught error.", e);

            fail();
        }
    }

    @Test
    public void getDocStructuresWithExistingShouldReturnStructureDescriptor() {

        try {

            final Pageable pageRequest = new Pageable();
            pageRequest.setPaged(true);
            pageRequest.setPage(0);
            pageRequest.setSize(10);
            pageRequest.setOffset(0);

            final Page<DocumentStructure> page = documentStructureRepository.getDocumentStructures(pageRequest);

            assertEquals(1, page.getSize().intValue());
        } catch (final TemplateProcessException e) {
            log.error("Error retrieving the doc structure", e);

            fail();
        } catch (final TemplateServiceConfigurationException e) {
            log.error("Invalid configuration found.", e);

            fail();
        } catch (final TemplateServiceException e) {
            log.error("Uncaught error.", e);

            fail();
        }
    }

}
