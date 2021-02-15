package net.videki.templateutils.template.core.documentstructure.descriptors;

import org.junit.Assert;
import org.junit.Test;

public class TemplateElementIdTest {

    @Test
    public void noArgConstructorShouldReturnRandomUuid() {
        final var te = new TemplateElementId();

        Assert.assertNotNull(te.getId());
    }

    @Test
    public void setGlobalShouldReturnKindGlobal() {
        final var te = new TemplateElementId();

        te.setGlobal();
        Assert.assertEquals(TemplateElementId.TEMPLATE_KIND_GLOBAL, te.getId());
    }

    @Test
    public void randomIdOverrideShouldReturnNewId() {
        final var te = new TemplateElementId();

        te.setId("WATCH_THIS");
        Assert.assertEquals("WATCH_THIS", te.getId());
    }

}
