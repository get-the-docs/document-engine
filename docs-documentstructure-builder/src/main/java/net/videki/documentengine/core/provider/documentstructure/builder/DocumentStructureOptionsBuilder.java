package net.videki.documentengine.core.provider.documentstructure.builder;

/*-
 * #%L
 * docs-documentstructure-builder
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

import net.videki.documentengine.core.documentstructure.descriptors.DocumentStructureOptions;
import net.videki.documentengine.core.service.exception.TemplateServiceConfigurationException;

import java.io.InputStream;

public interface DocumentStructureOptionsBuilder extends DocumentStructureBuilder {
    DocumentStructureOptions buildOptions(InputStream dsConfig) throws TemplateServiceConfigurationException;
}
