package net.videki.templateutils.template.core.processor.input.docx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.validation.constraints.NotNull;

import net.videki.templateutils.template.core.processor.AbstractTemplateProcessor;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wickedsource.docxstamper.DocxStamper;
import org.wickedsource.docxstamper.DocxStamperConfiguration;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;

public class DocxInputTemplateProcessor extends AbstractTemplateProcessor implements InputTemplateProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(DocxInputTemplateProcessor.class);

  @Override
  public InputFormat getInputFormat() {
    return InputFormat.DOCX;
  }

  @Override
  public <T> OutputStream fill(final String templateFileName, T dto) {
    OutputStream result = null;

    final DocxStamperConfiguration config = new DocxStamperConfiguration();
    final DocxStamper<T> stamper = new DocxStamper<>(config);

    try (final InputStream templateFile = getTemplate(templateFileName)) {

      if (templateFile != null) {
        final OutputStream out = getOutputStream();

        LOGGER.debug("Template {} found, starting fill...", templateFileName);
        stamper.stamp(templateFile, dto, out);

        LOGGER.debug("Template {} filled.", templateFileName);

        result = out;
      } else {
        final String msg = String.format("Template not found: %s", templateFileName);
        LOGGER.error(msg);

        throw new TemplateNotFoundException("e12c71e9-f27f-48ba-b600-2a0a071c5958", msg);
      }
      
    } catch (final IOException e) {
      LOGGER.error("Error reading/closing template file: {} or creating the output.", templateFileName);
    }

    return result;
  }

}
