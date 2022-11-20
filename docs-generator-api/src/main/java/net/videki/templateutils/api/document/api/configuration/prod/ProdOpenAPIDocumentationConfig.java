package net.videki.templateutils.api.document.api.configuration.prod;

/*-
 * #%L
 * docs-service-api
 * %%
 * Copyright (C) 2022 Levente Ban
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

import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import net.videki.templateutils.api.document.ServiceApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.Arrays;

/**
 *   OpenApi config.
 *
 *   @author Levente Ban
 */
@Profile(ServiceApplication.Profiles.PROD)
@Configuration
@PropertySources({@PropertySource("classpath:application.yml")})
public class ProdOpenAPIDocumentationConfig {

    @Value("${app.api.project-name}")
    private String projectName;

    @Value("${app.api.version}")
    private String projectVersion;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String resourceServerUrl;


    /**
     * Api info.
     * @return Api info.
     */
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                    .title(this.projectName)
                    .description("API for listing available templates, document structures and generating impersonated documents with provided data.\n" +
                            "    The API has two operation groups: \n" +
                            "      - templates for generating single documents and \n" +
                            "      - document structures for generating document sets.\n" +
                            "    The service is base on a template repository, a document structure repository and a result store. \n" +
                            "    These can be freely configured but once configured, have the same setup has to be used \n" +
                            "    for the template and document structure repositories since they have to be consistent. \n" +
                            "    The document generation is asynchronous, when posting a job you will get a transaction id (or provide by yourself) and \n" +
                            "    you can query and download the results based on that.")
                    .version(projectVersion)
                    .license(new License()
                            .name("Apache License Version 2.0")
                            .url("http://www.apache.org/licenses/")))
                .externalDocs(new ExternalDocumentation()
                        .description("Document generation API")
                        .url("https://www.getthedocs.tech/docs/documentation/components"))
                .components(
                        new Components()
                                .addSecuritySchemes("ApiKeyAuth", new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-API-KEY")
                                )
                                .addSecuritySchemes("JwtAuth", new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .description("Oauth2 flow")
                                        .openIdConnectUrl(this.resourceServerUrl + "/.well-known/openid-configuration")
                                        .flows(new OAuthFlows()
                                                .password(new OAuthFlow()
//                                                        .tokenUrl("http://localhost/realms/getthedocs/protocol/openid-connect/token")
                                                        .scopes(new Scopes()
                                                                .addString("template_reader", "List and read templates")
                                                                .addString("template_fill", "Impersonate templates")
                                                                .addString("template_manager", "Add, update and delete templates")
                                                                .addString("documentstructure_reader", "List and read document structures")
                                                                .addString("documentstructure_manager", "Add, update and delete document structures")
                                                        )
                                                )
                                        )
                                )
                );
    }
}
