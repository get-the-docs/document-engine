package org.getthedocs.documentengine.api.document.api.configuration.prod;

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

import org.getthedocs.documentengine.api.document.api.configuration.AbstractAuthenticatedWebSecurityConfig;
import org.getthedocs.documentengine.api.document.ServiceApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Web security config for the application.
 * 
 * @author Levente Ban
 */
@Profile(ServiceApplication.Profiles.PROD)
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
@Configuration
public class ProdWebSecurityConfig extends AbstractAuthenticatedWebSecurityConfig {

}