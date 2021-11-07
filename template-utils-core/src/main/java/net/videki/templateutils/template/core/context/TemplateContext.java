package net.videki.templateutils.template.core.context;

import net.videki.templateutils.template.core.dto.ITemplate;
import net.videki.templateutils.template.core.dto.JsonModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Template contexts are value object collections for a template element (template file),
 * grouped into contexts which can be used in the templates to prefix the data transfer objects.
 *
 * <b>If the template will only have one single context, the global context can be used,
 * which can be referred as 'model' in the template.</b>
 * <b>In case of multiple contexts (e.g. officer data, contract data and so on), the data objects can be grouped
 * into groups: contexts. In this case you can refer them as officer.name, contract.id, etc.</b>
 * See the attached examples for further explanation.
 *
 * @author Levente Ban
 */
public class TemplateContext implements ITemplate, JsonModel {
   
    /**
     * Context root object key.
     */
    public static final String CONTEXT_ROOT_KEY = "ctx";

    /**
     * Context root object key for single model contexts.
     */
    public static final String CONTEXT_ROOT_KEY_MODEL = "model";

    /**
     * The model context. 
     * It is a key-value store to hold objects.
     */
    private Map<String, Object> ctx = new HashMap<>();

    /**
     * Returns the template context for the root context.
     * @return the template context for the root context.
     */
    private TemplateContext getRoot() {
        return this;
    }

    /**
     * Returns the model object for the root context.
     * Use this method in the placeholder for single model templates.
     * @return the root context's model object.
     */
    public Object getModel() {
        return this.ctx.get(CONTEXT_ROOT_KEY_MODEL);
    }

    /**
     * Returns the context objects as a map for multi-object models provided for template processors to fill-in values.
     * @return the list of model objects.
     */
    public Map<String, Object> getCtx() {
        return this.ctx;
    }

    /**
     * Adds an object to the model list to be used as the root model. 
     * The method is aimed for reflection-based (see lib-based direct) usage.  
     * @param <T> the model context holding the provided data.
     * @param value the provided data.
     * @return the template context object created from the data object.
     */
    public <T> TemplateContext addValueObject(final T value) {
        this.ctx.put(CONTEXT_ROOT_KEY_MODEL, value);

        return this;
    }

    /**
     * Adds an object to the model list with the provided context key. 
     * The method is aimed for reflection-based (see lib-based direct) usage.  
     * @param <T> the model context holding the provided data.
     * @param contextKey the context key to the data for.
     * @param value the provided data.
     * @return the template context object created from the data object.
     */
    public <T> TemplateContext addValueObject(final String contextKey, final T value) {
        this.ctx.put(contextKey, value);

        return this;
    }

    /**
     * Trace logging convencience method.
     */
    @Override
    public String toString() {
        return "TemplateContext{" +
                "ctx=" + ctx +
                "}";
    }
}
