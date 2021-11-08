package net.videki.templateutils.template.core.service.exception;

/**
 * Base runtime exception to indicate unexpected errors during template
 * processing.
 * 
 * @author Levente Ban
 */
public class TemplateServiceRuntimeException extends RuntimeException {
    /**
     * Constructor with given error message.
     * 
     * @param message the error message.
     */
    public TemplateServiceRuntimeException(String message) {
        super(message);
    }
}
