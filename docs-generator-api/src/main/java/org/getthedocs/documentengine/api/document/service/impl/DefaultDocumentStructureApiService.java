package org.getthedocs.documentengine.api.document.service.impl;

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

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.getthedocs.documentengine.api.document.service.DocumentStructureApiService;
import org.getthedocs.documentengine.core.configuration.TemplateServiceConfiguration;
import org.getthedocs.documentengine.core.context.JsonTemplateContext;
import org.getthedocs.documentengine.core.documentstructure.DocumentStructure;
import org.getthedocs.documentengine.core.documentstructure.ValueSet;
import org.getthedocs.documentengine.core.documentstructure.descriptors.TemplateElementId;
import org.getthedocs.documentengine.core.provider.persistence.Page;
import org.getthedocs.documentengine.core.provider.persistence.Pageable;
import org.getthedocs.documentengine.core.service.TemplateServiceRegistry;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceRuntimeException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Document structure API.
 * 
 * @author Levente Ban
 */
@Slf4j
@Service
public class DefaultDocumentStructureApiService implements DocumentStructureApiService {

    /**
     * Returns a page of document structures from the configured document structure
     * repository service.
     * 
     * @param id   the document structure's id in the repository.
     * @param page the page to retrieve (effective only if the document structure
     *             repository implementation has paging capability).
     * @return the requested page, if exists.
     */
    @Override
    public Page<DocumentStructure> getDocumentStructures(String id, Pageable page) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("getDocumentStructures - {}", page);
            }

            final Page<DocumentStructure> result;
            if (id != null) {
                result = TemplateServiceConfiguration.getInstance().getDocumentStructureRepository().getDocumentStructures(page);

            } else {
                result = new Page<>();
                result.setNumber(0);
                result.setSize(0);
                result.setTotalElements((long)0);
                result.setTotalPages(0);
            }

            if (log.isDebugEnabled()) {
                log.debug("getDocumentStructures end - {}, item count: {}", page, result.getData().size());
            }
            return result;
        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: {}", page);

            return null;
        }
    }

    @Override
    public Optional<DocumentStructure> getDocumentStructureById(final String id) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("getDocumentStructureById - {}", id);
            }

            final DocumentStructure ds = TemplateServiceConfiguration.getInstance().getDocumentStructureRepository()
                    .getDocumentStructure(id);

            if (log.isDebugEnabled()) {
                log.debug("getDocumentStructureById end - {}", id);
            }
            if (log.isTraceEnabled()) {
                log.trace("getDocumentStructureById end - {}, data: [{}]", id, ds);
            }

            return Optional.ofNullable(ds);

        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: {}", id);

            return Optional.empty();
        }
    }

    @Async
    public void postDocumentStructureGenerationJob(final String transactionId, final String id, final Object body,
            final String notificationUrl) {
        try {
            final var valueSet = getValueSet(body);

            TemplateServiceRegistry.getInstance().fillAndSaveDocumentStructureByName(transactionId, id, valueSet);

        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: {}", id);
        }
    }

    private ValueSet getValueSet(final Object data) {
        if (data instanceof Map) {
            final ValueSet result = new ValueSet();

            final var param = (ValueSet) data;

            result.setDocumentStructureId(param.getDocumentStructureId());
            result.setLocale(new Locale(param.getLocale().toString()));
            for (final TemplateElementId actData : param.getValues().keySet()) {

                final StringWriter sw = new StringWriter();
                try {
                    JSONValue.writeJSONString(param.getValues().get(actData), sw);
                } catch (final IOException e) {
                    throw new TemplateServiceRuntimeException("Error parsing data.");
                }
                result.addContext(actData.getId(), new JsonTemplateContext(sw.toString()));
            }
            return result;
        } else {
            throw new TemplateServiceRuntimeException("Error parsing data.");
        }

    }

}
