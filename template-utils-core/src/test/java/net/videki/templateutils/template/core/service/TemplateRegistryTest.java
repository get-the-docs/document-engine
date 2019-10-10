package net.videki.templateutils.template.core.service;

import org.junit.Assert;
import org.junit.Test;

public class TemplateRegistryTest {

    @Test
    public void TemplateServiceInstanceTest() {
        final TemplateService ts = TemplateServiceRegistry.getInstance();

        Assert.assertNotNull(ts);
    }

    @Test
    public void TemplateServiceInstanceIsSingletonTest() {
        final TemplateService ts1 = TemplateServiceRegistry.getInstance();
        final TemplateService ts2 = TemplateServiceRegistry.getInstance();

        Assert.assertEquals(ts1, ts2);
    }
}
