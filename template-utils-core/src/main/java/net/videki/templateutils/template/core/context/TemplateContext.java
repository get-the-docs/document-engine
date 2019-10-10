package net.videki.templateutils.template.core.context;

import net.videki.templateutils.template.core.dto.ITemplate;
import net.videki.templateutils.template.core.dto.JsonModel;

import java.util.HashMap;
import java.util.Map;

public class TemplateContext implements ITemplate, JsonModel {
    private Map<String, Object> ctx = new HashMap<>();
    private TemplateContext getRoot() {
        return this;
    }

    public Map<String, Object> getCtx() {
        return this.ctx;
    }

}
