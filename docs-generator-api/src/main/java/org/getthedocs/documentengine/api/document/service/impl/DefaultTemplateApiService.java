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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.getthedocs.documentengine.api.document.service.NotificationService;
import org.getthedocs.documentengine.api.document.service.TemplateApiService;
import org.getthedocs.documentengine.core.configuration.TemplateServiceConfiguration;
import org.getthedocs.documentengine.core.context.JsonTemplateContext;
import org.getthedocs.documentengine.core.documentstructure.StoredGenerationResult;
import org.getthedocs.documentengine.core.documentstructure.StoredResultDocument;
import org.getthedocs.documentengine.core.provider.persistence.Page;
import org.getthedocs.documentengine.core.provider.persistence.Pageable;
import org.getthedocs.documentengine.core.service.TemplateServiceRegistry;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceRuntimeException;
import org.getthedocs.documentengine.core.template.descriptors.TemplateDocument;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Wraps the template registry for the API.
 * 
 * @author Levente Ban
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultTemplateApiService implements TemplateApiService {

    /**
     * Notification service for indicating document generation process end events to
     * the callers.
     */
    private final NotificationService notificationService;

    /**
     * Returns a page of templates from the configured template repository service.
     * 
     * @param templateId Optional template id to search for.
     * @param page       the page to retrieve (effective only if the template
     *                   repository implementation has paging capability).
     * @return the requested page, if exists.
     */
    @Override
    public Optional<Page<TemplateDocument>> getTemplates(String templateId, final Pageable page) {
        Optional<Page<TemplateDocument>> result = Optional.empty();
 
        try {
            if (log.isDebugEnabled()) {
                log.debug("getTemplates - {}, page:{}", templateId, page);
            }

            Page<TemplateDocument> resultObj;
            if (templateId == null) {
                Pageable actPage;
                actPage = Objects.requireNonNullElseGet(page, Pageable::new);
                resultObj = TemplateServiceConfiguration.getInstance().getTemplateRepository().getTemplates(actPage);

                result = Optional.of(resultObj); 
            } else {
                final Optional<TemplateDocument> doc = getTemplateById(templateId, null, false);

                resultObj = new Page<>();
                final List<TemplateDocument> data = new LinkedList<>();
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
                log.debug("getTemplates end - {}, item count: {}", page, resultObj.getData().size());
            }

        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: {}", page);

        }

        return result;
    }

    /**
     * Returns a template descriptor by id.
     * 
     * @param id         the template id.
     * @param version    template version for the given id, optional.
     * @param withBinary returns the template binary if specified, default false.
     * @return The template descriptor
     */
    @Override
    public Optional<TemplateDocument> getTemplateById(final String id, final String version, final boolean withBinary) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("getTemplateById - {}/{}, binary: {}", id, version, withBinary);
            }

            final Optional<TemplateDocument> result = TemplateServiceConfiguration.getInstance().getTemplateRepository()
                    .getTemplateDocumentById(id, version, withBinary);

            if (log.isDebugEnabled()) {
                log.debug("getTemplateById end - {}", id);
            }
            if (log.isTraceEnabled()) {
                log.trace("getTemplateById end - {}, data: [{}]", id, result);
            }
            return result;

        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: {}", id);

            return Optional.empty();
        }
    }

    /**
     * Posts a single document generation for the given template identified by its
     * id. It registers the transaction id in the result store and returns immediately.
     * 
     * @param transactionId   the transaction id.
     * @param id              the template id.
     * @param body            the value object.
     * @param notificationUrl notification url, optional.
     */
    @Override
    public void postTemplateGenerationJob(final String transactionId, final String id, final Object body,
            final String notificationUrl) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("postTemplateGenerationJob - transaction id: [{}], template id:[{}], notification url: [{}]", transactionId, id, notificationUrl);
            }
            if (log.isTraceEnabled()) {
                log.trace("postTemplateGenerationJob - transaction id: [{}], template id:[{}], data: [{}]", transactionId, id, body);
            }

            final boolean tranDirCreated =
                    TemplateServiceConfiguration.getInstance().getResultStore().registerTransaction(transactionId);

            if (tranDirCreated) {
                postTemplateGenerationJobAsync(transactionId, id, body, notificationUrl);
            } else {
                log.error("Error creating transaction in the result store [{}]", transactionId);
            }

            if (log.isDebugEnabled()) {
                log.debug("postTemplateGenerationJob end - transaction id: [{}], template id:[{}], notification url: [{}]", transactionId, id, notificationUrl);
            }

        } catch (final TemplateServiceRuntimeException e) {
            log.warn("Error processing request: transaction id: [{}], template id:[{}], notification url: [{}]", transactionId, id, notificationUrl);
        }

    }

    /**
     * Internal wrapper to post a single document generation for the given template identified by its
     * id.
     *
     * @param transactionId   the transaction id.
     * @param id              the template id.
     * @param body            the value object.
     * @param notificationUrl notification url, optional.
     */
    @Async
    protected void postTemplateGenerationJobAsync(final String transactionId, final String id, final Object body,
                                          final String notificationUrl) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("postTemplateGenerationJobAsync - transaction id: [{}], template id:[{}], notification url: [{}]", transactionId, id, notificationUrl);
            }
            if (log.isTraceEnabled()) {
                log.trace("postTemplateGenerationJobAsync - transaction id: [{}] template id: [{}], data: [{}]", transactionId, id, body);
            }

            final var genResult = TemplateServiceRegistry.getInstance().fillAndSave(transactionId, id,
                    getContext(body));

            this.notificationService.notifyRequestor(notificationUrl, genResult.getTransactionId(),
                    genResult.getFileName(), genResult.isGenerated());

            if (log.isDebugEnabled()) {
                log.debug("postTemplateGenerationJobAsync end - transaction id: [{}], template id:[{}], notification url: [{}]", transactionId, id, notificationUrl);
            }
            if (log.isTraceEnabled()) {
                log.trace("postTemplateGenerationJobAsync end - transaction id: [{}], template id: [{}], data: [{}]", transactionId, id, genResult);
            }
        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: transaction id: [{}], template id:[{}], notification url: [{}]", transactionId, id, notificationUrl);
        }

    }

    /**
     * Returns a result document descriptor, if the generation process has finished.
     * 
     * @param transactionId the transaction id.
     * @return the result document descriptor if found.
     * @throws TemplateServiceException thrown in case of any error during retrieval.
     */
    @Override
    public Optional<StoredGenerationResult> getResultDocumentByTransactionId(final String transactionId)
            throws TemplateServiceException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("getResultDocumentByTransactionId - id:[{}]", transactionId);
            }

            final var genResult = TemplateServiceConfiguration.getInstance().getResultStore()
                    .getGenerationResultByTransactionId(transactionId);

            if (log.isDebugEnabled()) {
                log.debug("getResultDocumentByTransactionId end - id:[{}]", transactionId);
            }
            if (log.isTraceEnabled()) {
                log.trace("getResultDocumentByTransactionId end - {}, data: [{}]", transactionId, genResult);
            }

            return genResult;
        } catch (final TemplateServiceException e) {
            log.error("getResultDocumentByTransactionId - Error processing request: id:[{}]", transactionId);

            throw e;
        }
    }

    /**
     * Returns a result document descriptor, if the generation process has finished.
     * 
     * @param transactionId the transaction id.
     * @param documentId    the document id as stored in the result store.
     * @param withBinary    whether the document binary also has to be returned.
     * @return the result document descriptor if found.
     */
    @Override
    public Optional<StoredResultDocument> getResultDocumentByTransactionIdAndDocumentId(final String transactionId,
            final String documentId, final boolean withBinary) throws TemplateServiceException {

        if (log.isDebugEnabled()) {
            log.debug(
                    "getResultDocumentByTransactionIdAndDocumentId - transaction id:[{}], document: [{}]. Binary requested: {}.",
                    transactionId, documentId, withBinary);
        }

        final var result = TemplateServiceConfiguration.getInstance().getResultStore()
                .getResultDocumentByTransactionId(transactionId, documentId, withBinary);

        if (log.isDebugEnabled()) {
            if (result.isPresent() && result.get().getBinary() != null) {
                log.debug(
                        "getResultDocumentByTransactionIdAndDocumentId end - transaction id:[{}], document: [{}]. Binary requested: {} - doc size: {}.",
                        transactionId, documentId, withBinary, result.get().getBinary().length);
            } else {
                log.debug(
                        "getResultDocumentByTransactionIdAndDocumentId end - transaction id:[{}], document: [{}]. Binary requested: {} - doc not present or empty.",
                        transactionId, documentId, withBinary);
            }
        }
        return result;
    }

    private JsonTemplateContext getContext(final Object data) {
        if (data instanceof Map) {
            log.debug("getContext - map...");
            final StringWriter sw = new StringWriter();
            try {
                JSONValue.writeJSONString(data, sw);
            } catch (final IOException e) {
                throw new TemplateServiceRuntimeException("Error parsing data.");
            }
            final JsonTemplateContext result = new JsonTemplateContext((String) sw.toString());
            log.debug("getContext - map, parse ok.");

            return result;
        } else if (data instanceof String){
            log.debug("getContext - plain string...");
            final JsonTemplateContext result = new JsonTemplateContext((String) data);
            log.debug("getContext - plain string, parse ok.");

            return result;
        } else {
            log.warn("getContext - object caught at rest api level, should be string or map.");

            throw new TemplateServiceRuntimeException("Error parsing data.");
        }

    }

}
