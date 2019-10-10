package net.videki.templateutils.template.core.processor.input;

import java.io.OutputStream;

import net.videki.templateutils.template.core.service.InputFormat;

public interface InputTemplateProcessor {

  InputFormat getInputFormat();

  <T> OutputStream fill(final String templateFileName, final T dto);
}
