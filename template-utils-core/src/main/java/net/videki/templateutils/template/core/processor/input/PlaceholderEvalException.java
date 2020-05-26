package net.videki.templateutils.template.core.processor.input;

import net.videki.templateutils.template.core.service.exception.TemplateProcessException;

public class PlaceholderEvalException extends TemplateProcessException {

    public PlaceholderEvalException(String errorCode, String message) {
        super(errorCode, message);
    }

    public PlaceholderEvalException(String errorCode, String message, Throwable e) {
        super(errorCode, message);

        this.setStackTrace(e.getStackTrace());
    }
}
