package net.videki.templateutils.template.core.service.exception;

/**
 * Base exception class to indicate service errors.
 * 
 * @author Levente Ban
 */
public class TemplateServiceException extends Exception {
    /**
     * the error code.
     */
    private String code;

    /**
     * Constructor with given error code and message.
     * 
     * @param errorCode the error code.
     * @param message   the error message.
     */
    public TemplateServiceException(String errorCode, String message) {
        super(message);
        this.code = errorCode;
    }

    /**
     * Returns the error code.
     * 
     * @return the error code.
     */
    public String getCode() {
        return code;
    }

}
