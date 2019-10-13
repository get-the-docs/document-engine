package net.videki.templateutils.template.core.processor.converter;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.OutputFormat;

import java.io.InputStream;
import java.io.OutputStream;

public interface Converter {

  InputFormat getInputFormat();

  OutputFormat getOutputFormat();

  OutputStream convert(final InputStream source) throws ConversionException;
}
