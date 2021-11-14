package net.videki.templateutils.api.document;

/*-
 * #%L
 * template-utils-service-api
 * %%
 * Copyright (C) 2021 Levente Ban
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.videki.templateutils.api.document.api.model.*;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {ServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentApiTemplatesIT {

    final String JSON_DIR = "/values/integrationtests";
    private String testEndpoint;

    @LocalServerPort
    private int port;

    final ObjectMapper jsonMapper = new ObjectMapper();

    @PostConstruct
    public void setUp() {

        this.testEndpoint = "http://localhost:" + port + "/api/v1";

    }

    @PreDestroy
    void tearDown() {
    }

    private String getTestDataFromFile(final String fileName) {

        String result = "";

        try {
            final File file = new ClassPathResource(fileName).getFile();
            final BufferedReader br = new BufferedReader(new FileReader(file));

            String actLine;
            final StringBuilder tmp = new StringBuilder();
            while ((actLine = br.readLine()) != null) {
                tmp.append(actLine);
            }

            result = tmp.toString();
        } catch (final IOException e) {
            log.warn("Could not read test data file: {}", e.getMessage());
        }

        return result;
    }

    private String getDataForTestCase(final String fileName) {
        return getTestDataFromFile(JSON_DIR + File.separator +
                this.getClass().getSimpleName() + File.separator +
                new Throwable().getStackTrace()[1].getMethodName() + File.separator + fileName);
    }

    @Test
    void getTemplatesAllShouldReturnTestResources() {
        log.info("getTemplatesAllShouldReturnTestResources...");
//        restTemplate.getForEntity(this.testEndpoint + "/template?{filed}={value}&page={page}&offset={offset}&size={szie}&sort=[{property:\"{propertyValue}\",\"direction\":\"{direction}\"}]", GetTemplatesResponse.class);

        final TestRestTemplate restTemplate = new TestRestTemplate();
        final ResponseEntity<GetTemplatesResponse> responseEntity =
                restTemplate.getForEntity(this.testEndpoint + "/template?" + 
                    "paged=false", GetTemplatesResponse.class);

        log.debug("get result: " + responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(14,responseEntity.getBody().getContents().size());

        log.info("getTemplatesAllShouldReturnTestResources - end.");
    }

    @Test
    void getTemplatesFirstPageShouldReturnTestResources() {
        log.info("getTemplatesFirstPageShouldReturnTestResources...");

        final TestRestTemplate restTemplate = new TestRestTemplate();
        final ResponseEntity<GetTemplatesResponse> responseEntity =
                restTemplate.getForEntity(this.testEndpoint + "/template?" + 
                    "page={page}&size={limit}", GetTemplatesResponse.class,
                    0, 10);

        log.debug("get result: " + responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(10,responseEntity.getBody().getContents().size());

        log.info("getTemplatesFirstPageShouldReturnTestResources - end.");
    }

    @Test
    void getTemplatesIdGivenShouldReturnTestResource() {
        log.info("getTemplatesIdGivenShouldReturnTestResource...");

        final TestRestTemplate restTemplate = new TestRestTemplate();
        final ResponseEntity<GetTemplatesResponse> responseEntity =
                restTemplate.getForEntity(this.testEndpoint + "/template?" + 
                    "templateId={templateId}", GetTemplatesResponse.class,
                    "integrationtests/contracts/contract_v09_hu.docx");

        log.debug("get result: " + responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1,responseEntity.getBody().getContents().size());

        log.info("getTemplatesIdGivenShouldReturnTestResource - end.");
    }

    @Test
    void getTemplatesIdGivenNonExistingShouldReturn404() {
        log.info("getTemplatesIdGivenNonExistingShouldReturn404...");

        final TestRestTemplate restTemplate = new TestRestTemplate();
        final ResponseEntity<GetTemplatesResponse> responseEntity =
                restTemplate.getForEntity(this.testEndpoint + "/template?" + 
                    "templateId={templateId}", GetTemplatesResponse.class,
                    "integrationtests/contracts/contract_v09_hu-this_file_does_not_exist.docx");

        log.debug("get result: " + responseEntity.getBody());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        log.info("getTemplatesIdGivenNonExistingShouldReturn404 - end.");
    }

    @Test
    void getTemplatesPageOutOfRangeShouldReturn200WithEmptyPage() {
        log.info("getTemplatesPageOutOfRangeShouldReturn200WithEmptyPage...");

        final TestRestTemplate restTemplate = new TestRestTemplate();
        final ResponseEntity<GetTemplatesResponse> responseEntity =
                restTemplate.getForEntity(this.testEndpoint + "/template?" + 
                    "page={page}&size={limit}", GetTemplatesResponse.class,
                    3000, 10);

        log.debug("get result: " + responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0,responseEntity.getBody().getContents().size());

        log.info("getTemplatesPageOutOfRangeShouldReturn200WithEmptyPage - end.");
    }

    @Test
    void getTemplatesNoParamsShouldReturnDefaultPage() {
        log.info("getTemplatesNoParamsShouldReturnUnpaged...");

        final TestRestTemplate restTemplate = new TestRestTemplate();
        final ResponseEntity<GetTemplatesResponse> responseEntity =
                restTemplate.getForEntity(this.testEndpoint + "/template", GetTemplatesResponse.class);

        log.debug("get result: " + responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final var defaultPage = new Pageable();
        assertEquals(defaultPage.getSize(),responseEntity.getBody().getContents().size());

        log.info("getTemplatesNoParamsShouldReturnUnpaged - end.");
    }

    @Test
    void postTemplateGenerationJobValidShouldReturnTransactionId() {
        log.info("postTemplateGenerationJobValidShouldReturnTransactionId...");

        final Map<String, String> urlVariables = new HashMap<>();
        final var data = this.getDataForTestCase("contractdata.json");
        urlVariables.put("templateId", "integrationtests/contracts/contract_v09_hu.docx");

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/json"));
        converter.setObjectMapper(this.jsonMapper);

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final RestTemplate restTemplate =
                new RestTemplate(Collections.singletonList(converter));

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        final ResponseEntity<TemplateJobApiResponse> responseEntity =
                restTemplate.postForEntity(this.testEndpoint + "/template/fill?" +
                        "templateId={templateId}", data, TemplateJobApiResponse.class,
                        urlVariables);

        log.debug("postTemplateGenerationJobValidShouldReturnTransactionId - transaction id: " + responseEntity.getBody());

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().getTransactionId() != null &&
                !responseEntity.getBody().getTransactionId().isBlank());

        log.info("postTemplateGenerationJobValidShouldReturnTransactionId - end.");
    }

    @Test
    void postTemplateGenerationJobNonexistingTemplateShouldReturnTransactionId() {
        log.info("postTemplateGenerationJobNonexistingTemplateShouldReturnBadRequest...");

        final Map<String, String> urlVariables = new HashMap<>();
        final var data = this.getDataForTestCase("contractdata.json");
        urlVariables.put("templateId", "integrationtests/contracts/contract_v09_hu-i_dont_exist.docx");

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/json"));
        converter.setObjectMapper(this.jsonMapper);

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final RestTemplate restTemplate =
                new RestTemplate(Collections.singletonList(converter));

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        final ResponseEntity<TemplateJobApiResponse> responseEntity =
                restTemplate.postForEntity(this.testEndpoint + "/template/fill?" +
                                "templateId={templateId}", data, TemplateJobApiResponse.class,
                        urlVariables);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().getTransactionId() != null &&
                !responseEntity.getBody().getTransactionId().isBlank());

        log.info("postTemplateGenerationJobNonexistingTemplateShouldReturnTransactionId - end.");
    }

    @Test
    void postTemplateGenerationJobNoParamsTemplateShouldReturnUnsupportedMediaType() {
        log.info("postTemplateGenerationJobNoParamsTemplateShouldReturnUnsupportedMediaType...");

        final Map<String, String> urlVariables = new HashMap<>();

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/json"));
        converter.setObjectMapper(this.jsonMapper);

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final RestTemplate restTemplate =
                new RestTemplate(Collections.singletonList(converter));

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        try {
            final ResponseEntity<TemplateJobApiResponse> responseEntity =
                    restTemplate.postForEntity(this.testEndpoint + "/template/fill",
                            null, TemplateJobApiResponse.class,
                            urlVariables);
        } catch (final HttpClientErrorException e) {
            assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getStatusCode());
        } catch (final Exception e) {
            fail();
        }

        log.info("postTemplateGenerationJobNoParamsTemplateShouldReturnUnsupportedMediaType - end.");
    }

    @Test
    void getTemplateGenerationJobValidShouldReturnGenerationResult() {
        log.info("getTemplateGenerationJobValidShouldReturnGenerationResult...");

        final Map<String, String> urlVariables = new HashMap<>();
        final var data = this.getDataForTestCase("contractdata.json");
        urlVariables.put("templateId", "integrationtests/contracts/contract_v09_hu.docx");

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/json"));
        converter.setObjectMapper(this.jsonMapper);

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final RestTemplate restTemplate =
                new RestTemplate(Collections.singletonList(converter));

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        // Step 1 - post job
        final ResponseEntity<GenerationResult> responseEntity =
                restTemplate.postForEntity(this.testEndpoint + "/template/fill?" +
                                "templateId={templateId}", data, GenerationResult.class,
                        urlVariables);

        // Step 2 - query result (may be empty due internal async processing)
        if (responseEntity != null &&
                responseEntity.hasBody() &&
                responseEntity.getBody().getTransactionId() != null) {
            final String transactionId = responseEntity.getBody().getTransactionId();

            log.debug("Transaction id from post: {}", transactionId);

            final TestRestTemplate resultRestTemplate = new TestRestTemplate();
            final ResponseEntity<GenerationResult> resultResponseEntity =
                    resultRestTemplate.getForEntity(this.testEndpoint + "/template/fill/{transactionId}",
                            GenerationResult.class, transactionId);

            log.debug("Transaction id from get results: {}", resultResponseEntity.getBody());

            assertEquals(HttpStatus.OK, resultResponseEntity.getStatusCode());
        } else {
            fail();
        }

        log.info("getTemplateGenerationJobValidShouldReturnGenerationResult - end.");
    }


    @Test
    void getResultDocumentForValidTransactionShouldReturnBinary() {
        log.info("getResultDocumentForValidTransactionShouldReturnBinary...");

        final Map<String, String> urlVariables = new HashMap<>();
        final var data = this.getDataForTestCase("contractdata.json");
        urlVariables.put("templateId", "integrationtests/contracts/contract_v09_hu.docx");

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/json"));
        converter.setObjectMapper(this.jsonMapper);

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final RestTemplate restTemplate =
                new RestTemplate(Collections.singletonList(converter));

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        // Step 1 - post job
        final ResponseEntity<GenerationResult> responseEntity =
                restTemplate.postForEntity(this.testEndpoint + "/template/fill?" +
                                "templateId={templateId}", data, GenerationResult.class,
                        urlVariables);

        // Step 4 - query result
        if (responseEntity.getBody() != null &&
                responseEntity.getBody().getTransactionId() != null) {
            final String transactionId = responseEntity.getBody().getTransactionId();

            log.debug("Transaction id from /template/fill: {}", transactionId);

            final TestRestTemplate resultRestTemplate = new TestRestTemplate();

            List<ResultDocument> resultDocumentList = null;
            for (int tryCount = 0; tryCount < 4; tryCount++) {
                final ResponseEntity<GenerationResult> resultResponseEntity =
                        resultRestTemplate.getForEntity(this.testEndpoint + "/template/fill/{transactionId}",
                                GenerationResult.class, transactionId);

                assertEquals(HttpStatus.OK, resultResponseEntity.getStatusCode());

                log.debug("Transaction id from get /template/fill/{transactionId}: {}", resultResponseEntity.getBody());

                if (resultResponseEntity.getBody() != null) {
                    resultDocumentList = resultResponseEntity.getBody().getElements();

                    if (resultDocumentList != null && !resultDocumentList.isEmpty()) {
                        break;
                    }
                } else {
                    // Keep waiting for the docs to be generated.
                    try {
                        Thread.sleep(4000);
                    } catch (final InterruptedException e) {
                        log.error("Error waiting for generation result. Thread interrupted.", e);
                    }
                }
            }

            Assertions.assertTrue(!resultDocumentList.isEmpty());

            // Step 3 - download first result (simply into the memory since this is not a load test)
            final String firstResultDocFileName = resultDocumentList.get(0).getDocumentName();

            log.debug("Result document list: {}", resultDocumentList);

            final TestRestTemplate resultDocRestTemplate = new TestRestTemplate();

            byte[] resultDocBinary = resultDocRestTemplate.getForObject(this.testEndpoint + "/template/fill/{transactionId}/doc?" +
                    "resultDocumentId={resultDocumentId}", byte[].class, transactionId, firstResultDocFileName);
            try {
                Files.write(Paths.get(firstResultDocFileName), resultDocBinary);
            } catch (final IOException e) {
                log.error("Error write result file.", e);
            }

            log.debug("Result docs downloaded for transaction id: {}, result document name: {}, binary size: {}",
                    transactionId, firstResultDocFileName, resultDocBinary.length);

            Assertions.assertTrue(resultDocBinary.length > 0);

        } else {
            fail();
        }

        log.info("getResultDocumentForValidTransactionShouldReturnBinary - end.");
    }
}
