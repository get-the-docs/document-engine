package net.videki.templateutils.template.core.processor.input.docx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.videki.templateutils.template.core.processor.AbstractTemplateProcessor;
import net.videki.templateutils.template.core.processor.input.PlaceholderEvalException;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wickedsource.docxstamper.DocxStamper;
import org.wickedsource.docxstamper.DocxStamperConfiguration;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import org.wickedsource.docxstamper.api.UnresolvedExpressionException;

public class DocxStamperInputTemplateProcessor extends AbstractTemplateProcessor implements InputTemplateProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(DocxStamperInputTemplateProcessor.class);

  @Override
  public InputFormat getInputFormat() {
    return InputFormat.DOCX;
  }

  @Override
  public <T> OutputStream fill(final String templateFileName, T dto) {
    LOGGER.debug("Start docx fill...");

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

    } catch (final TemplateNotFoundException e) {
      throw e;

    } catch (final UnresolvedExpressionException e) {
      final String msg = String.format("Placeholder error in file: %s", templateFileName);
      LOGGER.warn(msg, e);

      throw new PlaceholderEvalException("ff03cf41-25fb-463a-829d-e2b411df4c16", msg, e);

    } catch (final IOException e) {
      LOGGER.error("Error reading/closing template file: {} or creating the output.", templateFileName);
    } catch (final Exception e) {
      LOGGER.error("Error stamping the template file: {}. {}", templateFileName, e);
    }

    return result;
  }

}
