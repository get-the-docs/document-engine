package net.videki.templateutils.template.core.processor.input.xlsx;

/*-
 * #%L
 * template-utils-core
 * %%
 * Copyright (C) 2021 Levente Ban
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

/**
 * Xlsx template processor implementation based on the jXls library.
 * see http://jxls.sourceforge.net/ 
 * @author Levente Ban
 */
public class JxlsInputTemplateProcessor extends AbstractTemplateProcessor implements InputTemplateProcessor {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(JxlsInputTemplateProcessor.class);

  /**
   * Returns the input format the processor supports.
   * @return the input format (xlsx).
   */
  @Override
  public InputFormat getInputFormat() {
    return InputFormat.XLSX;
  }

  /**
     * Entry point to process the template. (all sheets in the spreadsheet)
     * @param templateFileName the template name in the template repository.
     * @param dto the model object. 
     * @return the result document stream.
   */
  @Override
  public <T> OutputStream fill(final String templateFileName, T dto) {
    OutputStream result;

    try (final InputStream is = getTemplate(templateFileName)) {

      if (is == null) {
        final String msg = String.format("Template not found: %s", templateFileName);
        LOGGER.warn(msg);

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
        LOGGER.warn(msg);

        throw new TemplateProcessException("4b7c901e-7f99-4dfe-991e-663ac15ee644", msg);

      }
    } catch (IOException e) {
      final String msg = String.format("Error opening the input template: %s", templateFileName);
      LOGGER.warn(msg);

      throw new TemplateProcessException("a2d17a80-fec7-4431-8d03-2bc3a94e23dd", msg);
    }

    return result;
  }

}
