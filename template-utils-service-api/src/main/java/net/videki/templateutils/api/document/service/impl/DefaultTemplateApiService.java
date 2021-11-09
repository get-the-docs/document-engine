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
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultTemplateApiService implements TemplateApiService {

  private final NotificationService notificationService;

  @Override
  public Page<TemplateDocument> getTemplates(final Pageable page) {
    try {
      if (log.isDebugEnabled()) {
        log.debug("getTemplates - {}", page);
      }

      final Page<TemplateDocument> result = TemplateServiceConfiguration.getInstance().getTemplateRepository()
          .getTemplates(page);

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
