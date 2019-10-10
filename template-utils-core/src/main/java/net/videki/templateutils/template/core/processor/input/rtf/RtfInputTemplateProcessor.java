package net.videki.templateutils.template.core.processor.input.rtf;

import java.io.OutputStream;
import javax.validation.constraints.NotNull;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.processor.AbstractTemplateProcessor;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;

public class RtfInputTemplateProcessor extends AbstractTemplateProcessor implements InputTemplateProcessor {

  @Override
  public InputFormat getInputFormat() {
    return InputFormat.RTF;
  }

  @Override
  public <T> OutputStream fill(@NotNull String templateFileName, T dto) {
    throw new UnsupportedOperationException("The rtf document format is not yet implemented.");
  }

}
