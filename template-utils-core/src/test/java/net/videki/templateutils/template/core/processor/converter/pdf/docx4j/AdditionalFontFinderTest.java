package net.videki.templateutils.template.core.processor.converter.pdf.docx4j;

import net.videki.templateutils.template.core.configuration.FontConfig;
import net.videki.templateutils.template.core.configuration.FontStyle;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class AdditionalFontFinderTest {

    @Test
    public void nullConfig() {

        try {
            AdditionalFontFinder.discoverFonts(null);

            fail();
        } catch (final TemplateServiceConfigurationException e) {
            Assert.assertEquals("037e44ac-3dcf-4344-8989-31cf3fcfc624", e.getCode());
        } catch (final Exception e) {
            fail();
        }
    }

    @Test
    public void configuredFontWithEmptyBaseDir() {

        try {
            final List<FontConfig> myFonts = new ArrayList<>();
            final FontConfig c = new FontConfig();
            c.setBasedir("");
            c.setFileName("invalid.ttf");
            c.setStyle(FontStyle.BOLD);
            myFonts.add(c);

            AdditionalFontFinder.discoverFonts(myFonts);

        } catch (final TemplateServiceConfigurationException e) {
            Assert.assertEquals("656dca28-bc61-4059-991f-7bb65ec916e6", e.getCode());
        } catch (final Exception e) {
            fail();
        }
    }

    @Test
    public void configuredFontWithNullBaseDir() {

        try {
            final List<FontConfig> myFonts = new ArrayList<>();
            final FontConfig c = new FontConfig();
            c.setBasedir(null);
            c.setFileName("invalid.ttf");
            c.setStyle(FontStyle.BOLD);
            myFonts.add(c);

            AdditionalFontFinder.discoverFonts(myFonts);

        } catch (final TemplateServiceConfigurationException e) {
            Assert.assertEquals("656dca28-bc61-4059-991f-7bb65ec916e6", e.getCode());
        } catch (final Exception e) {
            fail();
        }
    }


    @Test
    public void configuredFontWithInvalidBaseDir() {

        try {
            final List<FontConfig> myFonts = new ArrayList<>();
            final FontConfig c = new FontConfig();
            c.setBasedir(null);
            c.setFileName("invalid.ttf");
            c.setStyle(FontStyle.BOLD);
            myFonts.add(c);

            AdditionalFontFinder.discoverFonts(myFonts);

        } catch (final TemplateServiceConfigurationException e) {
            Assert.assertEquals("656dca28-bc61-4059-991f-7bb65ec916e6", e.getCode());
        } catch (final Exception e) {
            fail();
        }
    }

    @Test
    public void configuredWithEmptyFontConfig() {

        try {
            AdditionalFontFinder.discoverFonts(new ArrayList<>());

            Assert.assertTrue(true);
        } catch (final Exception e) {
            fail();
        }

    }

}
