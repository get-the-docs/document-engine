package org.getthedocs.documentengine.core.provider;

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

import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for filesystem-based providers.
 *
 * @author Levente Ban
 */
public abstract class FilesystemProvider {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemProvider.class);

    /**
     * The current basedir.
     */
    private String baseDir;

    /**
     * Constructor.
     *
     * @param baseDir the base directory.
     */
    public void init(final String baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * Returns the base path of the repository.
     *
     * @return the base path of the repository.
     * @throws TemplateServiceConfigurationException thrown in case of repository
     *                                               configuration errors.
     */
    public String getBasePath() throws TemplateServiceConfigurationException {
        if (this.baseDir == null) {
            final String msg = String.format(" %s repository not initialized, base path is null.", this.getClass().getCanonicalName());

            LOGGER.error(msg);
            throw new TemplateServiceConfigurationException("b1c3f0d2-4f5e-4c8a-9b6d-7f8e9a0b1c2d", msg);
        }

        return this.baseDir;
    }

}
