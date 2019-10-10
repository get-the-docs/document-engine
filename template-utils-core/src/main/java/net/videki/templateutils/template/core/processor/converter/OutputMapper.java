package net.videki.templateutils.template.core.processor.converter;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.OutputFormat;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.io.OutputStream;

public interface OutputMapper {

  InputFormat getInputFormat();

  OutputFormat getOutputFormat();

  OutputStream convert(@NotNull final InputStream source);
}
