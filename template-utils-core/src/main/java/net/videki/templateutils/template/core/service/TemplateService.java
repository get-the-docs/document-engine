package net.videki.templateutils.template.core.service;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;

import java.io.OutputStream;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * @author Levente Ban
 *
 * Base interface for the main entry point.
 */
public interface TemplateService {
  String CONTEXT_KEY = "ctx";

  /** Fills the given template specified by its name and return the filled document in the templates format.
   * @param templateName The template file name
   * @param dto the value object to fill the template
   * @throws TemplateServiceException if invalid parameters caught
   * @throws TemplateProcessException
   * */
  <T> OutputStream fill(final String templateName, final T dto) throws TemplateServiceException;

  /** Fills the given template specified by its name and convert if needed to the given output format. */
  <T> OutputStream fill(final String templateName, final T dto, final OutputFormat format) throws TemplateServiceException;

  /** Process a multipart template (consisting of more template files) and return one or more result documents. */
  List<OutputStream> fill(final DocumentStructure documentStructure, final ValueSet values) throws TemplateServiceException;

}
