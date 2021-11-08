package net.videki.templateutils.template.core.service;

/**
 * Top level container for the service. This is the entry point through the
 * getInstance() method for the outside world.
 * 
 * @author Levente Ban
 */
public class TemplateServiceRegistry {
    /**
     * Lock object for config changes.
     */
    private final static Object lockObject = new Object();

    /**
     * The actual service implementation.
     */
    private static TemplateService INSTANCE = new TemplateServiceImpl();

    /**
     * Sets the template service implementation to the given.
     * 
     * @param tsImpl the desired implementation.
     */
    void setTemplateService(final TemplateService tsImpl) {
        synchronized (lockObject) {
            INSTANCE = tsImpl;
        }
    }

    /**
     * Global entry point for processing templates.
     * 
     * @return the actual template service.
     */
    public static TemplateService getInstance() {
        TemplateService result = INSTANCE;
        if (result == null) {
            synchronized (lockObject) {
                result = INSTANCE = new TemplateServiceImpl();
            }
        }
        return result;
    }
}
