package net.videki.templateutils.template.core.processor.converter.pdf;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import net.videki.templateutils.template.core.configuration.FontConfig;
import net.videki.templateutils.template.core.configuration.FontStyle;
import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.util.FileSystemHelper;
import net.videki.templateutils.template.core.processor.converter.ConversionException;
import net.videki.templateutils.template.core.processor.converter.Converter;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.OutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

public class DocxToPdfConverter implements Converter {

  private static final Logger LOGGER = LoggerFactory.getLogger(DocxToPdfConverter.class);

  @Override
  public InputFormat getInputFormat() {
    return InputFormat.DOCX;
  }

  @Override
  public OutputFormat getOutputFormat() {
    return OutputFormat.PDF;
  }

  @Override
  public OutputStream convert(final InputStream source) {
    OutputStream result;

    if (source == null) {
      return null;
    }

    final TemplateServiceConfiguration fontConfiguration = TemplateServiceConfiguration.getInstance();

    final DocumentKind from = DocumentKind.valueOf("DOCX");
    final ConverterTypeTo to = ConverterTypeTo.valueOf("PDF");
    final Options options = Options.getFrom(from).to(to);

    final PdfOptions pdfOptions = PdfOptions.create();
    pdfOptions.fontProvider((familyName, encoding, size, style, color) -> {
      try {
        LOGGER.trace(String.format("Getting font required by the template: %s", familyName));

        FontStyle fontStyle;

        switch (style) {
          case Font.BOLD:
            fontStyle = FontStyle.BOLD;
            break;
          case Font.ITALIC:
            fontStyle = FontStyle.ITALIC;
            break;
          case Font.BOLDITALIC:
            fontStyle = FontStyle.BOLD_ITALIC;
            break;
          default:
            fontStyle = FontStyle.NORMAL;
        }
        final FontConfig fc = fontConfiguration.getFontConfig(familyName, fontStyle);

        if (fc != null) {
          final String fontPath = fc.getBasedir() + FileSystemHelper.FILENAME_DIR_SEPARATOR + fc.getFileName();
          final BaseFont baseFont = BaseFont.createFont(fontPath, encoding, BaseFont.EMBEDDED);

          Font f = new Font(baseFont, size, style, color);
          LOGGER.trace(String.format("Font loaded: %s-%s, file: %s", familyName, fontStyle, fontPath));
          return f;
        }

        LOGGER.trace(String.format("No bundled font found: %s - using fallback font factory.", familyName));

      } catch (Exception e) {
        throw new RuntimeException(e);
      }

      // Fallback
      return FontFactory.getFont(familyName, encoding, size, style, color);
    });
    options.subOptions(pdfOptions);
    IConverter converter = ConverterRegistry.getRegistry().getConverter(options);

    try {
      try {
        result = FileSystemHelper.getOutputStream();
        converter.convert(source, result, options);

      } finally {
        source.close();
      }
    } catch (Exception e) {
      final String msg = "Error on pdf creation.";
      LOGGER.error(msg, e);

      throw new ConversionException("c0a3ab2e-297d-4634-85cc-d171fd0772f1", msg, e);
    }

    return result;
  }

}
