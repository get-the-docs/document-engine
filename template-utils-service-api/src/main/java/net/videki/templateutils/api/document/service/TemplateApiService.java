/*
 * Copyright (c) 2021. Levente Ban
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.videki.templateutils.api.document.service;

import java.util.Optional;

import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;
import net.videki.templateutils.template.core.template.descriptors.TemplateDocument;

/**
 * @author Levente Ban
 * 
 *         Wraps the template registry for the API.
 */
public interface TemplateApiService {

    String TEMPLATE_PACKAGE_TEMPLATE_SEPARATOR = ":";
    String TEMPLATE_PACKAGE_SEPARATOR = "\\";

    /**
     * Returns a page of templates from the configured template repository service.
     * 
     * @param templateId Optional template id to search for.
     * @param page       the page to retrieve (effective only if the template
     *                   repository implementation has paging capability).
     * @return the requested page, if exists.
     */
    Page<TemplateDocument> getTemplates(String templateId, Pageable page);

    /**
     * Returns a template descriptor by id.
     * 
     * @param id         the template id.
     * @param version    template version for the given id, optional.
     * @param withBinary returns the template binary if specified, default false.
     * @return The template descriptor
     */
    Optional<TemplateDocument> getTemplateById(String id, String version, boolean withBinary);

    /**
     * Posts a single doument generation for the given template identified by its
     * id.
     * 
     * @param id              the template id.
     * @param body            the value object.
     * @param notificationUrl notification url, optional.
     * @return the transaction id for the document generation.
     */
    String postTemplateGenerationJob(String id, Object body, String notificationUrl);

}
