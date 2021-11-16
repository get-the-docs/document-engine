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

import net.videki.templateutils.api.document.service.DocumentStructureApiService;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.api.document.api.mapper.GetDocumentStructuresResponseApiModelMapper;
import net.videki.templateutils.api.document.api.mapper.PageableMapper;
import net.videki.templateutils.api.document.api.mapper.ValueSetItemApiModelToEntityMapper;
import net.videki.templateutils.api.document.api.model.DocStructureJobApiResponse;
import net.videki.templateutils.api.document.api.model.GenerationResult;
import net.videki.templateutils.api.document.api.model.GetDocumentStructuresResponse;
import net.videki.templateutils.api.document.api.model.Pageable;
import net.videki.templateutils.api.document.api.model.ValueSetItem;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
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
    public ResponseEntity<DocStructureJobApiResponse> postDocumentStructureGenerationJob(String documentStructureId,
            String locale, List<ValueSetItem> valueSetItems, String valuesetTransactionId, String notificationUrl) {

        final DocStructureJobApiResponse result = new DocStructureJobApiResponse();

        final String transactionId = UUID.randomUUID().toString();
        result.setTransactionId(transactionId);

        final ValueSet valueSet = new ValueSet(valuesetTransactionId);
        valueSet.setDocumentStructureId(documentStructureId);
        valueSet.setLocale(new Locale(locale));
        for (final net.videki.templateutils.api.document.model.ValueSetItem actItem : ValueSetItemApiModelToEntityMapper.INSTANCE.apiModelListToEntityList(valueSetItems)) {
            valueSet.addContext(actItem.getTemplateElementId(), actItem.getValue() );
        }

        if (log.isDebugEnabled()) {
            log.debug(
                    "Document structure generation request assembled. DocumentStructureId: [{}], valueset item count: [{}]",
                    documentStructureId, valueSetItems.size());
        }
        if (log.isTraceEnabled()) {
            log.trace(
                    "Document structure generation request assembled. DocumentStructureId: [{}], valueset items: [{}]",
                    documentStructureId, valueSetItems);
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
        return DocumentstructureApi.super.getGenerationResultByTransactionId(transactionId);
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
        return DocumentstructureApi.super.getResultDocumentForDocStructureByTransactionIdAndResultDocumentId(
                transactionId, resultDocumentId);
    }
}
