package net.videki.templateutils.template.core.documentstructure.descriptors;

import java.util.UUID;

public class TemplateElementId {
    public static final String TEMPLATE_KIND_GLOBAL = "GLOBAL";
    private String id = UUID.randomUUID().toString();

    public void setGlobal() {
        this.id = TEMPLATE_KIND_GLOBAL;
    }
    public String getId() {
        return id;
    }
}
