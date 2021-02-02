package net.videki.templateutils.template.core.processor.converter.pdf.docx4j;

import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.processor.converter.ConversionException;
import net.videki.templateutils.template.core.processor.converter.Converter;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.OutputFormat;
import net.videki.templateutils.template.core.util.FileSystemHelper;
import org.docx4j.Docx4J;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class DocxToPdfConverter implements Converter {

  private static final Logger LOGGER = LoggerFactory.getLogger(DocxToPdfConverter.class);

  static {

    try {
      PhysicalFonts.discoverPhysicalFonts();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

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
      throw new ConversionException("7d90a4a1-14df-4d1a-87d8-fd9b146357e8", "Null input caught.");
    }

    final TemplateServiceConfiguration fontConfiguration = TemplateServiceConfiguration.getInstance();

    result = FileSystemHelper.getOutputStream();

    try {

      final WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(source);
      final List<SectionWrapper> sections = wordMLPackage.getDocumentModel().getSections();
      for (int i = 0; i < sections.size(); i++) {
        wordMLPackage.getDocumentModel().getSections().get(i).getPageDimensions();
      }

      final Mapper fontMapper = new IdentityPlusMapper();

      AdditionalFontFinder.discoverFonts(fontConfiguration.getFontConfig());

      wordMLPackage.setFontMapper(fontMapper);

      Docx4J.toPDF(wordMLPackage, result);

      source.close();
    } catch (final Throwable e) {
      final String msg = "Error on pdf creation.";
      LOGGER.error(msg, e);

      throw new ConversionException("c0a3ab2e-297d-4634-85cc-d171fd0772f1", msg, e);
    }

    return result;
  }

}
