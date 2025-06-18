package org.getthedocs.documentengine.core.processor.converter;

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

import org.getthedocs.documentengine.core.service.InputFormat;
import org.getthedocs.documentengine.core.service.OutputFormat;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Base interface for document converters. 
 * @author Levente Ban 
 */
public interface Converter {

  /**
   * Returns the source format expected by the converter.
   * @return the input format.
   */
  InputFormat getInputFormat();

  /**
   * Returns the output format supported by the converter.
   * @return the output format.
   */
  OutputFormat getOutputFormat();

  /**
   * Entry point for conversion.
   * @param source th e source document.
   * @return the result document stream.
   * @throws ConversionException thrown in case of any errors during conversion.
   */
  OutputStream convert(final InputStream source) throws ConversionException;
}
