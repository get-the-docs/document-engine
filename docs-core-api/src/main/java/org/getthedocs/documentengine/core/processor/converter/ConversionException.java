package org.getthedocs.documentengine.core.processor.converter;

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

import org.getthedocs.documentengine.core.service.exception.TemplateProcessException;

/**
 * Document format conversion exception.
 * 
 * @author Levente Ban
 */
public class ConversionException extends TemplateProcessException {

    /**
     * Constructor with a given error code and message.
     * @param errorCode the error code - unique uuids for each throw to identify the origin in builds without debug info. 
     * @param message the error message.
     */
    public ConversionException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructor with a given error code, message and a root cause.
     * @param errorCode the error code - unique uuids for each throw to identify the origin in builds without debug info. 
     * @param message the error message.
     * @param e the root cause.
     */
    public ConversionException(String errorCode, String message, Throwable e) {
        super(errorCode, message);
        this.setStackTrace(e.getStackTrace());
    }
}
