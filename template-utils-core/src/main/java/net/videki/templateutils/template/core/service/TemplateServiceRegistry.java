package net.videki.templateutils.template.core.service;

public class TemplateServiceRegistry {
    private static final TemplateService INSTANCE = new TemplateServiceImpl();

    public static TemplateService getInstance() {
        return INSTANCE;
    }
}
