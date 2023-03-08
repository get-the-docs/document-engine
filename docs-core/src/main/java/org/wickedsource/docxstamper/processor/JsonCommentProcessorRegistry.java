/*
 * Copyright (c) 2021. Levente Ban
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

package org.wickedsource.docxstamper.processor;

/*-
 * #%L
 * docs-core
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

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Comments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParseException;
import org.wickedsource.docxstamper.api.DocxStamperException;
import org.wickedsource.docxstamper.api.UnresolvedExpressionException;
import org.wickedsource.docxstamper.api.commentprocessor.ICommentProcessor;
import org.wickedsource.docxstamper.api.coordinates.ParagraphCoordinates;
import org.wickedsource.docxstamper.api.coordinates.RunCoordinates;
import org.wickedsource.docxstamper.el.ExpressionUtil;
import org.wickedsource.docxstamper.jsonpath.JsonExpressionResolver;
import org.wickedsource.docxstamper.proxy.ProxyBuilder;
import org.wickedsource.docxstamper.proxy.ProxyException;
import org.wickedsource.docxstamper.replace.PlaceholderReplacer;
import org.wickedsource.docxstamper.util.CommentUtil;
import org.wickedsource.docxstamper.util.CommentWrapper;
import org.wickedsource.docxstamper.util.ParagraphWrapper;
import org.wickedsource.docxstamper.util.walk.BaseCoordinatesWalker;
import org.wickedsource.docxstamper.util.walk.CoordinatesWalker;

import java.math.BigInteger;
import java.util.*;

/**
 * Json comment processor for the docx stamper.
 *
 * @author Levente Ban
 */
public class JsonCommentProcessorRegistry {
    private Logger logger = LoggerFactory.getLogger(CommentProcessorRegistry.class);

    private Map<ICommentProcessor, Class<?>> commentProcessorInterfaces = new HashMap<>();

    private List<ICommentProcessor> commentProcessors = new ArrayList<>();

    private JsonExpressionResolver expressionResolver = new JsonExpressionResolver();

    private ExpressionUtil expressionUtil = new ExpressionUtil();

    private PlaceholderReplacer placeholderReplacer;

    private boolean failOnInvalidExpression = true;

    public JsonCommentProcessorRegistry(PlaceholderReplacer placeholderReplacer) {
        this.placeholderReplacer = placeholderReplacer;
    }

    public void setExpressionResolver(JsonExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

    public void registerCommentProcessor(Class<?> interfaceClass,
                                         ICommentProcessor commentProcessor) {
        this.commentProcessorInterfaces.put(commentProcessor, interfaceClass);
        this.commentProcessors.add(commentProcessor);
    }

    /**
     * Lets each registered ICommentProcessor have a run on the specified docx
     * document. At the end of the document the commit method is called for each
     * ICommentProcessor. The ICommentProcessors are run in the order they were
     * registered.
     *
     * @param document    the docx document over which to run the registered ICommentProcessors.
     * @param proxyBuilder a builder for a proxy around the context root object to customize its interface
     * @param <T>         type of the contextRoot object.
     */
    public <T> void runProcessors(final WordprocessingMLPackage document, final ProxyBuilder<T> proxyBuilder) {
        final Map<BigInteger, CommentWrapper> comments = CommentUtil.getComments(document);
        final List<CommentWrapper> proceedComments = new ArrayList<>();

        CoordinatesWalker walker = new BaseCoordinatesWalker(document) {

            @Override
            protected void onParagraph(ParagraphCoordinates paragraphCoordinates) {
                runProcessorsOnParagraphComment(document, comments, proxyBuilder, paragraphCoordinates)
                        .ifPresent(proceedComments::add);
                runProcessorsOnInlineContent(proxyBuilder, paragraphCoordinates);
            }

            @Override
            protected void onRun(RunCoordinates runCoordinates,
                                 ParagraphCoordinates paragraphCoordinates) {
                runProcessorsOnRunComment(document, comments, proxyBuilder, paragraphCoordinates, runCoordinates)
                        .ifPresent(proceedComments::add);
            }

        };
        walker.walk();

        for (ICommentProcessor processor : commentProcessors) {
            processor.commitChanges(document);
        }
        for (CommentWrapper commentWrapper : proceedComments) {
            CommentUtil.deleteComment(commentWrapper);
        }

    }

    /**
     * Finds all processor expressions within the specified paragraph and tries
     * to evaluate it against all registered {@link ICommentProcessor}s.
     *
     * @param proxyBuilder         a builder for a proxy around the context root object to customize its interface
     * @param paragraphCoordinates the paragraph to process.
     * @param <T>                  type of the context root object
     */
    private <T> void runProcessorsOnInlineContent(ProxyBuilder<T> proxyBuilder,
                                                  ParagraphCoordinates paragraphCoordinates) {

        ParagraphWrapper paragraph = new ParagraphWrapper(paragraphCoordinates.getParagraph());
        List<String> processorExpressions = expressionUtil
                .findProcessorExpressions(paragraph.getText());

        for (String processorExpression : processorExpressions) {
            String strippedExpression = expressionUtil.stripExpression(processorExpression);

            for (final ICommentProcessor processor : commentProcessors) {
                Class<?> commentProcessorInterface = commentProcessorInterfaces.get(processor);
                proxyBuilder.withInterface(commentProcessorInterface, processor);
                processor.setCurrentParagraphCoordinates(paragraphCoordinates);
            }

            try {
                T contextRootProxy = proxyBuilder.build();
                expressionResolver.resolveExpression(strippedExpression, contextRootProxy);
                placeholderReplacer.replace(paragraph, processorExpression, null);
                logger.debug("Processor expression '{}' has been successfully processed by a comment processor.",
                        processorExpression);
            } catch (SpelEvaluationException | SpelParseException e) {
                if (failOnInvalidExpression) {
                    throw new UnresolvedExpressionException(strippedExpression, e);
                } else {
                    logger.warn("Skipping processor expression '{}' because it can not be resolved by any comment processor. Reason: {}. Set log level to TRACE to view Stacktrace.",
                            processorExpression, e.getMessage());
                    logger.trace("Reason for skipping processor expression: ", e);
                }
            } catch (ProxyException e) {
                throw new DocxStamperException("Could not create a proxy around context root object", e);
            }
        }
    }


    /**
     * Takes the first comment on the specified paragraph and tries to evaluate
     * the string within the comment against all registered
     * {@link ICommentProcessor}s.
     *
     * @param document             the word document.
     * @param comments             the comments within the document.
     * @param proxyBuilder          a builder for a proxy around the context root object to customize its interface
     * @param paragraphCoordinates the paragraph whose comments to evaluate.
     * @param <T>                  the type of the context root object.
     */
    private <T> Optional<CommentWrapper> runProcessorsOnParagraphComment(final WordprocessingMLPackage document,
                                                                         final Map<BigInteger, CommentWrapper> comments, ProxyBuilder<T> proxyBuilder,
                                                                         ParagraphCoordinates paragraphCoordinates) {
        Comments.Comment comment = CommentUtil.getCommentFor(paragraphCoordinates.getParagraph(), document);
        return runCommentProcessors(document, comments, proxyBuilder, comment, paragraphCoordinates, null);
    }

    private <T> Optional<CommentWrapper> runProcessorsOnRunComment(final WordprocessingMLPackage document,
                                                                   final Map<BigInteger, CommentWrapper> comments, ProxyBuilder<T> proxyBuilder,
                                                                   ParagraphCoordinates paragraphCoordinates, RunCoordinates runCoordinates) {
        Comments.Comment comment = CommentUtil.getCommentAround(runCoordinates.getRun(), document);
        return runCommentProcessors(document, comments, proxyBuilder, comment, paragraphCoordinates, runCoordinates);
    }

    private <T> Optional<CommentWrapper> runCommentProcessors(final WordprocessingMLPackage document,
                                                              final Map<BigInteger, CommentWrapper> comments, ProxyBuilder<T> proxyBuilder,
                                                              Comments.Comment comment, ParagraphCoordinates paragraphCoordinates,
                                                              RunCoordinates runCoordinates) {

        CommentWrapper commentWrapper = Optional.ofNullable(comment)
                .map(Comments.Comment::getId)
                .map(comments::get)
                .orElse(null);

        if (Objects.isNull(comment) || Objects.isNull(commentWrapper)) {
            // no comment to process
            return Optional.empty();
        }

        String commentString = CommentUtil.getCommentString(comment);

        for (final ICommentProcessor processor : commentProcessors) {
            Class<?> commentProcessorInterface = commentProcessorInterfaces.get(processor);
            proxyBuilder.withInterface(commentProcessorInterface, processor);
            processor.setCurrentParagraphCoordinates(paragraphCoordinates);
            processor.setCurrentRunCoordinates(runCoordinates);
            processor.setCurrentCommentWrapper(commentWrapper);
        }

        try {
            T contextRootProxy = proxyBuilder.build();
            expressionResolver.resolveExpression(commentString, contextRootProxy);
            comments.remove(comment.getId()); // guarantee one-time processing
            logger.debug(
                    "Comment '{}' has been successfully processed by a comment processor.",
                            commentString);
            return Optional.of(commentWrapper);
        } catch (SpelEvaluationException | SpelParseException e) {
            if (failOnInvalidExpression) {
                throw new UnresolvedExpressionException(commentString, e);
            } else {
                logger.warn(
                        "Skipping comment expression '{}' because it can not be resolved by any comment processor. Reason: {}. Set log level to TRACE to view Stacktrace.",
                        commentString, e.getMessage());
                logger.trace("Reason for skipping comment: ", e);
            }
        } catch (ProxyException e) {
            throw new DocxStamperException("Could not create a proxy around context root object", e);
        }
        return Optional.empty();
    }

    public boolean isFailOnInvalidExpression() {
        return failOnInvalidExpression;
    }

    public void setFailOnInvalidExpression(boolean failOnInvalidExpression) {
        this.failOnInvalidExpression = failOnInvalidExpression;
    }

    public void reset() {
        for (ICommentProcessor processor : commentProcessors) {
            processor.reset();
        }
    }
}
