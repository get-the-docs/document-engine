package net.videki.templateutils.template.core.processor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.videki.templateutils.template.core.service.TemplateService;

public abstract class AbstractTemplateProcessor {

  private static final Logger LOGGER      = LoggerFactory.getLogger(TemplateService.class);

  protected static InputStream getInputStream(final OutputStream out) {
    return new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());

  }
  
  protected static OutputStream getOutputStream() {
    return new ByteArrayOutputStream();
  }

  protected static InputStream getTemplate(final String templateFile) {
    InputStream result;

    result = AbstractTemplateProcessor.class.getResourceAsStream(templateFile);
    if (result == null) {
      LOGGER.error("Template not found. File: {}. ", templateFile);
    } else {
      LOGGER.debug("Template found. File: {}. ", templateFile);
    }

    return result;

  }

}
