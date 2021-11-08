package net.videki.templateutils.template.core.service.exception;

/**
 * Template repository indicated exception to template not found.
 * @author Levente Ban
 */
public class TemplateNotFoundException extends TemplateProcessException {

    /**
     * Initializer with given error code and message. 
     * @param errorCode the error code. 
     * @param message the error message.
     */
    public TemplateNotFoundException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
