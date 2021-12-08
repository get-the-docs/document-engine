package net.videki.templateutils.template.core.documentstructure.v1;

/*-
 * #%L
 * template-utils-core
 * %%
 * Copyright (C) 2021 Levente Ban
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
