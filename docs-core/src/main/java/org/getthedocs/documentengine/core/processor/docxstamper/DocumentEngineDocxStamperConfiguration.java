package org.getthedocs.documentengine.core.processor.docxstamper;

/*-
 * #%L
 * docs-core
 * %%
 * Copyright (C) 2023 - 2025 Levente Ban
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
import io.reflectoring.docxstamper.api.commentprocessor.ICommentProcessor;
import io.reflectoring.docxstamper.api.typeresolver.ITypeResolver;
import io.reflectoring.docxstamper.el.NoOpEvaluationContextConfigurer;
import io.reflectoring.docxstamper.replace.typeresolver.FallbackResolver;

import java.util.HashMap;
import java.util.Map;

public class DocumentEngineDocxStamperConfiguration {
    private String lineBreakPlaceholder;
    private EvaluationContextConfigurer evaluationContextConfigurer = new NoOpEvaluationContextConfigurer();
    private boolean failOnUnresolvedExpression = true;
    private boolean leaveEmptyOnExpressionError = false;
    private boolean replaceNullValues = false;
    private Map<Class<?>, ICommentProcessor> commentProcessors = new HashMap();
    private Map<Class<?>, ITypeResolver> typeResolvers = new HashMap();
    private ITypeResolver defaultTypeResolver = new FallbackResolver();
    private Map<Class<?>, Object> expressionFunctions = new HashMap();

    public DocumentEngineDocxStamperConfiguration setLineBreakPlaceholder(String lineBreakPlaceholder) {
        this.lineBreakPlaceholder = lineBreakPlaceholder;
        return this;
    }

    public DocumentEngineDocxStamperConfiguration setEvaluationContextConfigurer(EvaluationContextConfigurer evaluationContextConfigurer) {
        this.evaluationContextConfigurer = evaluationContextConfigurer;
        return this;
    }

    public DocumentEngineDocxStamperConfiguration setFailOnUnresolvedExpression(boolean failOnUnresolvedExpression) {
        this.failOnUnresolvedExpression = failOnUnresolvedExpression;
        return this;
    }

    public DocumentEngineDocxStamperConfiguration addCommentProcessor(Class<?> interfaceClass, ICommentProcessor commentProcessor) {
        this.commentProcessors.put(interfaceClass, commentProcessor);
        return this;
    }

    public <T> DocumentEngineDocxStamperConfiguration addTypeResolver(Class<T> resolvedType, ITypeResolver resolver) {
        this.typeResolvers.put(resolvedType, resolver);
        return this;
    }

    public DocumentEngineDocxStamperConfiguration exposeInterfaceToExpressionLanguage(Class<?> interfaceClass, Object implementation) {
        this.expressionFunctions.put(interfaceClass, implementation);
        return this;
    }

    public DocumentEngineDocxStamperConfiguration leaveEmptyOnExpressionError(boolean leaveEmpty) {
        this.leaveEmptyOnExpressionError = leaveEmpty;
        return this;
    }

    public DocumentEngineDocxStamperConfiguration replaceNullValues(boolean replaceNullValues) {
        this.replaceNullValues = replaceNullValues;
        return this;
    }

    public DocumentEngineDocxStamper build() {
        return new DocumentEngineDocxStamper(this);
    }

    EvaluationContextConfigurer getEvaluationContextConfigurer() {
        return this.evaluationContextConfigurer;
    }

    boolean isFailOnUnresolvedExpression() {
        return this.failOnUnresolvedExpression;
    }

    Map<Class<?>, ICommentProcessor> getCommentProcessors() {
        return this.commentProcessors;
    }

    Map<Class<?>, ITypeResolver> getTypeResolvers() {
        return this.typeResolvers;
    }

    ITypeResolver getDefaultTypeResolver() {
        return this.defaultTypeResolver;
    }

    public DocumentEngineDocxStamperConfiguration setDefaultTypeResolver(ITypeResolver defaultTypeResolver) {
        this.defaultTypeResolver = defaultTypeResolver;
        return this;
    }

    public boolean isLeaveEmptyOnExpressionError() {
        return this.leaveEmptyOnExpressionError;
    }

    public boolean isReplaceNullValues() {
        return this.replaceNullValues;
    }

    String getLineBreakPlaceholder() {
        return this.lineBreakPlaceholder;
    }

    public Map<Class<?>, Object> getExpressionFunctions() {
        return this.expressionFunctions;
    }
}
