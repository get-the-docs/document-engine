package net.videki.templateutils.template.core.documentstructure.descriptors;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.util.*;

public class TemplateElement {

    private TemplateElementId templateElementId;
    private Map<Locale, String> templateNames;
    private InputFormat format;
    private Locale defaultLocale;
    private int count;

    public TemplateElement(final String templateElementId) {
        super();

        init();

        this.templateElementId = new TemplateElementId(templateElementId);
        this.defaultLocale = Locale.getDefault();

        this.count = 1;
    }

    public TemplateElement(final String templateElementId, final String templateName)
            throws TemplateServiceConfigurationException {
        this(templateElementId);
        withTemplateName(templateName, this.defaultLocale);
    }

    public TemplateElement(final String templateName, int count) {
        this(templateName);

        this.count = count;
    }

    private void init() {
        this.templateNames = new HashMap<>();
    }

    public String getTemplateName() {
        return this.templateNames.get(this.defaultLocale);
    }

    public String getTemplateName(final Locale locale) {
        String result = this.templateNames.get(locale);

        if (result == null) {
            result = getTemplateName();
        }

        return result;
    }

    public int getCount() {
        return count;
    }

    public TemplateElementId getTemplateElementId() {
        return templateElementId;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public List<Locale> getLocales() {
        return new LinkedList<>(this.templateNames.keySet());
    }

    public InputFormat getFormat() {
        return format;
    }

    public void setFormat(InputFormat format) {
        this.format = format;
    }

    public TemplateElement withTemplateName(final String templateName, final Locale locale)
            throws TemplateServiceConfigurationException {

        this.templateNames.put(locale, templateName);
        if (this.templateElementId == null) {
            this.templateElementId = new TemplateElementId(templateName);
        }
        InputFormat format = InputFormat.getInputFormatForFileName(templateName);

        if (this.templateNames.keySet().contains(format)) {
            final String msg = String.format("The locale is already specified for the template element. " +
                    "TemplateElementId: %s, locale: %s, templateName: %s",
                    this.templateElementId, locale, templateName);

            throw new TemplateServiceConfigurationException("a5ebf647-b52a-43c8-b4d7-7788fa7d2398", msg);
        }

        return this;
    }

    public TemplateElement withDefaultLocale(final Locale locale) {

        this.defaultLocale = locale;

        return this;
    }

    public TemplateElement withLocales(final List<Locale> newLocales) {
        if (newLocales != null) {
            final String templateNameForDefaultLocale = this.templateNames.get(this.defaultLocale);

            for (final Locale actLocale : newLocales) {
                this.templateNames.put(actLocale, templateNameForDefaultLocale);
            }
        }

        return this;
    }

    public TemplateElement withCount(int count) {
        this.count = count;

        return this;
    }

    public void setDefaultLocale(Locale locale) {
        this.defaultLocale = locale;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
