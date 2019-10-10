package net.videki.templateutils.template.core.documentstructure;

import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;

import javax.validation.constraints.NotNull;
import java.util.*;

public class ValueSet {
    private final Map<TemplateElementId, TemplateContext> values = new HashMap<>();

    /**
     * The locality to be used during the generation
     */
    private Locale locale;

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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Optional<TemplateContext> getGlobalContext() {
        final TemplateContext c = this.values.get(TemplateElementId.TEMPLATE_KIND_GLOBAL);
        if (c != null) {
            return Optional.of(c);
        } else {
            return Optional.empty();
        }
    }

    public Optional<TemplateContext> getContext(@NotNull final TemplateElementId elementId) {
        final TemplateContext c = this.values.get(elementId);
        if (c != null) {
            return Optional.of(c);
        } else {
            return Optional.empty();
        }
    }

}
