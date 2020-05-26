package net.videki.templateutils.template.core.processor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;

public abstract class AbstractTemplateProcessor {

  protected static InputStream getInputStream(final OutputStream out) {
    return new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());

  }
  
  protected static OutputStream getOutputStream() {
    return new ByteArrayOutputStream();
  }

  protected static InputStream getTemplate(final String templateFile) {
    return TemplateServiceConfiguration.getInstance().getTemplateRepository().getTemplate(templateFile);
  }

}
