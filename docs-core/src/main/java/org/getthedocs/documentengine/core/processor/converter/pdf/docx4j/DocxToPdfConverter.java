package org.getthedocs.documentengine.core.processor.converter.pdf.docx4j;

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

import org.getthedocs.documentengine.core.processor.converter.ConversionException;
import org.getthedocs.documentengine.core.processor.converter.Converter;
import org.getthedocs.documentengine.core.service.InputFormat;
import org.getthedocs.documentengine.core.service.OutputFormat;
import org.getthedocs.documentengine.core.configuration.TemplateServiceConfiguration;
import org.getthedocs.documentengine.core.util.FileSystemHelper;
import org.docx4j.Docx4J;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Docx to pdf converter implementation using docx4j. 
 * @author Levente Ban
 */
public class DocxToPdfConverter implements Converter {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(DocxToPdfConverter.class);

  /**
   * Docx4j font mapper.
   */
  private static final Mapper FONTMAPPER_INSTANCE = new IdentityPlusMapper();

  static {

    try {
      PhysicalFonts.discoverPhysicalFonts();

      final TemplateServiceConfiguration fontConfiguration = TemplateServiceConfiguration.getInstance();
      AdditionalFontFinder.discoverFonts(fontConfiguration.getFontConfig());
    } catch (final Exception e) {
      LOGGER.warn("Error initializing OS level fonts", e);
    }

  }

  /**
   * Returns the input format.
   * @return the input format.
   */
  @Override
  public InputFormat getInputFormat() {
    return InputFormat.DOCX;
  }

  /**
   * Returns the output format.
   * @return the output format.
   */
  @Override
  public OutputFormat getOutputFormat() {
    return OutputFormat.PDF;
  }

  /**
   * Entry point for the document conversion.
   * @param source the input document.
   * @return the converted document if the conversion was successful.
   */
  @Override
  public OutputStream convert(final InputStream source) {
    OutputStream result;

    if (source == null) {
      throw new ConversionException("7d90a4a1-14df-4d1a-87d8-fd9b146357e8", "Null input caught.");
    }

    try {
      result = FileSystemHelper.getOutputStream();

      final WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(source);

      wordMLPackage.setFontMapper(FONTMAPPER_INSTANCE);

      Docx4J.toPDF(wordMLPackage, result);

      source.close();
    } catch (final Throwable e) {
      final String msg = "Error on pdf creation.";
      LOGGER.error(msg, e);

      throw new ConversionException("c0a3ab2e-297d-4634-85cc-d171fd0772f1", msg, e);
    }

    return result;
  }

}
