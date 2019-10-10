package net.videki.templateutils.template.core.service.exception;

public class TemplateServiceConfigurationException extends TemplateServiceException {

    public static final String MSG_INVALID_PARAMETERS = "Invalid parameters";

    public TemplateServiceConfigurationException(String errorCode, String msg) {
        super(errorCode, msg);
    }
}
