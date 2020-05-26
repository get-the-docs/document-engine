package net.videki.templateutils.template.core.context;

import net.videki.templateutils.template.core.dto.ITemplate;
import net.videki.templateutils.template.core.dto.JsonModel;

import java.util.HashMap;
import java.util.Map;

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
}
