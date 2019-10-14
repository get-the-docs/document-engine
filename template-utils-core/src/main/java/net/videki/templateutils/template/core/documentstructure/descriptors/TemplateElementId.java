package net.videki.templateutils.template.core.documentstructure.descriptors;

import java.util.Objects;
import java.util.UUID;

public class TemplateElementId {
    public static final String TEMPLATE_KIND_GLOBAL = "GLOBAL";
    private String id = UUID.randomUUID().toString();

    public TemplateElementId() {
    }

    public TemplateElementId(final String id) {
        this.id = id;
    }

    public void setGlobal() {
        this.id = TEMPLATE_KIND_GLOBAL;
    }
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateElementId)) return false;
        TemplateElementId that = (TemplateElementId) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TemplateElementId{" +
                "id='" + id + '\'' +
                '}';
    }
}
