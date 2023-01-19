package net.videki.documentengine.core.provider.templaterepository.aws.s3;

/*-
 * #%L
 * docs-templaterepo-aws-s3
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
import net.videki.documentengine.core.provider.templaterepository.TemplateRepository;
import net.videki.documentengine.core.service.TemplateServiceRegistry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;

import static org.junit.Assert.*;

@Slf4j
public class AwsS3TemplateRepositoryTest {

    private static TemplateRepository templateRepository;
    private static final String TESTBUCKET;
    private static final String BASEDIR;

    static {
        TESTBUCKET = System.getenv(AwsS3TemplateRepository.TEMPLATE_REPOSITORY_PROVIDER_BUCKET_NAME_ENV_VAR);
        BASEDIR = "testfiles/templates/";
    }

    @BeforeClass
    public static void initialize() {

        templateRepository = TemplateServiceRegistry.getConfiguration().getTemplateRepository();

        final S3Client s3 = S3ClientFactory.getS3Client(Region.EU_CENTRAL_1);
        final String fileName = "myfile.txt";
        final String path = BASEDIR + "textfiles/";

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(TESTBUCKET)
                        .key(path + fileName)
                        .build(), RequestBody.fromBytes("Test content.".getBytes()));

    }

    @AfterClass
    public static void teardown() throws Exception	{

    }

    @Test
    public void testExistsFileExists() {

        final String path =  "textfiles/";
        final String fileName = "myfile.txt";

        assertNotNull(templateRepository.getTemplate(path + fileName));

    }

    @Test
    public void testExistsFileNotExists() {

        final String path =  "textfiles/";
        final String fileName = "nonexistingfile.txt";

        assertNull(templateRepository.getTemplate(path + fileName));

    }

    @Test
    public void testGetInputStreamForSingleFileToBucketInDefaultRegionOk() throws Exception	{

        final String path =  "textfiles/";
        final String fileName = "myfile.txt";

        final InputStream is = templateRepository.getTemplate(path + fileName);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        final StringBuilder textData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            textData.append(line);
        }

        is.close();

        log.debug("myfile.txt: " + textData);

        assertFalse(textData.toString().isEmpty());

    }

    @Test
    public void testGetInputStreamForSingleFileToBucketNonExisting() throws Exception	{

        final String path = "textfiles/";
        final String fileName = "nonexistingfile.txt";

        final InputStream is = templateRepository.getTemplate(path + fileName);

        assertNull(is);
    }

}