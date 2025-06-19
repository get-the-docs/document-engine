package org.getthedocs.documentengine.core.processor.docxstamper.processor;

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

import io.reflectoring.docxstamper.api.DocxStamperException;
import io.reflectoring.docxstamper.api.UnresolvedExpressionException;
import io.reflectoring.docxstamper.api.commentprocessor.ICommentProcessor;
import io.reflectoring.docxstamper.api.coordinates.ParagraphCoordinates;
import io.reflectoring.docxstamper.api.coordinates.RunCoordinates;
import io.reflectoring.docxstamper.el.ExpressionResolver;
import io.reflectoring.docxstamper.el.ExpressionUtil;
import io.reflectoring.docxstamper.processor.CommentProcessorRegistry;
import io.reflectoring.docxstamper.proxy.ProxyBuilder;
import io.reflectoring.docxstamper.proxy.ProxyException;
import io.reflectoring.docxstamper.replace.PlaceholderReplacer;
import io.reflectoring.docxstamper.util.CommentUtil;
import io.reflectoring.docxstamper.util.CommentWrapper;
import io.reflectoring.docxstamper.util.ParagraphWrapper;
import io.reflectoring.docxstamper.util.walk.BaseCoordinatesWalker;
import io.reflectoring.docxstamper.util.walk.CoordinatesWalker;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.CTMarkup;
import org.docx4j.wml.Comments;
import org.getthedocs.documentengine.core.processor.docxstamper.el.JsonExpressionResolver;
import org.getthedocs.documentengine.core.processor.input.docx.DocxStamperInputTemplateProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParseException;

import java.math.BigInteger;
import java.util.*;

public class DocumentEngineCommentProcessorRegistry extends CommentProcessorRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocxStamperInputTemplateProcessor.class);
    private ExpressionResolver expressionResolver = new JsonExpressionResolver();

    public DocumentEngineCommentProcessorRegistry(PlaceholderReplacer placeholderReplacer) {
        super(placeholderReplacer);
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }
}
