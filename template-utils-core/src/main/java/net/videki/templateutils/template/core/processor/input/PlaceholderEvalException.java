package net.videki.templateutils.template.core.processor.input;

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

import net.videki.templateutils.template.core.service.exception.TemplateProcessException;

/**
 * Placeholder evaluation exception for indicating template parameter errors.
 * @author Levente Ban
 */
public class PlaceholderEvalException extends TemplateProcessException {

    /**
     * Constructor to initialize with a given error code and message.
     * @param errorCode the error code to identify the origin.
     * @param message error message.
     */
    public PlaceholderEvalException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructor to initialize with a given error code, message and root cause.
     * @param errorCode the error code to identify the origin.
     * @param message error message.
     * @param e the root cause.
     */
    public PlaceholderEvalException(String errorCode, String message, Throwable e) {
        super(errorCode, message);

        this.setStackTrace(e.getStackTrace());
    }
}
