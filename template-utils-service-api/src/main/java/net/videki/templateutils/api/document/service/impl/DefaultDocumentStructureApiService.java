package net.videki.templateutils.api.document.service.impl;

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

import lombok.extern.slf4j.Slf4j;
import net.videki.templateutils.api.document.service.DocumentStructureApiService;
import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.v1.ValueSet;
import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
    public Optional<Page<DocumentStructure>> getDocumentStructures(String id, Pageable page) {
        Optional<Page<DocumentStructure>> result = Optional.empty();

        try {
            if (log.isDebugEnabled()) {
                log.debug("getDocumentStructures - {}, page:{}", id, page);
            }

            Page<DocumentStructure> resultObj;
            if (id == null) {
                Pageable actPage;
                actPage = Objects.requireNonNullElseGet(page, Pageable::new);
                resultObj = TemplateServiceConfiguration.getInstance().getDocumentStructureRepository()
                        .getDocumentStructures(actPage);

                result = Optional.of(resultObj);
            } else {
                final Optional<DocumentStructure> doc = getDocumentStructureById(id);

                resultObj = new Page<>();
                final List<DocumentStructure> data = new LinkedList<>();
                if (doc.isPresent()) {
                    data.add(doc.get());

                    resultObj.setData(data);
                    resultObj.setNumber(0);
                    resultObj.setSize(data.size() > 0 ? 1 : 0);
                    resultObj.setTotalElements((long) data.size());
                    resultObj.setTotalPages(data.size() > 0 ? 1 : 0);

                    result = Optional.of(resultObj);
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("getDocumentStructures end - {}, item count: {}", page, resultObj.getData().size());
            }

        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: {}", page);

        }

        return result;
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

    @Override
    public void postDocumentStructureGenerationJob(String transactionId, String id, ValueSet values,
            String notificationUrl) {
        try {
            if (log.isDebugEnabled()) {
                log.debug(
                        "postDocumentStructureGenerationJob - transaction id: [{}], document structure id:[{}], notification url: [{}]",
                        transactionId, id, notificationUrl);
            }
            if (log.isTraceEnabled()) {
                log.trace(
                        "postDocumentStructureGenerationJob - transaction id: [{}], document structure id:[{}], data: [{}]",
                        transactionId, id, values);
            }

            final boolean tranDirCreated = TemplateServiceConfiguration.getInstance().getResultStore()
                    .registerTransaction(transactionId);

            if (tranDirCreated) {
                postDocumentStructureGenerationJobAsync(transactionId, id, values, notificationUrl);
            } else {
                log.error("Error creating transaction in the result store [{}]", transactionId);
            }

            if (log.isDebugEnabled()) {
                log.debug(
                        "postDocumentStructureGenerationJob end - transaction id: [{}], document structure id:[{}], notification url: [{}]",
                        transactionId, id, notificationUrl);
            }
        } catch (final TemplateServiceRuntimeException e) {
            log.warn("Error processing request: {}", id);
        }
    }

    @Async
    protected void postDocumentStructureGenerationJobAsync(final String transactionId, final String id,
            final ValueSet valueSet, final String notificationUrl) {
        try {

            TemplateServiceRegistry.getInstance().fillAndSaveDocumentStructureByName(transactionId, id, valueSet);

        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: {}", id);
        }
    }
}
