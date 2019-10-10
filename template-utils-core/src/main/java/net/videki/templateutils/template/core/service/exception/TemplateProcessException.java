package net.videki.templateutils.template.core.service.exception;

public class TemplateProcessException extends TemplateServiceRuntimeException {
    private String code;
    public TemplateProcessException(String errorCode, String message) {
        super(message);
        this.code = errorCode;
    }

    public String getCode() {
        return code;
    }
}
