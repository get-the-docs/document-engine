package net.videki.templateutils.template.core.processor.input.xlsx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.videki.templateutils.template.core.processor.AbstractTemplateProcessor;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;

public class XlsxInputTemplateProcessor extends AbstractTemplateProcessor implements InputTemplateProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(XlsxInputTemplateProcessor.class);

  @Override
  public InputFormat getInputFormat() {
    return InputFormat.XLSX;
  }

  @Override
  public <T> OutputStream fill(final String templateFileName, T dto) {
    OutputStream result;

    try (final InputStream is = getTemplate(templateFileName)) {

      if (is == null) {
        final String msg = String.format("Template not found: %s", templateFileName);
        LOGGER.error(msg);

        throw new TemplateNotFoundException("3985eb36-6274-4870-af3a-c73a5c499873", msg);

      }

      Context context = new Context();
      context.putVar("model", dto);
      
//      Transformer transformer = TransformerFactory.createTransformer(is, out); // this throws error and returns null object
//      ExpressionEvaluator evaluator = new 
//      transformer.getTransformationConfig().setExpressionEvaluator(evaluator);   

      try (final OutputStream out = getOutputStream()) {

        LOGGER.debug("Template {} found, starting fill...", templateFileName);
        JxlsHelper.getInstance().processTemplate(is, out, context);

        LOGGER.debug("Template {} filled.", templateFileName);

        result = out;
      } catch (IOException e) {
        final String msg = String.format("Error creating the output for the template: %s", templateFileName);
        LOGGER.error(msg);

        throw new TemplateProcessException("4b7c901e-7f99-4dfe-991e-663ac15ee644", msg);

      }
    } catch (IOException e) {
      final String msg = String.format("Error opening the input template: %s", templateFileName);
      LOGGER.error(msg);

      throw new TemplateProcessException("a2d17a80-fec7-4431-8d03-2bc3a94e23dd", msg);
    }

    return result;
  }

}
