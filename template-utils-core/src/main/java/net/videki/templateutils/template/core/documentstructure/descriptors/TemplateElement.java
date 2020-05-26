package net.videki.templateutils.template.core.documentstructure.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.util.*;

public class TemplateElement {

    private TemplateElementId templateElementId;
    private Map<Locale, String> templateNames;
    private InputFormat format;
    private Locale defaultLocale;
    private int count;

    public TemplateElement() {
        super();
    }

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

    @JsonIgnore
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

    public void setCount(int count) {
        this.count = count;
    }

    public TemplateElementId getTemplateElementId() {
        return templateElementId;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(Locale locale) {
        this.defaultLocale = locale;
    }

    @JsonIgnore
    public List<Locale> getLocales() {
        return new LinkedList<>(this.templateNames.keySet());
    }

    @JsonIgnore
    public InputFormat getFormat() {
        return format;
    }

    public void setFormat(InputFormat format) {
        this.format = format;
    }

    public Map<Locale, String> getTemplateNames() {
        return templateNames;
    }
/*
    public void setTemplateNames(Map<String, String> templateNames) {
        final Map<Locale, String> tmpMap = new HashMap<>();

        for (final String actKey : templateNames.keySet()) {
            tmpMap.put(new Locale(actKey), templateNames.get(actKey));
        }
        this.templateNames.clear();
        this.templateNames.putAll(tmpMap);
    }
*/
    public TemplateElement withTemplateName(final String templateName, final Locale locale)
            throws TemplateServiceConfigurationException {

        if (this.templateNames.containsKey(locale)) {
            final String msg = String.format("The locale is already specified for the template element. " +
                    "TemplateElementId: %s, locale: %s, templateName: %s",
                    this.templateElementId, locale, templateName);

            throw new TemplateServiceConfigurationException("a5ebf647-b52a-43c8-b4d7-7788fa7d2398", msg);
        }

        if (this.templateElementId == null) {
            this.templateElementId = new TemplateElementId(templateName);
        }
        this.templateNames.put(locale, templateName);

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

}
