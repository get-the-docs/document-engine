package net.videki.templateutils.template.core.service.exception;

public class TemplateServiceException extends Exception{
    private String code;

    public TemplateServiceException(String errorCode, String message) {
        super(message);
        this.code = errorCode;
    }

    public String getCode() {
        return code;
    }

}
