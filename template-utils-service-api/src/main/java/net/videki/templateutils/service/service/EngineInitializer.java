package net.videki.templateutils.service.service;

import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class EngineInitializer implements InitializingBean {
    @Override
    public void afterPropertiesSet() {
        TemplateServiceConfiguration.getInstance();
    }
}
