package org.getthedocs.documentengine.core.processor.input.noop;

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

import org.getthedocs.documentengine.core.processor.input.InputTemplateProcessor;
import org.getthedocs.documentengine.core.processor.AbstractTemplateProcessor;
import org.getthedocs.documentengine.core.service.InputFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Identity template processor.
 * It produces a result document from the input template without changes.
 * @author Levente Ban
 */
public class NoopTemplateProcessor extends AbstractTemplateProcessor implements InputTemplateProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NoopTemplateProcessor.class);

    /**
     * Returns the input format.
     * @return the input format.
     */
    @Override
    public InputFormat getInputFormat() {
        return null;
    }

    /**
     * Entry point to "process" the template. 
     * @param templateFileName the template name in the template repository.
     * @param dto the model object. (is omitted)
     * @return the result document stream.
     */
    @Override
    public <T> OutputStream fill(String templateFileName, T dto) {
    
        try (final InputStream is = getTemplate(templateFileName);
            final ByteArrayOutputStream targetStream = new ByteArrayOutputStream()) {

            is.transferTo(targetStream);

            return targetStream;
        } catch (TemplateServiceConfigurationException e) {
            final String msg = String.format("Error getting template: %s", templateFileName);
            LOGGER.error(msg);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug(msg, e);
            }

            return null;
        } catch(final IOException e) {
            final String msg = "Error creating result stream";
            LOGGER.error(msg);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug(msg, e);
            }

            return null;
        }
    }
}
