package net.videki.templateutils.template.core.service;

public enum OutputFormat {

    //  RTF,
    UNCHANGED,
    DOCX,
    PDF;

    public boolean isSameFormat(final Object a) {
        if (a != null) {
            if (a instanceof InputFormat) {
                return ((InputFormat) a).name().equals(this.name());
            }
        }
        return false;
    }
}