package net.videki.templateutils.template.core.processor.input.noop;

import net.videki.templateutils.template.core.processor.AbstractTemplateProcessor;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.service.InputFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoopTemplateProcessor extends AbstractTemplateProcessor implements InputTemplateProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoopTemplateProcessor.class);

    @Override
    public InputFormat getInputFormat() {
        return null;
    }

    @Override
    public <T> OutputStream fill(String templateFileName, T dto) {
    
        try (final InputStream is = getTemplate(templateFileName);
            final ByteArrayOutputStream targetStream = new ByteArrayOutputStream()) {
        
            is.transferTo(targetStream);

            return targetStream;
        } catch(final IOException e) {
            LOGGER.error("Error creating result stream", e);
            
            return null;
        }
    }
}
