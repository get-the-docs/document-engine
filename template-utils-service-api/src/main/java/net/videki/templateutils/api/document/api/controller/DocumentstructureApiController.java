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

import net.videki.templateutils.api.document.api.model.*;
import net.videki.templateutils.api.document.service.DocumentStructureApiService;
import net.videki.templateutils.api.document.service.TemplateApiService;
import net.videki.templateutils.template.core.context.ContextObjectProxyBuilder;
import net.videki.templateutils.template.core.context.dto.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.v1.StoredGenerationResult;
import net.videki.templateutils.template.core.documentstructure.v1.StoredResultDocument;
import net.videki.templateutils.template.core.documentstructure.v1.ValueSet;
import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.api.document.api.mapper.GenerationResultApiModelMapper;
import net.videki.templateutils.api.document.api.mapper.GetDocumentStructuresResponseApiModelMapper;
import net.videki.templateutils.api.document.api.mapper.PageableMapper;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-21T15:16:34.406301500+01:00[Europe/Prague]")

/**
 * Document structure API.
 * 
 * @author Levente Ban
 */
@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("${app.api.base-path:/api/v1}")
public class DocumentstructureApiController implements DocumentstructureApi {

    private final NativeWebRequest request;
    private final TemplateApiService templateApiService;
    private final DocumentStructureApiService documentStructureApiService;

    /**
     * Returns the native web request.
     */
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Returns the available document structures.
     * 
     * @param documentStructureId optional document structure id.
     * @param pageable            the requested result page.
     * @return the page of document structures.
     */
    @Override
    public ResponseEntity<GetDocumentStructuresResponse> getDocumentStructures(final String documentStructureId,
            final Pageable pageable) {

        try {
            if (pageable != null) {
                log.info("getDocumentStructures - Querying template list page {}.", pageable);
            } else {
                log.info("Querying full document structure list.");
            }

            Optional<Page<DocumentStructure>> resultPage = documentStructureApiService
                    .getDocumentStructures(documentStructureId, PageableMapper.INSTANCE.map(pageable));

            if (resultPage.isPresent()) {
                GetDocumentStructuresResponse result = GetDocumentStructuresResponseApiModelMapper.INSTANCE
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
     * Initializes a document generation job for the given document structure and
     * value set.
     * 
     * @param id              the document structure id.
     * @param valueSet        the model objects.
     * @param notificationUrl optional notification hook (webhook, etc.) to indicate
     *                        job completion.
     */


    public ResponseEntity<DocStructureJobApiResponse> postDocumentStructureGenerationJob(final String documentStructureId,
                                                                                         final String locale,
                                                                                         final List<Object> requestBody,
                                                                                         final String valuesetTransactionId,
                                                                                         final String notificationUrl) {

        final DocStructureJobApiResponse result = new DocStructureJobApiResponse();

        final String transactionId = UUID.randomUUID().toString();
        result.setTransactionId(transactionId);

        final ValueSet valueSet = new ValueSet(valuesetTransactionId);
        valueSet.withDocumentStructureId(documentStructureId);
        valueSet.setLocale(new Locale(locale));
        for (final Object actItem : requestBody) {
            final Map<?, ?> valueMap = (Map<?, ?>)actItem;
            final Optional<?> c = valueMap.keySet().stream().findFirst();
            final String contextKey = c.isPresent() ? (String)c.get() : TemplateContext.CONTEXT_ROOT_KEY;
            
            final TemplateContext ctx = new TemplateContext();
            ctx.withContext(contextKey, ContextObjectProxyBuilder.build(valueMap));
        }
        valueSet.build();

        if (log.isDebugEnabled()) {
            log.debug(
                    "Document structure generation request assembled. DocumentStructureId: [{}], valueset item count: [{}]",
                    documentStructureId, valueSet.getValues().getCtx().size());
        }
        if (log.isTraceEnabled()) {
            log.trace(
                    "Document structure generation request assembled. DocumentStructureId: [{}], valueset items: [{}]",
                    documentStructureId, valueSet.getValues());
        }

        this.documentStructureApiService.postDocumentStructureGenerationJob(transactionId, documentStructureId,
                valueSet, notificationUrl);

        return ResponseEntity.accepted().body(result);
    }

    /**
     * Returns the generation result (containing the result documents) for a
     * document generation job.
     * 
     * @param transactionId the transaction id.
     * @return the generation result.
     */
    @Override
    public ResponseEntity<GenerationResult> getGenerationResultByTransactionId(final String transactionId) {
        if (log.isDebugEnabled()) {
            log.debug("getResultDocumentByTransactionId - transactionId: [{}]", transactionId);
        }

        if (transactionId == null || transactionId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final Optional<StoredGenerationResult> generationResult = this.templateApiService
                    .getResultDocumentByTransactionId(transactionId);

            if (log.isDebugEnabled()) {
                if (generationResult.isPresent()) {
                    log.debug("getResultDocumentByTransactionId - transactionId: [{}]", transactionId);
                } else {
                    log.warn("getResultDocumentByTransactionId - transactionId: [{}], no generation result caught.",
                            transactionId);
                }
            }
            if (log.isTraceEnabled()) {
                generationResult.ifPresent(storedGenerationResult -> log.debug(
                        "getResultDocumentByTransactionId - transactionId: [{}], generation result: {}", transactionId,
                        storedGenerationResult));
            }

            return generationResult
                    .map(storedGenerationResult -> ResponseEntity
                            .ok(GenerationResultApiModelMapper.INSTANCE.map(storedGenerationResult)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (final TemplateServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Returns a result document for the given transaction.
     * 
     * @param transactionId    the transaction id.
     * @param resultDocumentId the result document to return.
     * @return the result document descriptor and binary if found.
     */
    @Override
    public ResponseEntity<Resource> getResultDocumentForDocStructureByTransactionIdAndResultDocumentId(
            final String transactionId, final String resultDocumentId) {

        if (log.isDebugEnabled()) {
            log.debug(
                    "getResultDocumentForDocStructureByTransactionIdAndResultDocumentId - transactionId: [{}], result document id: [{}]",
                    transactionId, resultDocumentId);
        }

        if ((transactionId == null || transactionId.isBlank())
                && (resultDocumentId == null || resultDocumentId.isBlank())) {
            return ResponseEntity.badRequest().build();
        }

        ResponseEntity<Resource> result;

        try {
            final Optional<StoredResultDocument> storedResultDocument = this.templateApiService
                    .getResultDocumentByTransactionIdAndDocumentId(transactionId, resultDocumentId, true);

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
                        log.debug(
                                "getResultDocumentForDocStructureByTransactionIdAndResultDocumentId - transactionId: [{}], result document id: [{}]",
                                transactionId, resultDocumentId);
                    } else {
                        log.warn(
                                "getResultDocumentForDocStructureByTransactionIdAndResultDocumentId - transactionId: [{}], result document id: [{}] - no generation result caught.",
                                transactionId, resultDocumentId);
                    }
                }
                result = ResponseEntity.ok().headers(headers).contentLength(binaryData.length).body(ds);
            } else {
                log.warn(
                        "getResultDocumentForTemplateByTransactionIdAndResultDocumentId - transactionId: [{}], result document id: [{}] - no generation result caught.",
                        transactionId, resultDocumentId);

                result = ResponseEntity.notFound().build();
            }
        } catch (final TemplateServiceException e) {
            log.error(
                    "getResultDocumentForTemplateByTransactionIdAndResultDocumentId - transactionId: [{}], result document id: [{}] - error caught on download.",
                    transactionId, resultDocumentId, e);

            result = ResponseEntity.internalServerError().build();
        }

        return result;

    }
}
