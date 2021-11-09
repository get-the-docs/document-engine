package net.videki.templateutils.api.document.api.controller;

import net.videki.templateutils.api.document.service.DocumentStructureApiService;
import net.videki.templateutils.api.document.api.model.DocStructureJobApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-21T15:16:34.406301500+01:00[Europe/Prague]")

@RequiredArgsConstructor
@Controller
@RequestMapping("${app.api.base-path:/api/v1}")
public class ResultApiController implements ResultApi {

    private final NativeWebRequest request;

    private final DocumentStructureApiService documentStructureApiService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }


}
