package net.videki.templateutils.template.core.service;

/**
 * Supported output formats.
 * 
 * @author Levente Ban
 */
public enum OutputFormat {

    // RTF,

    /**
     * Wildcard format to mark that the document format should not be changed during
     * generation.
     */
    UNCHANGED,

    /**
     * Docx format.
     */
    DOCX,

    /**
     * Pdf.
     */
    PDF;

    /**
     * Checks a format to check that they have the same format.
     * 
     * @param a the format to check.
     * @return true if the parameter has the same format.
     */
    public boolean isSameFormat(final Object a) {
        if (a != null) {
            if (UNCHANGED.name().equals(this.name())) {
                return true;
            } else if ((a instanceof InputFormat)) {
                return ((InputFormat) a).name().equals(this.name());
            }
        }
        return false;
    }
}