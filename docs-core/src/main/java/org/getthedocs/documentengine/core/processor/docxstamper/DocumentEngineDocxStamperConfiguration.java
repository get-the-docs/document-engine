package org.getthedocs.documentengine.core.processor.docxstamper;

import io.reflectoring.docxstamper.api.EvaluationContextConfigurer;
import io.reflectoring.docxstamper.api.commentprocessor.ICommentProcessor;
import io.reflectoring.docxstamper.api.typeresolver.ITypeResolver;
import io.reflectoring.docxstamper.el.NoOpEvaluationContextConfigurer;
import io.reflectoring.docxstamper.replace.typeresolver.FallbackResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * Copy of the DocxStamperConfiguration class from the docx-stamper library to add JSON placeholder capabilities.
 * @see io.reflectoring.docxstamper.DocxStamperConfiguration
 */
public class DocumentEngineDocxStamperConfiguration {

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#setLineBreakPlaceholder(String)
     */
    private String lineBreakPlaceholder;

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#setEvaluationContextConfigurer(EvaluationContextConfigurer)
     */
    private EvaluationContextConfigurer evaluationContextConfigurer = new NoOpEvaluationContextConfigurer();

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration
     */
    private boolean failOnUnresolvedExpression = true;

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#leaveEmptyOnExpressionError(boolean)
     */
    private boolean leaveEmptyOnExpressionError = false;

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#replaceNullValues(boolean)
     */

    private boolean replaceNullValues = false;

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#setFailOnUnresolvedExpression(boolean)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<Class<?>, ICommentProcessor> commentProcessors = new HashMap();

    /**
     * @link io.reflectoring.docxstamper.api.DocxStamperConfiguration#addTypeResolver(Class, ITypeResolver)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<Class<?>, ITypeResolver> typeResolvers = new HashMap();

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#addTypeResolver(Class, ITypeResolver)
     */
    @SuppressWarnings({"rawtypes"})
    private ITypeResolver defaultTypeResolver = new FallbackResolver();

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<Class<?>, Object> expressionFunctions = new HashMap();

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#setLineBreakPlaceholder(String)
     * @param lineBreakPlaceholder line break placeholder to be used in the template
     * @return the current configuration instance
     */
    public DocumentEngineDocxStamperConfiguration setLineBreakPlaceholder(String lineBreakPlaceholder) {
        this.lineBreakPlaceholder = lineBreakPlaceholder;
        return this;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#setEvaluationContextConfigurer(EvaluationContextConfigurer)
     * @param evaluationContextConfigurer the evaluation context configurer to be used in the template
     * @return the current configuration instance
     */
    public DocumentEngineDocxStamperConfiguration setEvaluationContextConfigurer(EvaluationContextConfigurer evaluationContextConfigurer) {
        this.evaluationContextConfigurer = evaluationContextConfigurer;
        return this;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration
     * @param failOnUnresolvedExpression flag to indicate whether to fail on unresolved expressions
     * @return the current configuration instance
     */
    public DocumentEngineDocxStamperConfiguration setFailOnUnresolvedExpression(boolean failOnUnresolvedExpression) {
        this.failOnUnresolvedExpression = failOnUnresolvedExpression;
        return this;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration
     * @param interfaceClass the interface class to be used for the comment processor
     * @param commentProcessor the comment processor to be used for the interface class
     * @return the current configuration instance
     */
    public DocumentEngineDocxStamperConfiguration addCommentProcessor(Class<?> interfaceClass, ICommentProcessor commentProcessor) {
        this.commentProcessors.put(interfaceClass, commentProcessor);
        return this;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#addTypeResolver(Class, ITypeResolver)
     * @param resolvedType the type to be resolved
     * @param resolver the resolver class to be used for the type
     * @param <T> the type to be resolved
     * @return the current configuration instance
     */
    @SuppressWarnings({"rawtypes"})
    public <T> DocumentEngineDocxStamperConfiguration addTypeResolver(Class<T> resolvedType, ITypeResolver resolver) {
        this.typeResolvers.put(resolvedType, resolver);
        return this;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#exposeInterfaceToExpressionLanguage(Class, Object)
     * @param interfaceClass the interface to be exposed to the expression language
     * @param implementation the implementation of the interface to be exposed to the expression language
     * @return the current configuration instance
     */
    public DocumentEngineDocxStamperConfiguration exposeInterfaceToExpressionLanguage(Class<?> interfaceClass, Object implementation) {
        this.expressionFunctions.put(interfaceClass, implementation);
        return this;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#leaveEmptyOnExpressionError(boolean)
     * @param leaveEmpty the flag to indicate whether to leave the field empty on expression error
     * @return the current configuration instance
     */
    public DocumentEngineDocxStamperConfiguration leaveEmptyOnExpressionError(boolean leaveEmpty) {
        this.leaveEmptyOnExpressionError = leaveEmpty;
        return this;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#replaceNullValues(boolean)
     * @param replaceNullValues the flag to indicate whether to replace null values with empty strings
     * @return the current configuration instance
     */
    public DocumentEngineDocxStamperConfiguration replaceNullValues(boolean replaceNullValues) {
        this.replaceNullValues = replaceNullValues;
        return this;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#build()
     * @return the current configuration instance
     */
    @SuppressWarnings({"rawtypes"})
    public DocumentEngineDocxStamper build() {
        return new DocumentEngineDocxStamper(this);
    }

    /**
     * @link io.reflectoring.docxstamper.api.DocxStamperConfiguration#getEvaluationContextConfigurer()
     * @return the evaluation context configurer
     */
    EvaluationContextConfigurer getEvaluationContextConfigurer() {
        return this.evaluationContextConfigurer;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration
     * @return the flag indicating whether to fail on unresolved expressions
     */
    boolean isFailOnUnresolvedExpression() {
        return this.failOnUnresolvedExpression;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration
     * @return the map of the configured comment processors
     */
    Map<Class<?>, ICommentProcessor> getCommentProcessors() {
        return this.commentProcessors;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration
     * @return the map of the configured type resolvers
     */
    @SuppressWarnings({"rawtypes"})
    Map<Class<?>, ITypeResolver> getTypeResolvers() {
        return this.typeResolvers;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration
     * @return the default type resolver
     */
    @SuppressWarnings({"rawtypes"})
    ITypeResolver getDefaultTypeResolver() {
        return this.defaultTypeResolver;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration
     * @param defaultTypeResolver the default type resolver to be used
     * @return the current configuration instance
     */
    @SuppressWarnings({"rawtypes"})
    public DocumentEngineDocxStamperConfiguration setDefaultTypeResolver(ITypeResolver defaultTypeResolver) {
        this.defaultTypeResolver = defaultTypeResolver;
        return this;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#getExpressionFunctions()
     * @return the flag indicating whether to leave the field empty on expression error
     */
    public boolean isLeaveEmptyOnExpressionError() {
        return this.leaveEmptyOnExpressionError;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#getExpressionFunctions()
     * @return the flag indicating whether to replace null values with empty strings
     */
    public boolean isReplaceNullValues() {
        return this.replaceNullValues;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration
     * @return the line break placeholder to be used in the template
     */
    String getLineBreakPlaceholder() {
        return this.lineBreakPlaceholder;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamperConfiguration#getExpressionFunctions()
     * @return the map of the configured expression functions
     */
    public Map<Class<?>, Object> getExpressionFunctions() {
        return this.expressionFunctions;
    }
}
