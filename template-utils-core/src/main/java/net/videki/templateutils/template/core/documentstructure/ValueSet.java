package net.videki.templateutils.template.core.documentstructure;

import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;
import net.videki.templateutils.template.core.dto.JsonModel;

import java.util.*;

/**
 * A value set is the collection of data transfer objects to be used with a specific document structure.
 * It a basically a map holding multiple template contexts for template elements.
 *
 * @author Levente Ban
 */
public class ValueSet implements JsonModel {
    /** The document structure's unique id */
    private String documentStructureId;

    private final Map<TemplateElementId, TemplateContext> values = new HashMap<>();

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
    public ValueSet(String transactionId, Locale locale) {
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
    public Map<TemplateElementId, TemplateContext> getValues() {
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
     * @param documentStructureId the document structure id.
     */
    public void setDocumentStructureId(String documentStructureId) {
        this.documentStructureId = documentStructureId;
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
     * Returns the global template context (named 'model')
     * @return TemplateContext the context if specified
     */
    public Optional<TemplateContext> getGlobalContext() {
        final TemplateContext c = this.values.get(TemplateElementId.getGlobalTemplateElementId());
        if (c != null) {
            return Optional.of(c);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns the template context for a given template element
     * @param elementId the template element id
     * @return TemplateContext the context if specified
     */
    public Optional<TemplateContext> getContext(final TemplateElementId elementId) {
        final TemplateContext c = this.values.get(elementId);

        if (c != null) {
            return Optional.of(c);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Adds an object to the given template element to the specified context
     * @param elementId the template element (the object can be referred as a placeholder in this template
     * @param contextKey the placeholder prefix context name in the template
     * @param dto the value object to add
     * @param <T> the value object type
     * @return The related valueset (created on first, extended otherwise)
     */
    public <T> ValueSet addContext(final String elementId,
                                   final String contextKey,
                                   final T dto) {
        final TemplateContext ctx = new TemplateContext().addValueObject(contextKey, dto);

        return this.addContext(elementId, ctx);
    }

    /**
     * Adds an object for the given template element's default context (use this, if you do not want to use
     * multiple contexts within the template)
     * @param elementId the template element (the object can be referred as a placeholder in this template
     * @param dto the value object to add
     * @param <T> the value object type
     * @return The related valueset (created on first, extended otherwise)
     */
    public <T> ValueSet addDefaultContext(final String elementId,
                                   final T dto) {
        final TemplateContext ctx = new TemplateContext().addValueObject(TemplateContext.CONTEXT_ROOT_KEY_MODEL, dto);

        return this.addContext(elementId, ctx);
    }

    /**
     * Adds an existing context to the given template element, or replaces the existing occurrence of the context key
     * in the valueset.
     * @param elementId the template element (the object can be referred as a placeholder in this template
     * @param context the context to add
     * @return The related valueset (created on first, extended otherwise)
     */
    public ValueSet addContext(final String elementId,
                                   final TemplateContext context) {
        this.values.put(new TemplateElementId(elementId), context);

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
