package net.videki.templateutils.template.core.processor.input;

import net.videki.templateutils.template.core.service.exception.TemplateProcessException;

/**
 * Placeholder evaluation exception for indicating template parameter errors.
 * @author Levente Ban
 */
public class PlaceholderEvalException extends TemplateProcessException {

    /**
     * Constructor to initialize with a given error code and message.
     * @param errorCode the error code to identify the origin.
     * @param message error message.
     */
    public PlaceholderEvalException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructor to initialize with a given error code, message and root cause.
     * @param errorCode the error code to identify the origin.
     * @param message error message.
     * @param e the root cause.
     */
    public PlaceholderEvalException(String errorCode, String message, Throwable e) {
        super(errorCode, message);

        this.setStackTrace(e.getStackTrace());
    }
}
