package net.videki.templateutils.api.document.api.configuration;

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

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import java.util.Collections;

/**
 *   OpenApi config.
 *
 *   @author Levente Ban
 */
@Configuration
@PropertySources({@PropertySource("classpath:application.yml")})
public class OpenAPIDocumentationConfig {

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
                    .description("An API for listing available templates, and impersonating.")
                    .version(projectVersion)
                    .license(new License()
                            .name("Apache License Version 2.0")
                            .url("http://www.apache.org/licenses/")))
                .externalDocs(new ExternalDocumentation()
                        .description("Document generation API")
                        .url("https://www.getthedocs.tech/docs/documentation/components"))
                .security(Collections.singletonList(new SecurityRequirement().addList("ApiKeyAuth")));
    }
}
