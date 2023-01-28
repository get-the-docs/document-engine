package net.videki.documentengine.core.provider.aws.s3;

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
import net.videki.documentengine.core.provider.documentstructure.DocumentStructureRepository;
import net.videki.documentengine.core.provider.documentstructure.repository.aws.s3.AwsS3DocumentStructureRepository;
import net.videki.documentengine.core.service.TemplateServiceRegistry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static org.junit.Assert.*;

@Slf4j
public class S3ClientFactoryTest {

    private static String TESTBUCKET;
    private static final String BASEDIR;

    static {
        BASEDIR = "testfiles/templates/";
    }

    @BeforeClass
    public static void initialize() {
        final DocumentStructureRepository repository = TemplateServiceRegistry.getConfiguration().getDocumentStructureRepository();
        TESTBUCKET = ((AwsS3DocumentStructureRepository)repository).getBucketName();

        final S3Client s3 = S3ClientFactory.getS3Client(Region.EU_CENTRAL_1);

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(TESTBUCKET)
                        .key(BASEDIR + "myfile.txt")
                        .build(), RequestBody.fromBytes("Test content.".getBytes()));

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(TESTBUCKET)
                        .key(BASEDIR + "supportedfile.docx")
                        .build(), RequestBody.fromBytes("Test content.".getBytes()));

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(TESTBUCKET)
                        .key(BASEDIR + "supportedfile2.docx")
                        .build(), RequestBody.fromBytes("Test content.".getBytes()));
    }

    @AfterClass
    public static void teardown()	{

    }

    @Test
    public void testExistsFileExists() {

        final S3Client s3 = S3ClientFactory.getS3Client();

        assertNotNull(s3);

    }

    @Test
    public void testGetRegionForExistingBucket() {

        final Region region = S3ClientFactory.getRegion(TESTBUCKET);

        assertNotNull(region);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRegionForNonExistingBucket() {

        final Region region = S3ClientFactory.getRegion("there_is_no_such_bucket_here");

        assertNotNull(region);

    }

    @Test
    public void testGetClientForExistingBucket() {

        final S3Client s3 = S3ClientFactory.getS3Client(TESTBUCKET);

        assertNotNull(s3);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetClientForNonExistingBucket() {

        final S3Client s3 = S3ClientFactory.getS3Client("there_is_no_such_bucket_here");

        assertNotNull(s3);

    }

    @Test
    public void testGetClientByRegion() {

        final S3Client s3 = S3ClientFactory.getS3Client(Region.EU_CENTRAL_1);

        assertNotNull(s3);

    }

}
