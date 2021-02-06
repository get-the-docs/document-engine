package net.videki.templateutils.documentstructure.builder.core.documentstructure;

import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.util.Locale;
import java.util.Map;

public class OptionalTemplateElement extends TemplateElement {
    private TemplateElementId originalTemplateElementId;
    private TemplateElementOption option;

    public OptionalTemplateElement() {
    }

    public OptionalTemplateElement(String templateElementId) {
        super(templateElementId);
    }

    public OptionalTemplateElement(String templateElementId, String templateName) throws TemplateServiceConfigurationException {
        super(templateElementId, templateName);
    }

    public OptionalTemplateElement(String templateName, int count) {
        super(templateName, count);
    }

    public TemplateElementId getOriginalTemplateElementId() {
        return originalTemplateElementId;
    }

    public void setOriginalTemplateElementId(TemplateElementId originalTemplateElementId) {
        this.originalTemplateElementId = originalTemplateElementId;
    }

    public TemplateElementOption getOption() {
        return option;
    }

    public void setOption(TemplateElementOption option) {
        this.option = option;
    }

    private void replaceTemplateNames(final TemplateElement e) {
        Map<Locale, String> templateNames = e.getTemplateNames();
        templateNames.clear();
        templateNames.putAll(this.getTemplateNames());

        e.setDefaultLocale(this.getDefaultLocale());
        e.setCount(this.getCount());
        e.setFormat(this.getFormat());
    }

    public void applyElement(final TemplateElement e) {
        e.getTemplateElementId().setId(this.getTemplateElementId().getId());

        replaceTemplateNames(e);
    }

    public TemplateElement asTemplateElement() {
        final TemplateElement e = new TemplateElement(this.getTemplateElementId().getId());

        replaceTemplateNames(e);

        return e;
    }
}
