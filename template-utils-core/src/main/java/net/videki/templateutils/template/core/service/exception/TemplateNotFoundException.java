package net.videki.templateutils.template.core.service.exception;

public class TemplateNotFoundException extends TemplateProcessException {
    public TemplateNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }
}
