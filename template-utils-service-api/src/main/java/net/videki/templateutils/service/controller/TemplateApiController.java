package net.videki.templateutils.service.controller;

import lombok.extern.slf4j.Slf4j;
import net.videki.templateutils.service.model.TemplateJobApiResponse;
import net.videki.templateutils.service.service.TemplateApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-16T20:00:58.850415800+01:00[Europe/Prague]")

@Slf4j
@Controller
@RequestMapping("${openapi.documentGeneration.base-path:/api/v1}")
public class TemplateApiController implements TemplateApi {

    private final NativeWebRequest request;

    @Autowired
    public TemplateApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Autowired
    private TemplateApiService templateApiService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<TemplateJobApiResponse> postTemplateGenerationJob(
            @Pattern(regexp = "^[a-zA-Z0-9_/-]*$") @Size(min = 0, max = 4000) String id,
            @Valid Object body) {
        final TemplateJobApiResponse result = new TemplateJobApiResponse();

        result.setTransactionId(this.templateApiService.postTemplateGenerationJob(id, body));

        return ResponseEntity.accepted().body(result);
    }

}
