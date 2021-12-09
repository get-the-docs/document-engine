package net.videki.templateutils.template.core.service;

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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();

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
        try {
            writeLock.lock();

            INSTANCE = tsImpl;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Global entry point for processing templates.
     * 
     * @return the actual template service.
     */
    public static TemplateService getInstance() {
        TemplateService result;
        try {
            readLock.lock();

            result = INSTANCE;
            if (result == null) {
                result = INSTANCE = new TemplateServiceImpl();
            }
        } finally {
            readLock.unlock();
        }

        return result;
    }
}
