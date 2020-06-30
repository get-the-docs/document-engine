package net.videki.templateutils.template.core.processor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTemplateProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTemplateProcessor.class);

  protected static InputStream getInputStream(final OutputStream out) {
    return new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());

  }
  
  protected static OutputStream getOutputStream() {
    return new ByteArrayOutputStream();
  }

  protected static InputStream getTemplate(final String templateFile) {
    if (TemplateServiceConfiguration.getInstance() == null) {
      final String msg = "Template configuration error: TemplateServiceConfiguration.getInstance() is null.";
      LOGGER.error(msg);

      throw new TemplateServiceRuntimeException(msg);
    } else {
      if (TemplateServiceConfiguration.getInstance().getTemplateRepository() == null) {
        final String msg = "Template configuration error: no template repository.";
        LOGGER.error(msg);

        throw new TemplateServiceRuntimeException(msg);
      }
    }
    return TemplateServiceConfiguration.getInstance().getTemplateRepository().getTemplate(templateFile);
  }

}
