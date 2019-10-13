package net.videki.templateutils.template.core.processor.converter;

import net.videki.templateutils.template.core.service.exception.TemplateProcessException;

public class ConversionException extends TemplateProcessException {

    public ConversionException(String errorCode, String message) {
        super(errorCode, message);
    }

    public ConversionException(String errorCode, String message, Throwable e) {
        super(errorCode, message);
        this.setStackTrace(e.getStackTrace());
    }
}
