package net.videki.templateutils.api.document.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.videki.templateutils.api.document.service.TemplateApiService;
import net.videki.templateutils.api.document.api.model.TemplateJobApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("${app.api.base-path:/api/v1}")
public class FillApiController implements FillApi {

    private final NativeWebRequest request;

    private final TemplateApiService templateApiService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /*
     * @Override public ResponseEntity<TemplateDocument> getTemplateById(final
     * String id, final String version) {
     * 
     * final Optional<net.videki.templateutils.template.core.template.descriptors.
     * TemplateDocument> resultDoc = this.templateApiService .getTemplateById(id,
     * version, false); if (resultDoc.isPresent()) { return
     * ResponseEntity.ok(TemplateDocumentToApiModelMapper.INSTANCE.entityToApiModel(
     * resultDoc.get())); } else { return ResponseEntity.notFound().build(); } }
     */
    @Override
    public ResponseEntity<TemplateJobApiResponse> postTemplateGenerationJob(final String id, final Object body) {

        if (log.isDebugEnabled()) {
            log.debug("postTemplateGenerationJob - id: [{}]", id);
        }
        if (log.isTraceEnabled()) {
            log.debug("postTemplateGenerationJob - id: [{}], data: [{}]", id, body);
        }

        final TemplateJobApiResponse result = new TemplateJobApiResponse();

        final String transactionId = this.templateApiService.postTemplateGenerationJob(id, body);

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

}
