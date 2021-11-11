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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

import javax.servlet.ServletContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-16T18:11:50.335294800+01:00[Europe/Prague]")


@Configuration
@EnableSwagger2
@PropertySources({@PropertySource("classpath:application.yml")})
/**
 * OpenApi Springfox config.
 * 
 * @author Levente Ban
 */
public class OpenAPIDocumentationConfig {

    /**
     * Api info.
     * @return Api info.
     */
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Document generation API")
            .description("An API for listing available templates, and impersonating.")
            .license("Apache License Version 2.0")
            .licenseUrl("http://www.apache.org/licenses/")
            .termsOfServiceUrl("")
            .version("1.0.1")
            .contact(new Contact("","", ""))
            .build();
    }

    /**
     * Controller dockects.
     * @param servletContext the servlet context.
     * @param basePath api base path.
     * @return the controller dockets.
     */
    @Bean
    public Docket customImplementation(ServletContext servletContext, @Value("${app.api.base-path:/api/v1}") String basePath) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("net.videki.templateutils.api.document.api.controller"))
                    .build()
                .pathProvider(new BasePathAwareRelativePathProvider(servletContext, basePath))
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class)
                .apiInfo(apiInfo());
    }

    /**
     * Web MVC security config. We allow everything to be flexible and config at (cloud) service level.
     * @return the MVC config.
     */
    @Bean
    public WebMvcConfigurer webConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("Content-Type");
            }
        };
    }

    /**
     * Basepath provider for springfox.
     */
    class BasePathAwareRelativePathProvider extends RelativePathProvider {
        private String basePath;

        public BasePathAwareRelativePathProvider(ServletContext servletContext, String basePath) {
            super(servletContext);
            this.basePath = basePath;
        }

        @Override
        protected String applicationPath() {
            return  Paths.removeAdjacentForwardSlashes(UriComponentsBuilder.fromPath(super.applicationPath()).path(basePath).build().toString());
        }

        @Override
        public String getOperationPath(String operationPath) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
            return Paths.removeAdjacentForwardSlashes(
                    uriComponentsBuilder.path(operationPath.replaceFirst("^" + basePath, "")).build().toString());
        }
    }

    /**
     * API key auth scheme for springfox.
     * @return auth scheme.
     */
    private SecurityScheme apiKeyAuthScheme() {
        return new ApiKey("ApiKeyAuth", "X-API-KEY", "header");
    }

    /**
     * Auth ref to api key.
     * @return the sec reference.
     */
    private SecurityReference apiKeyAuthReference() {
        return new SecurityReference("ApiKeyAuth", new AuthorizationScope[0]);
    }

    /**
     * Security context for the service paths'.
     * @return the security context.
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(apiKeyAuthReference()))
                .forPaths(PathSelectors.ant("/api/**"))
                .build();
    }

}
