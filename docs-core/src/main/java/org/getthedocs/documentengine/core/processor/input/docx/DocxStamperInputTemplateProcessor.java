package org.getthedocs.documentengine.core.processor.input.docx;

/*-
 * #%L
 * docs-core
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

import org.getthedocs.documentengine.core.processor.input.InputTemplateProcessor;
import org.getthedocs.documentengine.core.processor.input.PlaceholderEvalException;
import org.getthedocs.documentengine.core.service.InputFormat;
import org.getthedocs.documentengine.core.service.exception.TemplateNotFoundException;
import org.getthedocs.documentengine.core.processor.AbstractTemplateProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wickedsource.docxstamper.DocxStamperConfiguration;

import org.wickedsource.docxstamper.JsonDocxStamper;
import org.wickedsource.docxstamper.api.UnresolvedExpressionException;

/**
 * Docx input template processor based on the docx stamper library. See
 * <a href="https://github.com/thombergs/docx-stamper">https://github.com/thombergs/docx-stamper</a>
 *
 * @author Levente Ban
 */
public class DocxStamperInputTemplateProcessor extends AbstractTemplateProcessor implements InputTemplateProcessor {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(DocxStamperInputTemplateProcessor.class);

  /**
   * Returns the input format.
   * 
   * @return the input format. (docx)
   */
  @Override
  public InputFormat getInputFormat() {
    return InputFormat.DOCX;
  }

  /**
   * Entry point for document generation.
   * 
   * @param templateFileName the template name in the template repository.
   * @param dto              the model object.
   * @return the result document if the processing was successful.
   */
  @Override
  public <T> OutputStream fill(final String templateFileName, T dto) {
    LOGGER.debug("Start docx fill...");

    OutputStream result = null;

    final DocxStamperConfiguration config = new DocxStamperConfiguration();
    final JsonDocxStamper<T> stamper = new JsonDocxStamper<>(config);

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
      LOGGER.warn(msg);
      LOGGER.warn("Expression eval error: ", e);

//      if (LOGGER.isDebugEnabled()) {
//        LOGGER.debug("Expression eval error: ", e);
//      }

      throw new PlaceholderEvalException("ff03cf41-25fb-463a-829d-e2b411df4c16", msg, e);

    } catch (final IOException e) {
      LOGGER.error("Error reading/closing template file: {} or creating the output.", templateFileName);
    } catch (final Exception e) {
      final String msg = String.format("Error stamping the template file: %s", templateFileName);
      LOGGER.warn(msg);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(msg, e);
      }

      throw new PlaceholderEvalException("36f17397-f921-44c6-9a6c-e2858d959c70", msg, e);
    }

    return result;
  }

}
