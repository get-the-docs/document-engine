package net.videki.templateutils.api.document.api.controller;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.videki.templateutils.api.document.api.mapper.GenerationResultApiModelMapper;
import net.videki.templateutils.api.document.api.mapper.GetTemplatesResponseApiModelMapper;
import net.videki.templateutils.api.document.api.mapper.PageableMapper;
import net.videki.templateutils.api.document.api.model.GenerationResult;
import net.videki.templateutils.api.document.api.model.GetTemplatesResponse;
import net.videki.templateutils.api.document.api.model.Pageable;
import net.videki.templateutils.api.document.api.model.TemplateJobApiResponse;
import net.videki.templateutils.api.document.service.TemplateApiService;
import net.videki.templateutils.template.core.documentstructure.v1.StoredGenerationResult;
import net.videki.templateutils.template.core.documentstructure.v1.StoredResultDocument;
import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.core.template.descriptors.TemplateDocument;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;

/**
 * Template API.
 *
 * @author Levente Ban
 */
@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("${app.api.base-path:/api/v1}")
public class TemplateApiController implements TemplateApi {

    private final NativeWebRequest request;

    private final TemplateApiService templateApiService;

    /**
     * Returns the native web request.
     */
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Returns the actual template list.
     *
     * @param templateId optional template id to query a single template.
     * @param pageable   the requested result page.
     * @return the available templates.
     */
    @Override
    public ResponseEntity<GetTemplatesResponse> getTemplates(final String templateId,
                                                             final Pageable pageable) {

        try {
            if (pageable != null) {
                log.info("getTemplates - Querying template list page {}.", pageable);
            } else {
                log.info("Querying full template list.");
            }

            Optional<Page<TemplateDocument>> resultPage = templateApiService.getTemplates(templateId, PageableMapper.INSTANCE.map(pageable));

            if (resultPage.isPresent()) {
                GetTemplatesResponse result = GetTemplatesResponseApiModelMapper.INSTANCE
                        .pageToApiModel(resultPage.get());

                if (log.isDebugEnabled()) {
                    log.debug("getTemplates - result: [{}]", result);
                }
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (final Exception e) {
            log.error("Error querying template list.", e);

            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Starts a single template based document generation with the given model object.
     *
     * @param id   the template id as stored in the template repository.
     * @param body the model object.
     * @return the transaction id to refer on status check and download.
     */
    @Override
    public ResponseEntity<TemplateJobApiResponse> postTemplateGenerationJob(final String id, final Object body,
                                                                            final String notificationUrl) {

        if (log.isDebugEnabled()) {
            log.debug("postTemplateGenerationJob - id: [{}]", id);
        }
        if (log.isTraceEnabled()) {
            log.debug("postTemplateGenerationJob - id: [{}], data: [{}]", id, body);
        }

        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        final TemplateJobApiResponse result = new TemplateJobApiResponse();
        final String transactionId = UUID.randomUUID().toString();

        this.templateApiService.postTemplateGenerationJob(transactionId, id, body,
                notificationUrl);

        result.setTransactionId(transactionId);

        if (transactionId != null) {
            if (log.isDebugEnabled()) {
                log.debug("postTemplateGenerationJob - result: [{}]", result);
            }

            return ResponseEntity.accepted().body(result);

        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Returns the available result documents for the given transaction id.
     *
     * @param transactionId the transaction id.
     * @return the generation result containing the list of the result documents (refer to these to download via getResultDocumentForTemplateByTransactionIdAndResultDocumentId).
     */
    @Override
    public ResponseEntity<GenerationResult> getResultDocumentByTransactionId(final String transactionId) {
        if (log.isDebugEnabled()) {
            log.debug("getResultDocumentByTransactionId - transactionId: [{}]", transactionId);
        }

        if (transactionId == null || transactionId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final Optional<StoredGenerationResult> generationResult =
                    this.templateApiService.getResultDocumentByTransactionId(transactionId);

            if (log.isDebugEnabled()) {
                if (generationResult.isPresent()) {
                    log.debug("getResultDocumentByTransactionId - transactionId: [{}]",
                            transactionId);
                } else {
                    log.warn("getResultDocumentByTransactionId - transactionId: [{}], no generation result caught.",
                            transactionId);
                }
            }
            if (log.isTraceEnabled()) {
                generationResult.ifPresent(storedGenerationResult -> log.debug(
                        "getResultDocumentByTransactionId - transactionId: [{}], generation result: {}",
                        transactionId, storedGenerationResult));
            }

            return generationResult.map(storedGenerationResult -> ResponseEntity.ok(GenerationResultApiModelMapper.INSTANCE.map(storedGenerationResult))).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (final TemplateServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Returns a given result document for a transaction id and result document id.
     *
     * @param transactionId    the transaction id.
     * @param resultDocumentId the result document to download.
     * @return the result document descriptor and binary.
     */
    @Override
    public ResponseEntity<Resource> getResultDocumentForTemplateByTransactionIdAndResultDocumentId(
            final String transactionId, final String resultDocumentId) {

        if (log.isDebugEnabled()) {
            log.debug("getResultDocumentForTemplateByTransactionIdAndResultDocumentId - transactionId: [{}], result document id: [{}]", transactionId, resultDocumentId);
        }

        if ((transactionId == null || transactionId.isBlank()) &&
                (resultDocumentId == null || resultDocumentId.isBlank())) {
            return ResponseEntity.badRequest().build();
        }

        ResponseEntity<Resource> result;

        try {
            final Optional<StoredResultDocument> storedResultDocument =
                    this.templateApiService.getResultDocumentByTransactionIdAndDocumentId(transactionId,
                            resultDocumentId, true);

            if (storedResultDocument.isPresent() && storedResultDocument.get().getBinary() != null) {
                // TODO: return and determine output format
                final HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDisposition(
                        ContentDisposition.attachment().filename(storedResultDocument.get().getFileName()).build());

                final byte[] binaryData = storedResultDocument.get().getBinary();
                final InputStreamResource ds = new InputStreamResource(new ByteArrayInputStream(binaryData));

                if (log.isDebugEnabled()) {
                    if (binaryData != null && binaryData.length > 0) {
                        log.debug("getResultDocumentForTemplateByTransactionIdAndResultDocumentId - transactionId: [{}], result document id: [{}]",
                                transactionId, resultDocumentId);
                    } else {
                        log.warn("getResultDocumentForTemplateByTransactionIdAndResultDocumentId - transactionId: [{}], result document id: [{}] - no generation result caught.",
                                transactionId, resultDocumentId);
                    }
                }        
                result = ResponseEntity.ok().headers(headers)
                        .contentLength(binaryData.length)
                        .body(ds);
            } else {
                log.warn("getResultDocumentForTemplateByTransactionIdAndResultDocumentId - transactionId: [{}], result document id: [{}] - no generation result caught.",
                transactionId, resultDocumentId);

                result = ResponseEntity.notFound().build();
            }
        } catch (final TemplateServiceException e) {
            log.error("getResultDocumentForTemplateByTransactionIdAndResultDocumentId - transactionId: [{}], result document id: [{}] - error caught on download.",
            transactionId, resultDocumentId, e);
            
            result = ResponseEntity.internalServerError().build();
        } 

        return result;
    }
}
