/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.3.1).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package net.videki.templateutils.service.controller;

import net.videki.templateutils.service.model.BaseApiResponse;
import net.videki.templateutils.service.model.DocStructureJobApiResponse;
import net.videki.templateutils.service.model.ValueSet;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-21T16:03:21.030101500+01:00[Europe/Prague]")

@Validated
@Api(value = "documentstructure", description = "the documentstructure API")
public interface DocumentstructureApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /documentstructure/{id} : Posts a template generation with the given data
     * Posts data for a document structure to be generated.
     *
     * @param id document structure id in the document structure repository (required)
     * @param valueSet Data provided for generation (required)
     * @return Default (status code 202)
     *         or Invalid DTO (status code 400)
     *         or Bad credentials (status code 401)
     *         or Forbidden (status code 403)
     *         or Template not found (status code 404)
     *         or Too many requests (status code 429)
     *         or Default (status code 200)
     */
    @ApiOperation(value = "Posts a template generation with the given data", nickname = "postDocumentStructureGenerationJob", notes = "Posts data for a document structure to be generated.", response = DocStructureJobApiResponse.class, authorizations = {
        @Authorization(value = "ApiKeyAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Default", response = DocStructureJobApiResponse.class),
        @ApiResponse(code = 400, message = "Invalid DTO", response = BaseApiResponse.class),
        @ApiResponse(code = 401, message = "Bad credentials", response = BaseApiResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BaseApiResponse.class),
        @ApiResponse(code = 404, message = "Template not found", response = BaseApiResponse.class),
        @ApiResponse(code = 429, message = "Too many requests", response = BaseApiResponse.class),
        @ApiResponse(code = 200, message = "Default", response = DocStructureJobApiResponse.class) })
    @RequestMapping(value = "/documentstructure/{id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    default ResponseEntity<DocStructureJobApiResponse> postDocumentStructureGenerationJob(@Pattern(regexp="^[a-zA-Z0-9_/-]*$") @Size(min=0,max=4000) @ApiParam(value = "document structure id in the document structure repository",required=true) @PathVariable("id") String id,@ApiParam(value = "Data provided for generation" ,required=true )  @Valid @RequestBody ValueSet valueSet) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"message\", \"transactionId\" : \"transactionId\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
