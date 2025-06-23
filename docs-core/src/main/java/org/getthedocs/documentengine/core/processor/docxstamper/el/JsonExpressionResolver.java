package org.getthedocs.documentengine.core.processor.docxstamper.el;

/*-
 * #%L
 * docs-core
 * %%
 * Copyright (C) 2025 Levente Ban
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

import io.reflectoring.docxstamper.api.EvaluationContextConfigurer;
import io.reflectoring.docxstamper.el.ExpressionResolver;
import io.reflectoring.docxstamper.el.ExpressionUtil;

/**
 * JsonExpressionResolver extension to provide the JsonPath expression resolver for the docx-stamper library.
 * Copy of the JsonExpressionResolver
 *
 * @author Levente Ban
 */
public class JsonExpressionResolver extends ExpressionResolver {

    /**
     * The docx-stamper library's ExpressionUtil.
     * @see io.reflectoring.docxstamper.el.ExpressionUtil
     */
    private static final ExpressionUtil expressionUtil = new ExpressionUtil();

    /**
     * Default constructor.
     */
    public JsonExpressionResolver() {
        super();
    }

    /**
     * Constructor with an EvaluationContextConfigurer.
     *
     * @param evaluationContextConfigurer the EvaluationContextConfigurer to be used.
     */
    public JsonExpressionResolver(EvaluationContextConfigurer evaluationContextConfigurer) {
        super(evaluationContextConfigurer);
    }

    /**
     * Runs the given expression against the given context object and returns the result of the evaluated expression.
     * This method rewrite is the extension hook for the JSONPath expression resolver.
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

    /**
     * Strips the JSONPath expression start and end tokens from the given expression string.
     * @param expression the expression string to be stripped.
     * @return the stripped expression string.
     */
    private String stripJsonpathExpression(final String expression) {
        if (expression == null) {
            throw new IllegalArgumentException("Cannot strip NULL expression!");
        }

        if (expression.contains("jsonpath") || expression.contains("jp")) {
            return expression
                    .replaceAll("\\$\\.", "")
                    .replaceAll("(jsonpath[\\s]*\\(|jp[\\s]*\\()([^\\)]*)(\\))", "jsonpath(###$2###)")
                    .replace("'", "''")
                    .replaceAll("jsonpath\\(###", "jsonpath\\('")
                    .replaceAll("###\\)", "'\\)");
        } else {
            return expression;
        }
    }

}
