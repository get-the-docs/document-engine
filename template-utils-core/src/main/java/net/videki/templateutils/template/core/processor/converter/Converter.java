package net.videki.templateutils.template.core.processor.converter;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.OutputFormat;

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
