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

package net.videki.templateutils.api.document.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import net.videki.templateutils.api.document.service.NotificationService;
import net.videki.templateutils.api.document.service.TemplateApiService;
import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.context.JsonTemplateContext;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import net.videki.templateutils.template.core.template.descriptors.TemplateDocument;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultTemplateApiService implements TemplateApiService {

  private final NotificationService notificationService;

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

  @Override
  public String postTemplateGenerationJob(final String id, final Object body, final String notificationUrl) {
    try {
      if (log.isDebugEnabled()) {
        log.debug("postTemplateGenerationJob - id:[{}], notification url: [{}]", id, notificationUrl);
      }
      if (log.isTraceEnabled()) {
        log.trace("postTemplateGenerationJob - {}, data: [{}]", id, body);
      }

      final var context = getContext(body);

      final var genResult = TemplateServiceRegistry.getInstance().fillAndSave(id, context);

      this.notificationService.notifyRequestor(notificationUrl, genResult.getTransactionId(), genResult.getFileName(),
          genResult.isGenerated());

      if (log.isDebugEnabled()) {
        log.debug("postTemplateGenerationJob end - id:[{}], notification url: [{}]", id, notificationUrl);
      }
      if (log.isTraceEnabled()) {
        log.trace("postTemplateGenerationJob end - {}, data: [{}]", id, genResult);
      }
      return genResult.getTransactionId();
    } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
      log.warn("Error processing request: id:[{}], notification url: [{}]", id, notificationUrl);
    }

    return null;
  }

  /*
   * private String decodeTemplateId(final String templateId) { if (templateId ==
   * null) { throw new TemplateServiceRuntimeException("No template id"); }
   * 
   * final String[] pathParts =
   * templateId.split(TEMPLATE_PACKAGE_TEMPLATE_SEPARATOR);
   * 
   * var path = pathParts[0].replace(TEMPLATE_PACKAGE_SEPARATOR, File.separator);
   * var fileName = (pathParts.length > 1 ? pathParts[1] : ""); if
   * (path.endsWith("\\") || path.endsWith("/")) { path = path.substring(0,
   * path.length() - 1); }
   * 
   * return path + File.separator + fileName; }
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
