package net.videki.templateutils.template.core.documentstructure.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.util.UUID;

/**
 * Template element id holder.
 * Can be identified by a unique id, or can be global - see TEMPLATE_KING_GLOBAL.
 * 
 * @author Levente Ban
 */
public class TemplateElementId {
    /**
     * Global template element id. 
     */
    public static final String TEMPLATE_KIND_GLOBAL = "GLOBAL";

    /**
     * Template element id.
     */
    private String id = UUID.randomUUID().toString();

    /**
     * Deafult constructor.
     */
    public TemplateElementId() {
    }

    /**
     * Initializes the container with a given template element id.
     * @param id the template element id.
     */
    public TemplateElementId(final String id) {
        this.id = id;
    }

    /**
     * Factory method to return a template element id holder with the global template element id.
     * @return a container with the global template element id.
     */
    public static TemplateElementId getGlobalTemplateElementId() {
        return new TemplateElementId(TEMPLATE_KIND_GLOBAL);
    }

    /**
     * Sets the template element id to the global id.
     */
    @JsonIgnore
    public void setGlobal() {
        this.id = TEMPLATE_KIND_GLOBAL;
    }

    /**
     * Returns the template element id.
     * @return the template element id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the template element id.
     * @param id the template element id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Checks whether two template element ids are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateElementId)) return false;
        TemplateElementId that = (TemplateElementId) o;
        return id.equals(that.id);
    }

    /**
     * Hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**Convenience method for logging. */
    @Override
    public String toString() {
        return "TemplateElementId{" +
                "id='" + id + '\'' +
                "}";
    }
}
