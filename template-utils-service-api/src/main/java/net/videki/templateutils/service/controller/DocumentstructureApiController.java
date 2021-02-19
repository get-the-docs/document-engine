package net.videki.templateutils.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-16T18:11:50.335294800+01:00[Europe/Prague]")

@Controller
@RequestMapping("${openapi.documentGeneration.base-path:/api/v1}")
public class DocumentstructureApiController implements DocumentstructureApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public DocumentstructureApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
