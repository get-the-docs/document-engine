package net.videki.templateutils.template.core.processor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract template processor to process a document template and produce a result document.
 * 
 * @author Levente Ban
 */
public abstract class AbstractTemplateProcessor {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTemplateProcessor.class);

  /**
   * Creates an input stream from the result's output stream.
   * @param out the output stream.
   * @return the related input stream. 
   */
  protected static InputStream getInputStream(final OutputStream out) {
    return new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());

  }
  
  /**
   * Creates a new output stream to hold the result document.
   * @return the outputstream.
   */
  protected static OutputStream getOutputStream() {
    return new ByteArrayOutputStream();
  }

  /**
   * Retrieves the template from the template repository. 
   * @param templateFile the template name.
   * @return the template binary if found.
   * @throws TemplateServiceRuntimeException thrown if the template repository is not properly configured.
   */
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
