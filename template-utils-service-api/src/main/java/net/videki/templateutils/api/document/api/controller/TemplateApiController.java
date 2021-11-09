package net.videki.templateutils.api.document.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.videki.templateutils.api.document.service.TemplateApiService;
import net.videki.templateutils.api.document.api.mapper.PageableMapper;
import net.videki.templateutils.api.document.api.mapper.TemplateDocumentToApiModelMapper;
import net.videki.templateutils.api.document.api.model.GenerationResult;
import net.videki.templateutils.api.document.api.model.GetTemplatesResponse;
import net.videki.templateutils.api.document.api.model.Pageable;
import net.videki.templateutils.api.document.api.model.TemplateJobApiResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("${app.api.base-path:/api/v1}")
public class TemplateApiController implements TemplateApi {

    private final NativeWebRequest request;

    private final TemplateApiService templateApiService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<GetTemplatesResponse> getTemplates(final @Valid String templateId,
            final @Valid Pageable pageable) {

        try {
            if (pageable != null) {
                log.info("getTemplates - Querying template list page {}.", pageable);
            } else {
                log.info("Querying full template list.");
            }

            GetTemplatesResponse result = TemplateDocumentToApiModelMapper.INSTANCE
                    .pageToApiModel(templateApiService.getTemplates(templateId, PageableMapper.INSTANCE.map(pageable)));

            if (log.isDebugEnabled()) {
                log.debug("getTemplates - result: [{}]", result);
            }
            return ResponseEntity.ok(result);

        } catch (final Exception e) {
            log.error("Error querying template list.", e);

            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<TemplateJobApiResponse> postTemplateGenerationJob(final String id, final Object body,
            final String notificationUrl) {

        if (log.isDebugEnabled()) {
            log.debug("postTemplateGenerationJob - id: [{}]", id);
        }
        if (log.isTraceEnabled()) {
            log.debug("postTemplateGenerationJob - id: [{}], data: [{}]", id, body);
        }

        final TemplateJobApiResponse result = new TemplateJobApiResponse();

        final String transactionId = this.templateApiService.postTemplateGenerationJob(id, body, notificationUrl);

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

    @Override
    public ResponseEntity<GenerationResult> getResultDocumentByTransactionId(final String transactionId) {
        return TemplateApi.super.getResultDocumentByTransactionId(transactionId);
    }

    @Override
    public ResponseEntity<Resource> getResultDocumentForTemplateByTransactionIdAndResultDocumentId(
            final String transactionId, final String resultDocumentId) {
        return TemplateApi.super.getResultDocumentForTemplateByTransactionIdAndResultDocumentId(transactionId,
                resultDocumentId);
    }
}
