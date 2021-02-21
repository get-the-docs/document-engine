package net.videki.templateutils.service.controller;

import net.videki.templateutils.service.model.DocStructureJobApiResponse;
import net.videki.templateutils.service.model.TemplateJobApiResponse;
import net.videki.templateutils.service.model.ValueSet;
import net.videki.templateutils.service.service.TemplateApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-21T15:16:34.406301500+01:00[Europe/Prague]")

@Controller
@RequestMapping("${openapi.documentGeneration.base-path:/api/v1}")
public class DocumentstructureApiController implements DocumentstructureApi {

    private final NativeWebRequest request;


    @Autowired
    private TemplateApiService templateApiService;

    @Autowired
    public DocumentstructureApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<DocStructureJobApiResponse> postDocumentStructureGenerationJob(
            @Pattern(regexp = "^[a-zA-Z0-9_/-]*$") @Size(min = 1, max = 200) String id,
            @Valid ValueSet valueSet) {
        final DocStructureJobApiResponse result = new DocStructureJobApiResponse();

        result.setTransactionId(this.templateApiService.postDocumentStructureGenerationJob(id, valueSet));

        return ResponseEntity.accepted().body(result);
    }
}
