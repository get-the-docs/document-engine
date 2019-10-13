package net.videki.templateutils.template.core.service;

import net.videki.templateutils.template.core.configuration.util.FileSystemHelper;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;

public enum InputFormat {
  DOCX("DOCX"), XLSX("XLSX");

  private final String strValue;

  InputFormat(final String pValue) {
    this.strValue = pValue;
  }

  public String getStrValue() {
    return strValue;
  }

  public boolean isSameFormat(final Object a) {
    if (a != null) {
      if (a instanceof OutputFormat) {
        return this.name().equals(((OutputFormat) a).name());
      }
    }
    return false;
  }

  public static InputFormat getInputFormatForFileName(final String templateName) {
    InputFormat format;
    try {
      format = InputFormat
              .valueOf(
                      templateName.toUpperCase().substring(
                              templateName.lastIndexOf(FileSystemHelper.FILENAME_COLON))
                              .replace(FileSystemHelper.FILENAME_COLON, ""));

      return format;
    } catch (IllegalArgumentException e) {
      final String msg = String.format("Unhandled template file format " +
              "(input processor for the filename extension not found). Filename: %s", templateName);
      throw new TemplateProcessException("c14d63df-8db2-45a2-bf21-e62fe60a23a0", msg);
    }
  }
}
