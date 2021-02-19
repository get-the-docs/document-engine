package net.videki.templateutils.service.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import net.videki.templateutils.service.service.TemplateApiService;
import net.videki.templateutils.template.core.context.JsonTemplateContext;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Service
public class TemplateApiServiceImpl implements TemplateApiService {

    public String postTemplateGenerationJob(final String id, final Object body) {
        try {
            final var templateId = decodeTemplateId(id);
            final var context = getContext(body);

            final var genResult =
                    TemplateServiceRegistry.getInstance().fillAndSave(templateId, context);

            return genResult.getTransactionId();
        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error process request {}", id);
        }

        return null;
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

    private TemplateContext getContext(final Object data) {
        if (data instanceof Map) {
            final StringWriter sw = new StringWriter();
            try {
                JSONValue.writeJSONString(data, sw);
            } catch (final IOException e) {
                throw new TemplateServiceRuntimeException("Error parsing data");
            }
            return new JsonTemplateContext((String) sw.toString());
        } else {
            throw new TemplateServiceRuntimeException("Error parsing data.");
        }

    }

}
