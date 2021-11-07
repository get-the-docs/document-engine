package net.videki.templateutils.template.core.configuration;

/**
 * Configurable font styles.
 *
 * @see FontConfig
 *
 * @author Levente Ban
 */
public enum FontStyle {

    /**
     * Font style: bold.
     */
    BOLD("bold"),

    /**
     * Font style: bold italic.
     */
    BOLD_ITALIC("boldItalic"),

    /**
     * Font style: italic.
     */
    ITALIC("italic"),

    /**
     * Font style: normal.
     */
    NORMAL("normal");

    private String value;

    /**
     * Font style with the given style.
     * @param value the font style.
     */
    FontStyle(String value) {
        this.value = value;
    }

    /**
     * Returns the font style. 
     * @return the font style.
     */
    public String getValue() {
        return value;
    }

}
