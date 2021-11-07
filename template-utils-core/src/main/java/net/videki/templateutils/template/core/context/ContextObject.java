package net.videki.templateutils.template.core.context;

import net.videki.templateutils.template.core.dto.ITemplate;
import net.videki.templateutils.template.core.dto.JsonModel;

import java.util.List;

/**
 * Abstract value holder for expression based actual model objects.
 * It implements the ITemplate interface to provide the reflection based convinience methods and the json capabilities. 
 * Usage: for literal values the getValue() getter, for value lists the getItems().
 *  
 * @author Levente Ban
 */
public abstract class ContextObject implements ITemplate, JsonModel {

    /**
     * Returns a literal value described by the expression defined, if assigned to the object.
     * @param expression the expression to return a literal.
     * @return the expression result object.
     */
    public abstract Object getValue(String expression);

    /**
     * Returns the list of embedded objects, if persent.
     * @param expression the expression to return a value list.
     * @return the list of objects as a result of the expression evaluation provided by the actual parameter.
     */
    public abstract List<Object> getItems(String expression);
}
