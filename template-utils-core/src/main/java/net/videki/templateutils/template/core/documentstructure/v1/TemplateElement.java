package net.videki.templateutils.template.core.documentstructure.v1;

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

    /**
     * Template element id.
     */
    private TemplateElementId templateElementId;

    /**
     * The template names (the actual template ids to refer to)
     */
    private Map<Locale, String> templateNames;

    /**
     * The template's input format.
     */
    private InputFormat format;

    /**
     * The default locale with which the template should be used (see the language it was written).
     */
    private Locale defaultLocale;

    /**
     * The number of copies the generation result should contain. 
     */
    private int count;

    /**
     * Default constructor.
     */
    public TemplateElement() {
        super();
    }

    /**
     * Inizializes the container with a given template element id.
     * Using this, the system default locale with one copy will be used.
     * @param templateElementId the template elemeent id.
     */
    public TemplateElement(final String templateElementId) {
        super();

        init();

        this.templateElementId = new TemplateElementId(templateElementId);
        this.defaultLocale = Locale.getDefault();

        this.count = 1;
    }

    /**
     * Initializes the container with a given id, template name and locality.
     * @param templateElementId the template id (see friendly name).
     * @param templateName the template name in the template repository.
     * @param defaultLocale the locale the template should be generated for.
     * @throws TemplateServiceConfigurationException thrown if the input format cannot be determined from the template name or is not supported.
     */
    public TemplateElement(final String templateElementId, final String templateName, final Locale defaultLocale)
            throws TemplateServiceConfigurationException {
        this(templateElementId);
        withDefaultLocale(defaultLocale);
        withTemplateName(templateName, this.defaultLocale);
    }

    /**
     * Initializes the container with a given id and template name to be used with the default locale.
     * @param templateElementId the template id (see friendly name).
     * @param templateName the template name in the template repository.
     * @throws TemplateServiceConfigurationException thrown if the input format cannot be determined from the template name or is not supported.
     */
    public TemplateElement(final String templateElementId, final String templateName)
            throws TemplateServiceConfigurationException {
        this(templateElementId);
        withTemplateName(templateName, this.defaultLocale);
    }

    /**
     * Initializes the container with a given template name to be used with the default locale.
     * The id will be the same as the template name.
     * @param templateName the template name in the template repository.
     * @param count the number of copies the e generated.
     */
    public TemplateElement(final String templateName, int count) {
        this(templateName);

        this.count = count;
    }

    /**
     * Internal initializer.
     */
    private void init() {
        this.templateNames = new HashMap<>();
    }

    /**
     * Returns the template name.
     * @return the template name.
     */
    @JsonIgnore
    public String getTemplateName() {
        return getTemplateName(this.defaultLocale);
    }

    /**
     * Returns the template name for a given locale.
     * @param locale the locale.
     * @return the template name for the given locale if found.
     */
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

    /**
     * The number of copies.
     * @return the number of copies.
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the number of copies.
     * @param count the number of copies.
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Returns the template element id (see friendly name).
     * @return the template element id.
     */
    public TemplateElementId getTemplateElementId() {
        return templateElementId;
    }

    /**
     * Returns the default locale to be used.
     * @return the default locale.
     */
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * Sets the default locale.
     * @param locale the locale.
     */
    public void setDefaultLocale(Locale locale) {
        this.defaultLocale = locale;
    }

    /**
     * Returns the default locale with which the template should be generated.
     * @return the default locale.
     */
    @JsonIgnore
    public List<Locale> getLocales() {
        return new LinkedList<>(this.templateNames.keySet());
    }

    /**
     * Returns the template's input format.
     * @return the input format.
     */
    @JsonIgnore
    public InputFormat getFormat() {
        InputFormat result = this.format;
        if (this.format == null) {
            result = InputFormat.getInputFormatForFileName(this.getTemplateName());
        }

        return result;
    }

    /**
     * Sets the input format.
     * To be used in case the template element name does not contain the format, or it cannot be determined from it 
     * (because of the template repository type, etc.).
     * @param format the input format.
     */
    public void setFormat(InputFormat format) {
        this.format = format;
    }

    /**
     * Returns the template names and localities configured for the template element.
     * @return the list of template names and localities.
     */
    public Map<Locale, String> getTemplateNames() {
        return templateNames;
    }

    /**
     * Builder method to initialize the container with a template name and locale.
     * @param templateName the template name.
     * @param locale the locale for which the template will be used (for those locales with no explicit template definition the default locale will be used). 
     * @return the container.
     * @throws TemplateServiceConfigurationException thrown if the input format cannot be determined from the template name.
     */
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

    /**
     * Builder method to set the default locale.
     * @param locale the default locale.
     * @return the conatiner.
     */
    public TemplateElement withDefaultLocale(final Locale locale) {

        this.defaultLocale = locale;

        return this;
    }

    /**
     * Builder method to define the list of default locales.
     * @param newLocales the list of locales.
     * @return the container.
     */
    public TemplateElement withLocales(final List<Locale> newLocales) {
        if (newLocales != null) {
            final String templateNameForDefaultLocale = this.templateNames.get(this.defaultLocale);

            for (final Locale actLocale : newLocales) {
                this.templateNames.put(actLocale, templateNameForDefaultLocale);
            }
        }

        return this;
    }

    /**
     * Builder method to set the copy count.
     * @param count the copy count.
     * @return the container.
     */
    public TemplateElement withCount(int count) {
        this.count = count;

        return this;
    }

    /**
     * Convenience method for logging.
     */
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
