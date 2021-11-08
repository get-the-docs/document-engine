package net.videki.templateutils.template.core.processor.input;

import java.io.OutputStream;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;

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
