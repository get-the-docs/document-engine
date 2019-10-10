package net.videki.templateutils.template.core.configuration;

public class FontConfig {

	private String fontFamily;
	private FontStyle style;
	private String fileName;
	private String basedir;

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public FontStyle getStyle() {
		return style;
	}

	public void setStyle(FontStyle style) {
		this.style = style;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBasedir() {
		return basedir;
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

}
