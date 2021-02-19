package net.videki.templateutils.template.core.context;

import net.videki.templateutils.template.core.dto.ITemplate;
import net.videki.templateutils.template.core.dto.JsonModel;

import java.util.List;

public abstract class ContextObject implements ITemplate, JsonModel {
    public abstract Object getValue(String expression);

    public abstract List<Object> getItems(String expression);
}
