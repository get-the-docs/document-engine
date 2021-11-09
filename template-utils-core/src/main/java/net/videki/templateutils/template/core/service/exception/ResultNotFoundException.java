package net.videki.templateutils.template.core.service.exception;

/**
 * Result store indicated exception for result document or transaction not found.
 * @author Levente Ban
 */
public class ResultNotFoundException extends TemplateServiceException {

    /**
     * Initializer with given error code and message. 
     * @param errorCode the error code. 
     * @param message the error message.
     */
    public ResultNotFoundException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
