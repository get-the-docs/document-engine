package net.videki.templateutils.template.core.service.exception;

/**
 * Template processing error indicator for indicating non-eval based runtime
 * errors.
 * 
 * @author Levente Ban
 */
public class TemplateProcessException extends TemplateServiceRuntimeException {

    /**
     * the error code
     */
    private String code;

    /**
     * Convstructor with given error code and message.
     * 
     * @param errorCode the error code.
     * @param message   the error message.
     */
    public TemplateProcessException(final String errorCode, final String message) {
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
