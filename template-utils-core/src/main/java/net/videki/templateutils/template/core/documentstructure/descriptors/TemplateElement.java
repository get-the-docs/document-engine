package net.videki.templateutils.template.core.documentstructure.descriptors;

import net.videki.templateutils.template.core.service.InputFormat;

import java.util.*;

public class TemplateElement {

    private final TemplateElementId templateElementId;
    private String friendlyName;
    private String templateName;
    private InputFormat format;
    private List<Locale> locales;
    private Locale defaultLocale;
    private final List<Object> valueList;
    private int count;

    public TemplateElement(final String templateName) {
        super();
        this.templateElementId = new TemplateElementId();
        this.locales = new LinkedList<>();
        this.defaultLocale = Locale.getDefault();
        this.templateName = templateName;
        this.valueList = new ArrayList<>();
        this.count = 1;
    }

    public TemplateElement(final String friendlyName, final String templateName) {
        this(templateName);
        this.friendlyName = friendlyName;
    }

    public TemplateElement(final String templateName, final List<Object> valueList) {
        this(templateName);

        this.friendlyName = templateName;
        this.valueList.addAll(valueList);

    }

    public TemplateElement(final String templateName, final List<Object> valueList, int count) {
        this(templateName, valueList);

        this.count = count;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public List<Object> getValueList() {
        return valueList;
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
        return locales;
    }

    public InputFormat getFormat() {
        return format;
    }

    public void setFormat(InputFormat format) {
        this.format = format;
    }

    public TemplateElement withFriendlyName(final String friendlyName) {
        this.friendlyName = friendlyName;

        return this;
    }

    public TemplateElement withTemplateName(final String templateName) {
        this.templateName = templateName;

        return this;
    }

    public TemplateElement withFormat(final InputFormat format) {
        this.format = format;

        return this;
    }

    public TemplateElement withDefaultLocale(Locale locale) {
        this.defaultLocale = locale;

        return this;
    }

    public TemplateElement withLocales(List<Locale> locales) {
        this.locales.clear();
        this.locales.addAll(locales);

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
