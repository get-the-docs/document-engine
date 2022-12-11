package net.videki.documentengine.core.template.descriptors;

/*-
 * #%L
 * docs-core
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

import net.videki.documentengine.core.service.InputFormat;
import net.videki.documentengine.core.service.exception.TemplateServiceConfigurationException;

import java.beans.Transient;
import java.util.Locale;

/**
 * A template document that represents a physical document template file.
 * <p>
 * Example:
 * </p>
 * <p>
 * templateName: "/mobile-new_customer-contract/02-contract_v09_en.docx"
 * </p>
 * <p>
 * locale: "en"
 * </p>
 * <p>
 * inputFormat: "DOCX"
 * </p>
 * <p>
 * version:
 * </p>
 * <p>
 * internalKey: "/mobile-new_customer-contract/02-contract_v09_en.docx"
 * </p>
 *
 * @author Levente Ban
 */
public class TemplateDocument {

    /**
     * The template name in the template repository.
     */
    private final String templateName;

    /**
     * The template document's format.
     */
    private final InputFormat format;

    /**
     * The default locale to be used with.
     */
    private final Locale locale;

    /**
     * The template version if the template repository supports versioning.
     */
    private final String version;

    /**
     * Template repository specific identifier of the template if the repository has
     * an alternate key (url for object stores, etc.).
     */
    private final String internalKey;

    /**
     * The template binary if requested.
     */
    private byte[] binary;

    /**
     * Constructor with template name only. Will use the default locale.
     *
     * @param templateName the template document name in the template repository.
     */
    public TemplateDocument(final String templateName) {
        this(templateName, Locale.getDefault(), null, null);
    }

    /**
     * Constructor with template name and locality.
     *
     * @param templateName the template document name in the template repository.
     * @param locale       the locale to be used with.
     * @throws TemplateServiceConfigurationException thrown if the format cannot be
     *                                               determined or repo access
     *                                               errors.
     */
    public TemplateDocument(final String templateName, final Locale locale) throws TemplateServiceConfigurationException {
        this(templateName, Locale.getDefault(), null, null);
    }

    /**
     * Constructor with template name and version with default locale. Designed to
     * be used with version enabled template repositories.
     *
     * @param templateName the template document name in the template repository.
     * @param version      the template version
     */
    public TemplateDocument(final String templateName, final String version) {
        this(templateName, Locale.getDefault(), version, null);
    }

    /**
     * Constructor with template name, version and repo specific key. Will use the
     * default locale.
     *
     * @param templateName the template document name in the template repository.
     * @param version      the template version.
     * @param internalKey  template repository specific key.
     */
    public TemplateDocument(final String templateName, final String version, final String internalKey) {
        this.templateName = templateName;
        this.locale = Locale.getDefault();
        this.format = InputFormat.getInputFormatForFileName(this.getTemplateName());
        this.version = version;
        this.internalKey = internalKey;
    }

    /**
     * Full constructor.
     *
     * @param templateName the template name in the repository.
     * @param locale       the locale to be used with.
     * @param version      the template version in the repository.
     * @param internalKey  the repository specific key.
     */
    public TemplateDocument(final String templateName, final Locale locale, final String version,
                            final String internalKey) {
        this.templateName = templateName;
        this.locale = locale;
        this.format = InputFormat.getInputFormatForFileName(this.getTemplateName());
        this.version = version;
        this.internalKey = internalKey;
    }

    /**
     * Returns the template name.
     *
     * @return the template name.
     */
    public String getTemplateName() {
        return this.templateName;
    }

    /**
     * Returns the locale.
     *
     * @return the locale.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Returns the input format for the template.
     *
     * @return the input format.
     */
    public InputFormat getFormat() {

        return format;
    }

    /**
     * Returns the template version, if supported by the repository.
     *
     * @return the template version.
     */
    public String getVersion() {

        return version;
    }

    /**
     * Returns the internal key of the template if it is supported by the template
     * repository.
     *
     * @return the repo specific key.
     */
    public String getInternalKey() {

        return internalKey;
    }

    /**
     * The template binary if requested.
     *
     * @return the binary.
     */
    @Transient
    public byte[] getBinary() {
        return this.binary;
    }

    /**
     * Sets the binary.
     *
     * @param binary the binary.
     */
    public void setBinary(byte[] binary) {
        this.binary = binary;
    }

    /**
     * Convenience method for logging.
     */
    @Override
    public String toString() {
        return "TemplateDocument{" + "templateName=" + templateName + ", format=" + format + ", locale=" + locale
                + ", version=" + version + ", internalKey=" + internalKey + "}";
    }
}
