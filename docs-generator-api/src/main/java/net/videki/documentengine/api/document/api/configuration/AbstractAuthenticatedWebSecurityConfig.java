package net.videki.documentengine.api.document.api.configuration;

/*-
 * #%L
 * docs-generator-api
 * %%
 * Copyright (C) 2021 - 2022 Levente Ban
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
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractAuthenticatedWebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @SuppressWarnings("unchecked")
    public static class RealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            return ((List<String>)realmAccess.get("roles")).stream()
                    .map(roleName -> "ROLE_" + roleName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }

    @Value("${swagger-resource-path}")
    private String swaggerPath;

    private final String[] AUTH_WHITELIST =
            { this.swaggerPath + "/help/**",
                    this.swaggerPath + "/swagger-ui",
                    this.swaggerPath + "/swagger**",
                    this.swaggerPath + "/swagger-resources/**",
                    this.swaggerPath + "/v3/**",
                    this.swaggerPath + "/webjars/**"};


    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
//                .and()
                .authorizeRequests()
                    .antMatchers(AUTH_WHITELIST).permitAll()
                .and()
                .oauth2ResourceServer(
                        oauth2ResourceServer -> oauth2ResourceServer.jwt(
                                jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );
    }
    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new RealmRoleConverter());
        return jwtConverter;
    }
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.GET, AUTH_WHITELIST);
    }


}