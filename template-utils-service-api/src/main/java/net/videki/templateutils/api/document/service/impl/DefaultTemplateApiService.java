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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import net.videki.templateutils.api.document.service.NotificationService;
import net.videki.templateutils.api.document.service.TemplateApiService;
import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.context.JsonTemplateContext;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.StoredGenerationResult;
import net.videki.templateutils.template.core.documentstructure.StoredResultDocument;
import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import net.videki.templateutils.template.core.template.descriptors.TemplateDocument;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Page<TemplateDocument> getTemplates(String templateId, final Pageable page) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("getTemplates - {}", page);
            }

            Page<TemplateDocument> result = null;
            if (templateId != null) {
                result = TemplateServiceConfiguration.getInstance().getTemplateRepository().getTemplates(page);

            } else {
                final Optional<TemplateDocument> doc = getTemplateById(templateId, null, false);

                result = new Page<>();
                final List<TemplateDocument> data = new LinkedList<>();
                if (doc.isPresent()) {
                    data.add(doc.get());
                }
                result.setData(data);
                result.setNumber(0);
                result.setSize(data.size() > 0 ? 1 : 0);
                result.setTotalElements((long) data.size());
                result.setTotalPages(data.size() > 0 ? 1 : 0);
            }

            if (log.isDebugEnabled()) {
                log.debug("getTemplates end - {}, item count: {}", page, result.getData().size());
            }
            return result;

        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: {}", page);

            return null;
        }
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

            return null;
        }
    }

    /**
     * Posts a single doument generation for the given template identified by its
     * id.
     * 
     * @param transactionId   the trasaction id.
     * @param id              the template id.
     * @param body            the value object.
     * @param notificationUrl notification url, optional.
     * @return the transaction id for the document generation.
     */
    @Async
    @Override
    public void postTemplateGenerationJob(final String transactionId, final String id, final Object body,
            final String notificationUrl) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("postTemplateGenerationJob - id:[{}], notification url: [{}]", id, notificationUrl);
            }
            if (log.isTraceEnabled()) {
                log.trace("postTemplateGenerationJob - {}, data: [{}]", id, body);
            }

            final var context = getContext(body);

            final var genResult = TemplateServiceRegistry.getInstance().fillAndSave(id, context);

            this.notificationService.notifyRequestor(notificationUrl, genResult.getTransactionId(),
                    genResult.getFileName(), genResult.isGenerated());

            if (log.isDebugEnabled()) {
                log.debug("postTemplateGenerationJob end - id:[{}], notification url: [{}]", id, notificationUrl);
            }
            if (log.isTraceEnabled()) {
                log.trace("postTemplateGenerationJob end - {}, data: [{}]", id, genResult);
            }
        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: id:[{}], notification url: [{}]", id, notificationUrl);
        }

    }

    /**
     * Returns a result document descriptor, if the generation process has finished.
     * 
     * @param transactionId the transaction id.
     * @return the result document descriptor if found.
     * @throws TemplateServiceException
     */
    @Override
    public Optional<StoredGenerationResult> getResultDocumentByTransactionId(String transactionId)
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
                        transactionId, documentId, withBinary, result.get().getBinary().length);
            }
        }
        return result;
    }

    /**
     * Creates a context object from the incoming object.
     * 
     * @param data the raw model data.
     * @return the template context.
     */
    private TemplateContext getContext(final Object data) {
        if (data instanceof Map) {
            final StringWriter sw = new StringWriter();
            try {
                JSONValue.writeJSONString(data, sw);
            } catch (final IOException e) {
                throw new TemplateServiceRuntimeException("Error parsing data.");
            }
            return new JsonTemplateContext((String) sw.toString());
        } else {
            throw new TemplateServiceRuntimeException("Error parsing data.");
        }

    }

}
