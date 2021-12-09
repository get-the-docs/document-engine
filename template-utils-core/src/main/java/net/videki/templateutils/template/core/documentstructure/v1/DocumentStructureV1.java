package net.videki.templateutils.template.core.documentstructure.v1;

import net.videki.templateutils.template.core.context.dto.JsonModel;

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

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;

/**
 * <p>
 * If multiple documents have to be generated, the DocumentStructure describes
 * the template structure of the desired output document collection.
 * </p>
 *
 * @author Levente Ban
 */
public class DocumentStructureV1 extends DocumentStructure implements JsonModel {
    public DocumentStructureV1() {
        super();
    }

    public DocumentStructureV1(String id) {
        super(id);
    }

    public DocumentStructureV1(String id, String version) {
        super(id, version);
    }
}
