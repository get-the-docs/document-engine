package net.videki.templateutils.api.document.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import net.videki.templateutils.api.document.model.ValueSetItem;
import net.videki.templateutils.api.document.service.DocumentStructureApiService;
import net.videki.templateutils.template.core.context.JsonTemplateContext;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class DefaultDocumentStructureApiService implements DocumentStructureApiService {

  @Override
  public Page<DocumentStructure> getDocumentStructures(final String id, final Pageable page) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  @Override
  public Optional<DocumentStructure> getDocumentStructureById(final String id) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public String postDocumentStructureGenerationJob(final String id, final Object body) {
    try {
      final var docStructureId = decodeTemplateId(id);
      final var valueSet = getValueSet(body);

      final var genResult = TemplateServiceRegistry.getInstance().fillAndSaveDocumentStructureByName(docStructureId,
          valueSet);

      return genResult.getTransactionId();
    } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
      log.warn("Error processing request: {}", id);

      return null;
    }
  }

  private String decodeTemplateId(final String templateId) {
    if (templateId == null) {
      throw new TemplateServiceRuntimeException("No template id");
    }

    final String[] pathParts = templateId.split(TEMPLATE_PACKAGE_TEMPLATE_SEPARATOR);

    var path = pathParts[0].replace(TEMPLATE_PACKAGE_SEPARATOR, File.separator);
    var fileName = (pathParts.length > 1 ? pathParts[1] : "");
    if (path.endsWith("\\") || path.endsWith("/")) {
      path = path.substring(0, path.length() - 1);
    }

    return path + File.separator + fileName;
  }

  private net.videki.templateutils.template.core.documentstructure.ValueSet getValueSet(final Object data) {
    if (data instanceof Map) {
      final net.videki.templateutils.template.core.documentstructure.ValueSet result = new ValueSet();

      final var param = (net.videki.templateutils.api.document.model.ValueSet) data;

      result.setDocumentStructureId(param.getDocumentStructureId());
      result.setLocale(new Locale(param.getLocale()));
      for (final ValueSetItem actData : param.getValues()) {

        final StringWriter sw = new StringWriter();
        try {
          JSONValue.writeJSONString(actData.getValue(), sw);
        } catch (final IOException e) {
          throw new TemplateServiceRuntimeException("Error parsing data.");
        }
        result.addContext(actData.getTemplateElementId(), new JsonTemplateContext(sw.toString()));
      }
      return result;
    } else {
      throw new TemplateServiceRuntimeException("Error parsing data.");
    }

  }

}
