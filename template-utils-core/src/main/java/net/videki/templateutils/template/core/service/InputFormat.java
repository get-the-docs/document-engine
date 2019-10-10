package net.videki.templateutils.template.core.service;

public enum InputFormat {
  DOCX("DOCX"), RTF("RTF"), XLSX("XLSX");

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

}
