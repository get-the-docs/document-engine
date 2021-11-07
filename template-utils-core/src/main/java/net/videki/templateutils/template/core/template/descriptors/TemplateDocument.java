package net.videki.templateutils.template.core.template.descriptors;

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.beans.Transient;
import java.util.*;

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

  private final String templateName;
  private final InputFormat format;
  private final Locale locale;
  private final String version;
  private final String internalKey;
  private byte[] binary;

  public TemplateDocument(final String templateName) {
    this(templateName, Locale.getDefault(), null, null);
  }

  public TemplateDocument(final String templateName, final Locale locale) throws TemplateServiceConfigurationException {
    this(templateName, Locale.getDefault(), null, null);
  }

  public TemplateDocument(final String templateName, final String version) {
    this(templateName, Locale.getDefault(), version, null);
  }

  public TemplateDocument(final String templateName, final String version, final String internalKey) {
    this.templateName = templateName;
    this.locale = Locale.getDefault();
    this.format = InputFormat.getInputFormatForFileName(this.getTemplateName());
    this.version = version;
    this.internalKey = internalKey;
  }

  public TemplateDocument(final String templateName, final Locale locale, final String version,
      final String internalKey) {
    this.templateName = templateName;
    this.locale = locale;
    this.format = InputFormat.getInputFormatForFileName(this.getTemplateName());
    this.version = version;
    this.internalKey = internalKey;
  }

  public String getTemplateName() {
    return this.templateName;
  }

  public Locale getLocale() {
    return locale;
  }

  public InputFormat getFormat() {

    return format;
  }

  public String getVersion() {

    return version;
  }

  public String getInternalKey() {

    return internalKey;
  }

  @Transient
  public byte[] getBinary() {
    return this.binary;
  }

  public void setBinary(byte[] binary) {
    this.binary = binary;
  }


  @Override
  public String toString() {
    return "TemplateDocument{" + "templateName=" + templateName + ", format=" + format + ", locale=" + locale + ", version=" + version + ", internalKey=" + internalKey + "}";
  }
}
