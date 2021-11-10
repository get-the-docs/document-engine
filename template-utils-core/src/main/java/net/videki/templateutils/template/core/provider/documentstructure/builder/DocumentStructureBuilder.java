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

package net.videki.templateutils.template.core.provider.documentstructure.builder;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;

import java.io.InputStream;

/**
 * Base interface for document structure builder implementations.
 * @author Levente Ban
 */
public interface DocumentStructureBuilder {

    /**
     * Builds a document structure descriptor from the input stream.
     * @param dsConfig the document structure as a stream in its specific format.
     * @return the document struture descriptor if the input could be successfully parsed.
     * @throws TemplateServiceConfigurationException thrown in case of configuration errors.
     */
    DocumentStructure build(InputStream dsConfig) throws TemplateServiceConfigurationException;
}
