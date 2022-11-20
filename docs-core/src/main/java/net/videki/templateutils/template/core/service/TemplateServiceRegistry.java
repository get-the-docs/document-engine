package net.videki.templateutils.template.core.service;

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

/**
 * Top level container for the service. This is the entry point through the
 * getInstance() method for the outside world.
 * 
 * @author Levente Ban
 */
public class TemplateServiceRegistry {
    /**
     * Lock object for config changes.
     */
    private final static Object lockObject = new Object();

    /**
     * The actual service implementation.
     */
    private static TemplateService INSTANCE = new TemplateServiceImpl();

    /**
     * Sets the template service implementation to the given.
     * 
     * @param tsImpl the desired implementation.
     */
    void setTemplateService(final TemplateService tsImpl) {
        synchronized (lockObject) {
            INSTANCE = tsImpl;
        }
    }

    /**
     * Global entry point for processing templates.
     * 
     * @return the actual template service.
     */
    public static TemplateService getInstance() {
        TemplateService result = INSTANCE;
        if (result == null) {
            synchronized (lockObject) {
                result = INSTANCE = new TemplateServiceImpl();
            }
        }
        return result;
    }
}
