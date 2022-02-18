package net.videki.templateutils.template.core.documentstructure;

/*-
 * #%L
 * template-utils-core
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

import net.videki.templateutils.template.core.context.dto.JsonModel;
import net.videki.templateutils.template.core.context.dto.TemplateContext;

import java.util.*;

/**
 * A value set is the collection of data transfer objects to be used with a specific document structure.
 * It a basically an extension of the template context.
 *
 * @author Levente Ban
 */
public class ValueSet implements JsonModel {

    /** The document structure's unique id */
    private String documentStructureId;

    private final TemplateContext values = new TemplateContext();

    /** Value set unique id */
    private final String transactionId;

    /**
     * The locality to be used during the generation
     */
    private Locale locale;

    /**
     * Default constructor - generating a random transaction id
     */
    public ValueSet() {
        this.transactionId = UUID.randomUUID().toString();
        this.locale = Locale.getDefault();
    }

    /**
     * Constructor - specifying a given transaction id
     * @param transactionId the requested transaction id
     */
    public ValueSet(final String transactionId) {
        this.transactionId = transactionId;
        this.locale = Locale.getDefault();

    }

    /**
     * Constructor - specifying a given transaction id
     * @param documentStructureId the document structure id, for that the value set is constructed
     * @param transactionId the requested transaction id
     */
    public ValueSet(final String documentStructureId, final String transactionId) {
        this.documentStructureId = documentStructureId;
        this.transactionId = transactionId;
        this.locale = Locale.getDefault();

    }

    /**
     * Constructor - specifying a given transaction id
     * @param transactionId the requested transaction id
     * @param locale the value sets' preferred locale (based on the data provided)
     */
    public ValueSet(final String transactionId, final Locale locale) {
        this.transactionId = transactionId;
        this.locale = locale;
    }

    /**
     * <p>The actual value set which will be used when filling each doc part.</p>
     * <p>There are two kinds of contexts:</p>
     * <ol>
     *     <li>TEMPLATE_KIND_GLOBAL: global context - the value objects are used for all parts</li>
     *     <li>other given value: template element related - the value objects in the context root element
     *     will be only used when generating the template element specified by the templateElementId</li>
     * </ol>
     * @return Map the template contexts for each template element
     */
    public TemplateContext getValues() {
        return values;
    }

    /**
     * Returns the document structure id.
     * @return the document structure id.
     */
    public String getDocumentStructureId() {
        return documentStructureId;
    }

    /**
     * Sets the document structure id.
     *
     * @param documentStructureId the document structure id.
     * @return the value set.
     */
    public ValueSet withDocumentStructureId(String documentStructureId) {
        this.documentStructureId = documentStructureId;

        return this;
    }

    /**
     * Returns the locale used for document generation.
     * @return the locale.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets the locale to be used for document generation.
     * @param locale the locale.
     */
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * Returns the transaction id for which the document generation is targeted (where these values will be used).
     * @return the transaction id.
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Adds an object for the given template element's default context (use this, if you do not want to use
     * multiple contexts within the template)
     * @param dto the value object to add
     * @param <T> the value object type
     * @return The related valueset (created on first, extended otherwise)
     */
    public <T> ValueSet withDefaultContext(final T dto) {
        final TemplateContext ctx = new TemplateContext().withContext(TemplateContext.CONTEXT_ROOT_KEY, dto);

        return this.withContext(ctx);
    }

    /**
     * Adds an existing context to the given template element, or replaces the existing occurrence of the context key
     * in the valueset.
     * @param context the context to add
     * @return The related valueset (created on first, extended otherwise)
     */
    public ValueSet withContext(final TemplateContext context) {
        if (context != null) {
            context.getCtx().keySet().stream().forEach(t -> this.values.withContext(t, context.getCtx().get(t)));
        }

        return this;
    }

    /**
     * Adds an existing context to the given template element, or replaces the existing occurrence of the context key
     * in the valueset.
     * @param contextKey the context key.
     * @param <T> the DTO type.
     * @param context the context to add.
     * @return The related valueset (created on first, extended otherwise).
     */
    public <T> ValueSet withContext(final String contextKey, final T context) {
        if (context != null) {
            this.values.withContext(contextKey, context);
        }

        return this;
    }

    /**
     * Adds an existing context to the given template element, or replaces the existing occurrence of the context key
     * in the valueset.
     * @param contextStr the context to add
     * @return The related valueset (created on first, extended otherwise)
     */
    public ValueSet withContext(final String contextStr) {
        if (contextStr != null) {
            final TemplateContext contextObject = new TemplateContext().withContext(contextStr);
            withContext(contextObject);
        }

        return this;
    }

    /**
     * Builder method for defining the locale to be used during generation. 
     * @param locale the locale.
     * @return the container.
     */
    public ValueSet withLocale(final Locale locale) {
        this.locale = locale;

        return this;
    }

    public ValueSet build() {

        this.values.build();

        return this;
    } 

    /**
     * Convenience method for logging.
     */
    @Override
    public String toString() {
        return "ValueSet{" +
                "values=" + values +
                ", transactionId='" + transactionId + '\'' +
                ", locale=" + locale +
                '}';
    }
}
