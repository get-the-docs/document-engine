package net.videki.templateutils.template.core.documentstructure.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.util.*;

/**
 * A template element represents a logical document template.
 * This means a document and its available translations, but all having the same format.
 * <p>The template elements can be referred by their id and the locale used to fill in the actual template.</p>
 * <p>Example:</p>
 * <p>- templateElementId:                                               </p>
 * <p>      id: "contract"                                               </p>
 * <p>    templateNames:                                                 </p>
 * <p>      en: "/mobile-new_customer-contract/02-contract_v09_en.docx"  </p>
 * <p>      hu: "/mobile-new_customer-contract/02-contract_v09_hu.docx"  </p>
 *
 * @author Levente Ban
 */
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

    public TemplateElement(final String templateElementId, final String templateName, final Locale defaultLocale)
            throws TemplateServiceConfigurationException {
        this(templateElementId);
        withDefaultLocale(defaultLocale);
        withTemplateName(templateName, this.defaultLocale);
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
        return getTemplateName(this.defaultLocale);
    }

    public String getTemplateName(final Locale locale) {
        String result;

        Locale searchLocale = locale;
        if (searchLocale == null) {
            searchLocale = defaultLocale;
        }

            final String locStr = searchLocale.getLanguage().split("_")[0];
            Optional<Locale> actLanguage = Optional.empty();

        if (searchLocale.getCountry() != null) {
            actLanguage = this.templateNames.keySet()
                    .stream()
                    .filter(searchLocale::equals)
                    .findFirst();

        }
        if (actLanguage.isEmpty()) {
            actLanguage = this.templateNames.keySet()
                    .stream()
                    .filter(t -> locStr.equalsIgnoreCase(t.getLanguage().split("_")[0]))
                    .findFirst();
        }

        if (actLanguage.isPresent()) {
            result = this.templateNames.get(actLanguage.get());
        } else {
            if (this.templateNames.keySet().size() > 0) {
                result = this.templateNames.get(this.defaultLocale);
            } else {
                result = null;
            }
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
        InputFormat result = this.format;
        if (this.format == null) {
            result = InputFormat.getInputFormatForFileName(this.getTemplateName());
        }

        return result;
    }

    public void setFormat(InputFormat format) {
        this.format = format;
    }

    public Map<Locale, String> getTemplateNames() {
        return templateNames;
    }

    public TemplateElement withTemplateName(final String templateName, final Locale locale)
            throws TemplateServiceConfigurationException {

        if (this.defaultLocale == null) {
            this.defaultLocale = locale;
        }

        InputFormat actFormat = InputFormat.getInputFormatForFileName(this.getTemplateName());
        if (this.format == null) {
            this.format = actFormat;
        } else {
            if (!this.format.isSameFormat(actFormat)) {
                final String msg = String.format("Another format specified for the template element then " +
                                "for the items already added. " +
                                "TemplateElementId: %s, locale: %s, templateName: %s",
                        this.templateElementId, locale, templateName);

                throw new TemplateServiceConfigurationException("7d1ef0fd-839d-4190-94b3-bf84a597c9ab", msg);            }
        }

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

    @Override
    public String toString() {
        return "TemplateElement{" +
                "templateElementId=" + templateElementId +
                ", templateNames=[" + templateNames + "]" +
                ", format=" + format +
                ", defaultLocale=" + defaultLocale +
                ", count=" + count +
                "}";
    }
}
