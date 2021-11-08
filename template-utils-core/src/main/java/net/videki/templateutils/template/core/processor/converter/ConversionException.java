package net.videki.templateutils.template.core.processor.converter;

import net.videki.templateutils.template.core.service.exception.TemplateProcessException;

/**
 * Document format conversion exception.
 * 
 * @author Levente Ban
 */
public class ConversionException extends TemplateProcessException {

    /**
     * Constructor with a given error code and message.
     * @param errorCode the error code - unique uuids for each throw to identify the origin in builds without debug info. 
     * @param message the error message.
     */
    public ConversionException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructor with a given error code, message and a root cause.
     * @param errorCode the error code - unique uuids for each throw to identify the origin in builds without debug info. 
     * @param message the error message.
     * @param e the root cause.
     */
    public ConversionException(String errorCode, String message, Throwable e) {
        super(errorCode, message);
        this.setStackTrace(e.getStackTrace());
    }
}
