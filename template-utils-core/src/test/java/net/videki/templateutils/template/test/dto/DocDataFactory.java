package net.videki.templateutils.template.test.dto;

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

import net.videki.templateutils.template.test.dto.doc.DocumentProperties;

import java.time.LocalDateTime;

public class DocDataFactory {
    public static DocumentProperties createDocData(final String transactionId) {
        final DocumentProperties result = new DocumentProperties();

        result.setLogin("PB\\cnorris");
        result.setGenerationDate(LocalDateTime.now());
        result.setDmsUrl("http://dms.internal.pbvintage.com/" + transactionId);

        return result;
    }

}
