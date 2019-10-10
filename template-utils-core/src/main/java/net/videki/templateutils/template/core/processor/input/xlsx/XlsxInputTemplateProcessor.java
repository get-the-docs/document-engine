package net.videki.templateutils.template.core.processor.input.xlsx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.validation.constraints.NotNull;

import net.videki.templateutils.template.core.processor.AbstractTemplateProcessor;
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
  public <T> OutputStream fill(@NotNull String templateFileName, T dto) {
    OutputStream result = null;

    try (final InputStream is = XlsxInputTemplateProcessor.class.getResourceAsStream(templateFileName);
         final OutputStream out = getOutputStream()) {

      Context context = new Context();
      context.putVar("model", dto);
      
//      Transformer transformer = TransformerFactory.createTransformer(is, out); // this throws error and returns null object
//      ExpressionEvaluator evaluator = new 
//      transformer.getTransformationConfig().setExpressionEvaluator(evaluator);   

      LOGGER.debug("Template {} found, starting fill...", templateFileName);
      JxlsHelper.getInstance().processTemplate(is, out, context);

      LOGGER.debug("Template {} filled.", templateFileName);

      result = out;
    } catch (IOException e) {
      LOGGER.error("Error reading/closing template file: {} or creating the output.", templateFileName);
    }

    return result;
  }

}
