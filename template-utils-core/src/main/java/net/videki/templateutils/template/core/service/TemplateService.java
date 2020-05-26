package net.videki.templateutils.template.core.service;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.GenerationResult;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;

import java.io.OutputStream;

/**
 * @author Levente Ban
 *
 * Base interface for the main entry point.
 */
public interface TemplateService {

  /** <p>Fills the given single file template specified by its name and return the filled document
   * in the templates format.</p>
   * <p>The template file format has to be in the configured template provider - @see TemplateServiceConfiguration</p>
   * @param templateName the template file name
   * @param dto the value object to fill the template
   * @throws TemplateServiceException if invalid parameters caught
   * @throws TemplateProcessException thrown if the configuration/call params are invalid
   *
   * */
  <T> OutputStream fill(final String templateName, final T dto) throws TemplateServiceException;

  /** Fills the given single file template specified by its name and converts if needed to the given output format.
   * @param templateName The template file name
   * @param dto the value object to fill the template
   * @param format the output format - @see OutputFormat
   * @throws TemplateServiceException if invalid parameters caught
   * @throws TemplateProcessException thrown if the configuration/call params are invalid
   * */
  <T> OutputStream fill(final String templateName, final T dto, final OutputFormat format)
          throws TemplateServiceException;

  /** Process a multipart template (consisting of one or more template files) and return one or more result documents.
   * @param documentStructure the document structure to be filled with the specified values.
   * @param values the value objects for the document parts. The values are organized into contexts where
   *               each document part may have its own value objects and a global one - see the template contexts
   *               in @see ValueSet.
   * */
  GenerationResult fill(final DocumentStructure documentStructure, final ValueSet values)
          throws TemplateServiceException;

}
