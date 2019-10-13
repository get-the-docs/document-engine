package net.videki.templateutils.template.core.service;

public class TemplateServiceRegistry {
    private static TemplateService INSTANCE = new TemplateServiceImpl();

    void setTemplateService(final TemplateService tsImpl) {
        synchronized (this) {
            INSTANCE = tsImpl;
        }
    }

    public static TemplateService getInstance() {
        return INSTANCE;
    }
}
