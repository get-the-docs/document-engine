package org.getthedocs.documentengine.api.document.api.configuration.local;

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

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.getthedocs.documentengine.api.document.ServiceApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

/**
 *   OpenApi config.
 *
 *   @author Levente Ban
 */
@Profile(ServiceApplication.Profiles.LOCAL)
@Configuration
@PropertySources({@PropertySource("classpath:application.yml")})
public class LocalOpenAPIDocumentationConfig {

    @Value("${app.api.project-name}")
    private String projectName;

    @Value("${app.api.version}")
    private String projectVersion;


    /**
     * Api info.
     * @return Api info.
     */
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                    .title(projectName)
                    .description("API for listing available templates, document structures and generating impersonated documents with provided data.\n" +
                            "    The API has two operation groups: \n" +
                            "      - templates for generating single documents and \n" +
                            "      - document structures for generating document sets.\n" +
                            "    The service is base on a template repository, a document structure repository and a result store. \n" +
                            "    These can be freely configured but once configured, have the same setup has to be used \n" +
                            "    for the template and document structure repositories since they have to be consistent. \n" +
                            "    \n" +
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
                );
    }
}
