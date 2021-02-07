package net.videki.templateutils.template.core.service;

import org.junit.Assert;
import org.junit.Test;

public class TemplateRegistryTest {

    @Test
    public void templateServiceInstanceTest() {
        final TemplateService ts = TemplateServiceRegistry.getInstance();

        Assert.assertNotNull(ts);
    }

    @Test
    public void templateServiceInstanceIsSingletonTest() {
        final TemplateService ts1 = TemplateServiceRegistry.getInstance();
        final TemplateService ts2 = TemplateServiceRegistry.getInstance();

        Assert.assertEquals(ts1, ts2);
    }

    @Test
    public void templateServiceSetInstanceTest() {
        final TemplateService ts1 = TemplateServiceRegistry.getInstance();
        final var registry = new TemplateServiceRegistry();
        registry.setTemplateService(null);
        final TemplateService ts2 = TemplateServiceRegistry.getInstance();

        Assert.assertNotEquals(ts1, ts2);
    }

    @Test
    public void templateServiceCheckInstanceReinitTest() {
        final var registry = new TemplateServiceRegistry();
        registry.setTemplateService(null);
        final TemplateService ts2 = TemplateServiceRegistry.getInstance();

        Assert.assertNotNull(ts2);
    }
}
