package net.videki.templateutils.template.core.service.exception;

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

/**
 * Base exception class to indicate service errors.
 * 
 * @author Levente Ban
 */
public class TemplateServiceException extends Exception {
    /**
     * the error code.
     */
    private String code;

    /**
     * Constructor with given error code and message.
     * 
     * @param errorCode the error code.
     * @param message   the error message.
     */
    public TemplateServiceException(String errorCode, String message) {
        super(message);
        this.code = errorCode;
    }

    /**
     * Returns the error code.
     * 
     * @return the error code.
     */
    public String getCode() {
        return code;
    }

}
