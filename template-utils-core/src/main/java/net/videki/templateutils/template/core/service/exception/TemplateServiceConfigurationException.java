package net.videki.templateutils.template.core.service.exception;

/**
 * Exception class to indicate configuration errors related to the template
 * service or its components.
 * 
 * @author Levente Ban
 */
public class TemplateServiceConfigurationException extends TemplateServiceException {

    /**
     * Base error message.
     */
    public static final String MSG_INVALID_PARAMETERS = "Invalid parameters";

    /**
     * Constructor with given error code and message.
     * 
     * @param errorCode the error code (by default constant uuids to be able to
     *                  identify the raise location without debug info too).
     * @param msg       the error message.
     */
    public TemplateServiceConfigurationException(final String errorCode, final String msg) {
        super(errorCode, msg);
    }
}
