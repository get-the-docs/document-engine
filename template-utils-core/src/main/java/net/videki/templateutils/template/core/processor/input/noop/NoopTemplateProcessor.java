package net.videki.templateutils.template.core.processor.input.noop;

import net.videki.templateutils.template.core.processor.AbstractTemplateProcessor;
import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.service.InputFormat;

import java.io.InputStream;
import java.io.OutputStream;

public class NoopTemplateProcessor extends AbstractTemplateProcessor implements InputTemplateProcessor {
    @Override
    public InputFormat getInputFormat() {
        return null;
    }

    @Override
    public <T> OutputStream fill(String templateFileName, T dto) {
        final InputStream is = getTemplate(templateFileName);
        return null;
    }
}
