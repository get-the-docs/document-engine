package net.videki.templateutils.template.core.documentstructure;

import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;
import net.videki.templateutils.template.core.dto.JsonModel;

import java.util.*;

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

    public ValueSet() {
        this.transactionId = UUID.randomUUID().toString();
    }

    /**
     * <p>The actual value set which will be used when filling each doc part.</p>
     * <p>There are two kinds of contexts:</p>
     * <ol>
     *     <li>TEMPLATE_KIND_GLOBAL: global context - the value objects are used for all parts</li>
     *     <li>other given value: template element related - the value objects in the context root element
     *     will be only used when generating the template element specified by the templateElementId</li>
     * </ol>
     */
    public Map<TemplateElementId, TemplateContext> getValues() {
        return values;
    }

    public String getDocumentStructureId() {
        return documentStructureId;
    }

    public void setDocumentStructureId(String documentStructureId) {
        this.documentStructureId = documentStructureId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Optional<TemplateContext> getGlobalContext() {
        final TemplateContext c = this.values.get(TemplateElementId.TEMPLATE_KIND_GLOBAL);
        if (c != null) {
            return Optional.of(c);
        } else {
            return Optional.empty();
        }
    }

    public Optional<TemplateContext> getContext(final TemplateElementId elementId) {
        final TemplateContext c = this.values.get(elementId);

        if (c != null) {
            return Optional.of(c);
        } else {
            return Optional.empty();
        }
    }

    public <T> ValueSet addContext(final String elementId,
                                   final String contextKey,
                                   final T dto) {
        final TemplateContext ctx = new TemplateContext().addValueObject(contextKey, dto);

        return this.addContext(elementId, ctx);
    }

    public <T> ValueSet addDefaultContext(final String elementId,
                                   final T dto) {
        final TemplateContext ctx = new TemplateContext().addValueObject(TemplateContext.CONTEXT_ROOT_KEY_MODEL, dto);

        return this.addContext(elementId, ctx);
    }


    public ValueSet addContext(final String elementId,
                                   final TemplateContext context) {
        this.values.put(new TemplateElementId(elementId), context);

        return this;
    }

    public ValueSet withLocale(final Locale locale) {
        this.locale = locale;

        return this;
    }

    @Override
    public String toString() {
        return "ValueSet{" +
                "values=" + values +
                ", transactionId='" + transactionId + '\'' +
                ", locale=" + locale +
                '}';
    }
}
