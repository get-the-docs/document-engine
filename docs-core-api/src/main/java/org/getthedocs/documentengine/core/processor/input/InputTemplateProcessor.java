package org.getthedocs.documentengine.core.processor.input;

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

import java.io.OutputStream;

import org.getthedocs.documentengine.core.service.InputFormat;
import org.getthedocs.documentengine.core.service.exception.TemplateProcessException;

/**
 * Base interface for template processors.
 * (to fill document templates with values in model objects).
 * @author Levente Ban
 */
public interface InputTemplateProcessor {

  /**
   * Returns the input format which the processor supports. 
   * @return the input format.
   */
  InputFormat getInputFormat();

  /**
   * Entry point for document conversion.
   * @param <T> the model object class. 
   * @param templateFileName the template document's id in the template repository.
   * @param dto the model object.
   * @return the processed (filled) template, if the procesing was successful, null otherwise.
   * @throws TemplateProcessException thrown in case of technical errors during conversion.
   */
  <T> OutputStream fill(final String templateFileName, final T dto);
}
