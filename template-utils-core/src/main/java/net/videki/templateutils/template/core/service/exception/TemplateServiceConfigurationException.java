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

package net.videki.templateutils.template.core.service.exception;

/**
 * Exception class to indicate configuration errors related to the template
 * service or its components.
 * 
 * @author Levente Ban
 */
public class TemplateServiceConfigurationException extends TemplateServiceException {

    /**
     * Base error message.
     */
    public static final String MSG_INVALID_PARAMETERS = "Invalid parameters";

    /**
     * Constructor with given error code and message.
     * 
     * @param errorCode the error code (by default constant uuids to be able to
     *                  identify the raise location without debug info too).
     * @param msg       the error message.
     */
    public TemplateServiceConfigurationException(final String errorCode, final String msg) {
        super(errorCode, msg);
    }
}
