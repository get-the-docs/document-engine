package net.videki.templateutils.api.document;

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

import com.fasterxml.jackson.databind.Module;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Document generation REST API. 
 * 
 * @author Levente Ban
 */
@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = {"net.videki.templateutils.api.document"})
public class ServiceApplication implements CommandLineRunner {

    /**
     * Application entry point.
     * @param args system args.
     */
    public static void main(String[] args) {
        new SpringApplication(ServiceApplication.class).run(args);
    }

    /**
     * Exit code holder.
     */
    static class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }

    /**
     * Container startup entry point to be able to add the config.
     * @param arg0 system args.
     */
    @Override
    public void run(String... arg0) {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }
    /**
     * Json module inject. 
     * @return json nullable module.
     */
    @Bean
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

}
