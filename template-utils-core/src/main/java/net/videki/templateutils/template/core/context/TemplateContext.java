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
    public static final String CONTEXT_ROOT_KEY = "ctx";
    public static final String CONTEXT_ROOT_KEY_MODEL = "model";

    private Map<String, Object> ctx = new HashMap<>();
    private TemplateContext getRoot() {
        return this;
    }
    public Object getModel() {
        return this.ctx.get(CONTEXT_ROOT_KEY_MODEL);
    }

    public Map<String, Object> getCtx() {
        return this.ctx;
    }

    public <T> TemplateContext addValueObject(final T value) {
        this.ctx.put(CONTEXT_ROOT_KEY_MODEL, value);

        return this;
    }

    public <T> TemplateContext addValueObject(final String contextKey, final T value) {
        this.ctx.put(contextKey, value);

        return this;
    }

    @Override
    public String toString() {
        return "TemplateContext{" +
                "ctx=" + ctx +
                "}";
    }
}
