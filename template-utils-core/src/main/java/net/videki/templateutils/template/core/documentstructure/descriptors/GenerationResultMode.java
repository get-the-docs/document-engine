package net.videki.templateutils.template.core.documentstructure.descriptors;

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
 * Defines the generation result of the whole generation.
 *
 * @author Levente Ban
 */
public enum GenerationResultMode {
    /**
     * Generate one output for all template elements
     */
    SEPARATE_DOCUMENTS,
    /**
     * Generate one result document by concatenating all parts defined by the document structure order and counts
     */
    SINGLE_OUTPUT;
}
