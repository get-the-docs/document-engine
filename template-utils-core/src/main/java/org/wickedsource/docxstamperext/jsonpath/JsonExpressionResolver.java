/*
 * Copyright (c) 2021-2021. Levente Ban
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wickedsource.docxstamperext.jsonpath;

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

import org.wickedsource.docxstamper.api.EvaluationContextConfigurer;
import org.wickedsource.docxstamper.el.ExpressionResolver;
import org.wickedsource.docxstamper.el.ExpressionUtil;
import org.wickedsource.docxstamper.el.NoOpEvaluationContextConfigurer;

import net.videki.templateutils.template.core.context.dto.JsonTemplateContext;

/**
 * Expression resolver extension for json model contexts.
 *
 * @author Levente Ban
 */
public class JsonExpressionResolver extends ExpressionResolver {

    private static final ExpressionUtil expressionUtil = new ExpressionUtil();

    private final EvaluationContextConfigurer evaluationContextConfigurer;

    public JsonExpressionResolver() {
        this.evaluationContextConfigurer = new NoOpEvaluationContextConfigurer();
    }

    public JsonExpressionResolver(EvaluationContextConfigurer evaluationContextConfigurer) {
        this.evaluationContextConfigurer = evaluationContextConfigurer;
    }

    /**
     * Runs the given expression against the given context object and returns the result of the evaluated expression.
     *
     * @param expressionString the expression to evaluate.
     * @param contextRoot      the context object against which the expression is evaluated.
     * @return the result of the evaluated expression.
     */
    @Override
    public Object resolveExpression(String expressionString, Object contextRoot) {

        if ((expressionString.startsWith("${") || expressionString.startsWith("#{")) && expressionString.endsWith("}")) {
            expressionString = expressionUtil.stripExpression(expressionString);
        }

        expressionString = stripJsonpathExpression(expressionString);
        return super.resolveExpression(expressionString, contextRoot);

    }

    private String stripJsonpathExpression(final String expression) {
        if (expression == null) {
            throw new IllegalArgumentException("Cannot strip NULL expression!");
        }

        if (expression.contains("jsonpath") || expression.contains("jp")) {
            return expression
                    .replaceAll("\\$\\.", "")
                    .replaceAll("(jsonpath[\\s]*\\(|jp[\\s]*\\()([^\\)]*)(\\))", "jsonpath(###$2###)")
                    .replaceAll("'", "''")
                    .replaceAll("jsonpath\\(###", "jsonpath\\('")
                    .replaceAll("###\\)", "'\\)");
        } else {
            return expression;
        }
    }

}
