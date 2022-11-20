package net.videki.templateutils.api.document.api.controller;

/*-
 * #%L
 * docs-service-api
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
import net.videki.templateutils.api.document.api.mapper.GetDocumentStructuresResponseApiModelMapper;
import net.videki.templateutils.api.document.api.mapper.PageableMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import java.util.UUID;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-21T15:16:34.406301500+01:00[Europe/Prague]")

/**
 * Document structure API.
 * 
 * @author Levente Ban
 */
@Controller
@RequestMapping("${app.api.base-path}")
public class DocumentstructuresApiController implements DocumentstructuresApi {

    private final NativeWebRequest request;

    @Autowired
    private DocumentStructureApiService documentStructureApiService;

    @Autowired
    public DocumentstructuresApiController(NativeWebRequest request) {
        this.request = request;
    }

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
     * @param pageable the requested result page.
     * @return the page of document structures.
     */
    @Override
    public ResponseEntity<GetDocumentStructuresResponse> getDocumentStructures(final String documentStructureId, final PageableTemplate pageable) {

        final var result = this.documentStructureApiService.getDocumentStructures(documentStructureId, PageableMapper.INSTANCE.map(pageable));

        return ResponseEntity.ok(GetDocumentStructuresResponseApiModelMapper.INSTANCE.pageToApiModel(result));
    }

    /**
     * Initializes a document generation job for the given document structure and value set.
     * 
     * @param id the document structure id.
     * @param valueSet the model objects.
     * @param notificationUrl optional notification hook (webhook, etc.) to indicate job completion. 
     */
    @PreAuthorize("hasAnyRole('documentstructure_user')")
    @Override
    public ResponseEntity<DocStructureJobApiResponse> postDocumentStructureGenerationJob(
            final String id,
            final ValueSet valueSet,
            final String notificationUrl) {

        final DocStructureJobApiResponse result = new DocStructureJobApiResponse();

        final String transactionId = UUID.randomUUID().toString();
        result.setTransactionId(transactionId);

        this.documentStructureApiService.postDocumentStructureGenerationJob(transactionId, id, valueSet, notificationUrl);

        return ResponseEntity.accepted().body(result);
    }

    /**
     * Returns the generation result (containing the result documents) for a document generation job. 
     * 
     * @param transactionId the transaction id.
     * @return the generation result.
     */
    @PreAuthorize("hasAnyRole('documentstructure_user')")
    @Override
    public ResponseEntity<GenerationResult> getGenerationResultByTransactionId(final String transactionId) {
        return DocumentstructuresApi.super.getGenerationResultByTransactionId(transactionId);
    }

    /**
     * Returns a result document for the given transaction.
     * 
     * @param transactionId the transaction id.
     * @param resultDocumentId the result document to return.
     * @return the result document descriptor and binary if found.
     */
    @PreAuthorize("hasAnyRole('documentstructure_user')")
    @Override
    public ResponseEntity<Resource> getResultDocumentForDocStructureByTransactionIdAndResultDocumentId(final String transactionId,
      final String resultDocumentId) {
        return DocumentstructuresApi.super.getResultDocumentForDocStructureByTransactionIdAndResultDocumentId(transactionId,
            resultDocumentId);
    }
}
