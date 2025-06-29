package org.getthedocs.documentengine.core.service;

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

import org.getthedocs.documentengine.core.documentstructure.*;
import org.getthedocs.documentengine.core.service.exception.TemplateProcessException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
/**
 * Base interface for the main entry point.
 * 
 * @author Levente Ban
 */
public interface TemplateService {

        /**
         * <p>
         * Fills the given single file template specified by its name and return the
         * filled document in the templates format.
         * </p>
         * <p>
         * The template file format has to be in the configured template provider - @see
         * TemplateServiceConfiguration
         * </p>
         * 
         * @param templateName the template file name
         * @param dto          the value object to fill the template
         * @param <T>          the value object type
         * @throws TemplateServiceException if invalid parameters caught
         * @throws TemplateProcessException thrown if the configuration/call params are
         *                                  invalid
         * @return ResultDocument a copy of the input template filled with the dto's
         *         data on success
         *
         */
        <T> ResultDocument fill(String templateName, T dto) throws TemplateServiceException;

        /**
         * <p>
         * Fills the given single file template specified by its name and return the
         * filled document in the templates format.
         * </p>
         * <p>
         * The template file format has to be in the configured template provider - @see
         * TemplateServiceConfiguration
         * </p>
         *
         * @param transactionId the transaction id, if defined.
         * @param templateName the template file name
         * @param dto          the value object to fill the template
         * @param <T>          the value object type
         * @throws TemplateServiceException if invalid parameters caught
         * @throws TemplateProcessException thrown if the configuration/call params are
         *                                  invalid
         * @return ResultDocument a copy of the input template filled with the dto's
         *         data on success
         *
         */
        <T> ResultDocument fill(String transactionId, String templateName, T dto) throws TemplateServiceException;

        /**
         * Fills the given single file template specified by its name and converts if
         * needed to the given output format.
         * 
         * @param transactionId the transaction id, if defined.
         * @param templateName  the template file name.
         * @param dto           the value object to fill the template.
         * @param format        the output format - @see OutputFormat.
         * @param <T>           the value object type.
         * @throws TemplateServiceException if invalid parameters caught.
         * @throws TemplateProcessException thrown if the configuration/call params are
         *                                  invalid.
         * @return ResultDocument a copy of the input template filled with the dto's
         *         data on success.
         *
         */
        <T> ResultDocument fill(String transactionId, String templateName, T dto, OutputFormat format)
                        throws TemplateServiceException;

        /**
         * Process a multipart template (consisting of one or more template files) and
         * return one or more result documents.
         * 
         * @param transactionId     the transaction id, if defined.
         * @param documentStructure the document structure to be filled with the
         *                          specified values.
         * @param values            the value objects for the document parts. The values
         *                          are organized into contexts where each document part
         *                          may have its own value objects and a global one -
         *                          see the template contexts in @see ValueSet.
         * @throws TemplateServiceException if invalid parameters caught
         * @return GenerationResult the result documents generated based on the input
         *         documentstructure and value set
         */
        GenerationResult fill(String transactionId, DocumentStructure documentStructure, ValueSet values)
                        throws TemplateServiceException;

        /**
         * Process a multipart template (consisting of one or more template files) and
         * return one or more result documents.
         * 
         * @param transactionId         the transaction id, if defined.
         * @param documentStructureFile the document structure file name to be filled
         *                              with the specified values.
         * @param values                the value objects for the document parts. The
         *                              values are organized into contexts where each
         *                              document part may have its own value objects and
         *                              a global one - see the template contexts in @see
         *                              ValueSet.
         * @throws TemplateServiceException if invalid parameters caught
         * @return GenerationResult the result documents generated based on the input
         *         documentstructure and value set
         */
        GenerationResult fillDocumentStructureByName(String transactionId, String documentStructureFile, ValueSet values)
                        throws TemplateServiceException;

        /**
         * <p>
         * Fills the given single file template specified by its name and return the
         * filled document in the templates format.
         * </p>
         * <p>
         * The template file format has to be in the configured template provider - @see
         * TemplateServiceConfiguration
         * </p>
         * 
         * @param templateName the template file name
         * @param dto          the value object to fill the template
         * @param <T>          the value object type
         * @throws TemplateServiceException if invalid parameters caught
         * @throws TemplateProcessException thrown if the configuration/call params are
         *                                  invalid
         * @return StoredResultDocument the result filename and its save success flag
         *
         */
        <T> StoredResultDocument fillAndSave(String templateName, T dto) throws TemplateServiceException;

        /**
         * Fills the given single file template specified by its name and converts if
         * needed to the given output format.
         * 
         * @param templateName The template file name
         * @param dto          the value object to fill the template
         * @param format       the output format - @see OutputFormat
         * @param <T>          the value object type
         * @throws TemplateServiceException if invalid parameters caught
         * @throws TemplateProcessException thrown if the configuration/call params are
         *                                  invalid
         * @return StoredResultDocument the result filename and its save success flag
         *
         */
        <T> StoredResultDocument fillAndSave(String templateName, T dto, OutputFormat format)
                        throws TemplateServiceException;

        /**
         * <p>
         * Fills the given single file template specified by its name and return the
         * filled document in the templates format.
         * </p>
         * <p>
         * The template file format has to be in the configured template provider - @see
         * TemplateServiceConfiguration
         * </p>
         *
         * @param transactionId the transaction id, if defined
         * @param templateName  the template file name
         * @param dto           the value object to fill the template
         * @param <T>           the value object type
         * @throws TemplateServiceException if invalid parameters caught
         * @throws TemplateProcessException thrown if the configuration/call params are
         *                                  invalid
         * @return StoredResultDocument the result filename and its save success flag
         *
         */
        <T> StoredResultDocument fillAndSave(String transactionId, String templateName, T dto) throws TemplateServiceException;

        /**
         * Fills the given single file template specified by its name and converts if
         * needed to the given output format.
         * 
         * @param transactionId The transaction id, if defined
         * @param templateName  The template file name
         * @param dto           the value object to fill the template
         * @param format        the output format - @see OutputFormat
         * @param <T>           the value object type
         * @throws TemplateServiceException if invalid parameters caught
         * @throws TemplateProcessException thrown if the configuration/call params are
         *                                  invalid
         * @return StoredResultDocument the result filename and its save success flag
         *
         */
        <T> StoredResultDocument fillAndSave(String transactionId, String templateName, T dto, OutputFormat format)
                        throws TemplateServiceException;

        /**
         * Process a multipart template (consisting of one or more template files) and
         * return one or more result documents.
         * 
         * @param documentStructure the document structure to be filled with the
         *                          specified values.
         * @param values            the value objects for the document parts. The values
         *                          are organized into contexts where each document part
         *                          may have its own value objects and a global one -
         *                          see the template contexts in @see ValueSet.
         * @throws TemplateServiceException if invalid parameters caught
         * @return StoredGenerationResult the result documents file names generated
         *         based on the input documentstructure and value set
         */
        StoredGenerationResult fillAndSave(DocumentStructure documentStructure, ValueSet values)
                        throws TemplateServiceException;


        /**
         * Process a multipart template (consisting of one or more template files) and
         * return one or more result documents.
         *
         * @param transactionId     the transaction id, if defined
         * @param documentStructure the document structure to be filled with the
         *                          specified values.
         * @param values            the value objects for the document parts. The values
         *                          are organized into contexts where each document part
         *                          may have its own value objects and a global one -
         *                          see the template contexts in @see ValueSet.
         * @throws TemplateServiceException if invalid parameters caught
         * @return StoredGenerationResult the result documents file names generated
         *         based on the input documentstructure and value set
         */
        StoredGenerationResult fillAndSave(String transactionId, DocumentStructure documentStructure, ValueSet values)
                        throws TemplateServiceException;                        
        /**
         * Process a multipart template (consisting of one or more template files) and
         * return one or more result documents.
         * 
         * @param documentStructureFile the document structure file name to be filled
         *                              with the specified values.
         * @param values                the value objects for the document parts. The
         *                              values are organized into contexts where each
         *                              document part may have its own value objects and
         *                              a global one - see the template contexts in @see
         *                              ValueSet.
         * @throws TemplateServiceException if invalid parameters caught
         * @return GenerationResult the result documents generated based on the input
         *         documentstructure and value set
         */
        StoredGenerationResult fillAndSaveDocumentStructureByName(String documentStructureFile,
                        ValueSet values) throws TemplateServiceException;


        /**
         * Process a multipart template (consisting of one or more template files) and
         * return one or more result documents.
         * 
         * @param transactionId         the transaction id.
         * @param documentStructureFile the document structure file name to be filled
         *                              with the specified values.
         * @param values                the value objects for the document parts. The
         *                              values are organized into contexts where each
         *                              document part may have its own value objects and
         *                              a global one - see the template contexts in @see
         *                              ValueSet.
         * @throws TemplateServiceException if invalid parameters caught
         * @return GenerationResult the result documents generated based on the input
         *         documentstructure and value set
         */
        StoredGenerationResult fillAndSaveDocumentStructureByName(String transactionId, String documentStructureFile,
                        ValueSet values) throws TemplateServiceException;

}
