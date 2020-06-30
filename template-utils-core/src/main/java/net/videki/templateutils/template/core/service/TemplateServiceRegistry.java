package net.videki.templateutils.template.core.service;

public class TemplateServiceRegistry {
    private final static Object lockObject = new Object();
    private static TemplateService INSTANCE = new TemplateServiceImpl();

    void setTemplateService(final TemplateService tsImpl) {
        synchronized (lockObject) {
            INSTANCE = tsImpl;
        }
    }

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
