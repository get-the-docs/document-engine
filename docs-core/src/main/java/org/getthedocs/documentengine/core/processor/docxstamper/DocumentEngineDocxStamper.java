package org.getthedocs.documentengine.core.processor.docxstamper;

import io.reflectoring.docxstamper.api.DocxStamperException;
import io.reflectoring.docxstamper.api.commentprocessor.ICommentProcessor;
import io.reflectoring.docxstamper.api.typeresolver.ITypeResolver;
import io.reflectoring.docxstamper.api.typeresolver.TypeResolverRegistry;
import io.reflectoring.docxstamper.el.ExpressionResolver;
import io.reflectoring.docxstamper.processor.CommentProcessorRegistry;
import io.reflectoring.docxstamper.processor.displayif.DisplayIfProcessor;
import io.reflectoring.docxstamper.processor.displayif.IDisplayIfProcessor;
import io.reflectoring.docxstamper.processor.repeat.*;
import io.reflectoring.docxstamper.processor.replaceExpression.IReplaceWithProcessor;
import io.reflectoring.docxstamper.processor.replaceExpression.ReplaceWithProcessor;
import io.reflectoring.docxstamper.proxy.ProxyBuilder;
import io.reflectoring.docxstamper.replace.PlaceholderReplacer;
import io.reflectoring.docxstamper.replace.typeresolver.DateResolver;
import io.reflectoring.docxstamper.replace.typeresolver.FallbackResolver;
import io.reflectoring.docxstamper.replace.typeresolver.image.Image;
import io.reflectoring.docxstamper.replace.typeresolver.image.ImageResolver;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.getthedocs.documentengine.core.processor.docxstamper.el.JsonExpressionResolver;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

/**
 * Copy of the DocxStamper class from the docx-stamper library to add JSON placeholder capabilities.
 */

public class DocumentEngineDocxStamper<T> {

    /**
     * @link io.reflectoring.docxstamper.api.DocxStamper#placeholderReplacer
     */
    private PlaceholderReplacer<T> placeholderReplacer;

    /**
     * @link io.reflectoring.docxstamper.api.DocxStamper#commentProcessorRegistry
     */
    private CommentProcessorRegistry commentProcessorRegistry;

    /**
     * @link io.reflectoring.docxstamper.api.DocxStamper#typeResolverRegistry
     */
    private TypeResolverRegistry typeResolverRegistry;

    /**
     * @link io.reflectoring.docxstamper.api.DocxStamper#proxyBuilder
     */
    private DocumentEngineDocxStamperConfiguration config = new DocumentEngineDocxStamperConfiguration();

    /**
     * @link io.reflectoring.docxstamper.api.DocxStamper#proxyBuilder
     */
    @SuppressWarnings("rawtypes")
    private ProxyBuilder proxyBuilder = new ProxyBuilder();

    /**
     * @see io.reflectoring.docxstamper.DocxStamper
     */
    public DocumentEngineDocxStamper() {
        initFields();
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamper
     * @param config the configuration to use
     */
    public DocumentEngineDocxStamper(DocumentEngineDocxStamperConfiguration config) {
        this.config = config;
        initFields();
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamper
     * @param proxyBuilder the proxy builder class to use
     */
    public DocumentEngineDocxStamper(ProxyBuilder proxyBuilder) {
        this.proxyBuilder = proxyBuilder;
        initFields();
    }

    /**
     * @link io.reflectoring.docxstamper.DocxStamper#initFields
     */
    private void initFields() {
        typeResolverRegistry = new TypeResolverRegistry(new FallbackResolver());
        typeResolverRegistry.registerTypeResolver(Image.class, new ImageResolver());
        typeResolverRegistry.registerTypeResolver(Date.class, new DateResolver("dd.MM.yyyy"));
        for (Map.Entry<Class<?>, ITypeResolver> entry : config.getTypeResolvers().entrySet()) {
            typeResolverRegistry.registerTypeResolver(entry.getKey(), entry.getValue());
        }

        ExpressionResolver expressionResolver = new JsonExpressionResolver(config.getEvaluationContextConfigurer());
        placeholderReplacer = new PlaceholderReplacer<>(typeResolverRegistry, config.getLineBreakPlaceholder());
        placeholderReplacer.setExpressionResolver(expressionResolver);
        placeholderReplacer.setLeaveEmptyOnExpressionError(config.isLeaveEmptyOnExpressionError());
        placeholderReplacer.setReplaceNullValues(config.isReplaceNullValues());

        commentProcessorRegistry = new CommentProcessorRegistry(placeholderReplacer);
        commentProcessorRegistry.setExpressionResolver(expressionResolver);
        commentProcessorRegistry.setFailOnInvalidExpression(config.isFailOnUnresolvedExpression());
        commentProcessorRegistry.registerCommentProcessor(IRepeatProcessor.class, new RepeatProcessor(typeResolverRegistry, expressionResolver));
        commentProcessorRegistry.registerCommentProcessor(IParagraphRepeatProcessor.class, new ParagraphRepeatProcessor(typeResolverRegistry, expressionResolver));
        commentProcessorRegistry.registerCommentProcessor(IRepeatDocPartProcessor.class, new RepeatDocPartProcessor(typeResolverRegistry, expressionResolver));
        commentProcessorRegistry.registerCommentProcessor(IDisplayIfProcessor.class, new DisplayIfProcessor());
        commentProcessorRegistry.registerCommentProcessor(IReplaceWithProcessor.class,
                new ReplaceWithProcessor());
        for (Map.Entry<Class<?>, ICommentProcessor> entry : config.getCommentProcessors().entrySet()) {
            commentProcessorRegistry.registerCommentProcessor(entry.getKey(), entry.getValue());
        }
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamper#stamp
     * @param template the input template as an InputStream
     * @param contextRoot the context root object containing the data to be filled into the template
     * @param out the output stream to write the filled template to
     * @throws DocxStamperException thrown if an error occurs during the stamping process
     */
    public void stamp(InputStream template, T contextRoot, OutputStream out) throws DocxStamperException {
        try {
            WordprocessingMLPackage document = WordprocessingMLPackage.load(template);
            stamp(document, contextRoot, out);
        } catch (DocxStamperException e) {
            throw e;
        } catch (Exception e) {
            throw new DocxStamperException(e);
        }
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamper#stamp
     * @param document the input template as a WordprocessingMLPackage
     * @param contextRoot the context root object containing the data to be filled into the template
     * @param out the output stream to write the filled template to
     * @throws DocxStamperException thrown if an error occurs during the stamping process
     */
     void stamp(WordprocessingMLPackage document, T contextRoot, OutputStream out) throws DocxStamperException {
        try {
            ProxyBuilder<T> proxyBuilder = addCustomInterfacesToContextRoot(contextRoot, this.config.getExpressionFunctions());
            replaceExpressions(document, proxyBuilder);
            processComments(document, proxyBuilder);
            replaceExpressions(document, proxyBuilder);
            postProcess();
            document.save(out);
            commentProcessorRegistry.reset();
        } catch (DocxStamperException e) {
            throw e;
        } catch (Exception e) {
            throw new DocxStamperException(e);
        }
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamper#addCustomInterfacesToContextRoot
     * @param contextRoot the context root object containing the data to be filled into the template
     * @param interfacesToImplementations the implementations of the custom resolver interfaces
     * @return the proxy builder with the custom resolver interfaces added to the context root
     */
    private ProxyBuilder<T> addCustomInterfacesToContextRoot(T contextRoot, Map<Class<?>, Object> interfacesToImplementations) {
        ProxyBuilder<T> proxyBuilder = new ProxyBuilder<T>()
                .withRoot(contextRoot);
        if (interfacesToImplementations.isEmpty()) {
            return proxyBuilder;
        }
        for (Map.Entry<Class<?>, Object> entry : interfacesToImplementations.entrySet()) {
            Class<?> interfaceClass = entry.getKey();
            Object implementation = entry.getValue();
            proxyBuilder.withInterface(interfaceClass, implementation);
        }
        return proxyBuilder;
    }

    /**
     * @see io.reflectoring.docxstamper.DocxStamper#replaceExpressions
     * @param document the input template as a WordprocessingMLPackage
     * @param proxyBuilder the proxy builder with the custom resolver interfaces added to the context root
     */
    private void replaceExpressions(WordprocessingMLPackage document, ProxyBuilder<T> proxyBuilder) {
        placeholderReplacer.resolveExpressions(document, proxyBuilder);
    }

    /**
     * @link io.reflectoring.docxstamper.api.DocxStamper#processComments
     * @param document the input template as a WordprocessingMLPackage
     * @param proxyBuilder the proxy builder with the custom resolver interfaces added to the context root
     */
    private void processComments(final WordprocessingMLPackage document, ProxyBuilder<T> proxyBuilder) {
        commentProcessorRegistry.runProcessors(document, proxyBuilder);
    }

    /**
     * @link io.reflectoring.docxstamper.api.DocxStamper#postProcess
     */
    private void postProcess() {
        if(placeholderReplacer.isLeaveEmptyOnExpressionError()) {
            placeholderReplacer.applyLeaveEmptyErrorExpressions();
        }
    }

}

