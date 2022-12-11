package net.videki.documentengine.core.provider.resultstore.filesystem;

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

import net.videki.documentengine.core.documentstructure.ResultDocument;
import net.videki.documentengine.core.documentstructure.GenerationResult;
import net.videki.documentengine.core.provider.resultstore.ResultStore;
import net.videki.documentengine.core.service.exception.TemplateProcessException;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileSystemResultStoreTest {

    @Test
    public void saveNullInputDocumentResultTest() {
        final ResultStore resultStore = new FileSystemResultStore();

        try {
            resultStore.save((ResultDocument) null);
        } catch (final TemplateProcessException e) {
            if (!"cb05a816-dea2-4498-823a-33fb4ece1565".equals(e.getCode())) {
                fail();
            }

        }
    }

    @Test
    public void saveNullInputGenerationResultTest() {
        final ResultStore resultStore = new FileSystemResultStore();

        try {
            resultStore.save((GenerationResult) null);
        } catch (final TemplateProcessException e) {
            if (!"0d383b14-c3ef-430d-9007-3add27e086d8".equals(e.getCode())) {
                fail();
            }

        }
    }
}
