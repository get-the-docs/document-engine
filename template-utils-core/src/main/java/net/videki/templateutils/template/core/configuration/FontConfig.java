package net.videki.templateutils.template.core.configuration;

/**
 * Font mapping for enabling additional fonts during PDF generation.
 * (To extend the predefined types (courier, helvetica, times roman) @see com.lowagie.text.Font)
 *
 * @see TemplateServiceConfiguration service configuration
 *
 * @author Levente Ban
 */
public class FontConfig {

    private String fontFamily;
    private FontStyle style;
    private String fileName;
    private String basedir;

    /** 
     * Returns the font family of the custom font.
     * @return the font family.
     * */
    public String getFontFamily() {
        return fontFamily;
    }

    /**
     * Sets the font family for a font.
     * @param fontFamily the font family.
     */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    /**
     * Returns the font style.
     * @return the font style.
     */
    public FontStyle getStyle() {
        return style;
    }

    /**
     * Sets the font style.
     * @param style the font style.
     */
    public void setStyle(FontStyle style) {
        this.style = style;
    }

    /**
     * Returns the font file's name.
     * @return the font file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the font file name.
     * @param fileName the font file name.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the basedir where the custom fonts are installed. 
     * @return the basedir.
     */
    public String getBasedir() {
        return basedir;
    }

    /**
     * Sets the basedir where the custom fonts are installed.
     * @param basedir the basedir of fonts.
     */
    public void setBasedir(String basedir) {
        this.basedir = basedir;
    }

}
