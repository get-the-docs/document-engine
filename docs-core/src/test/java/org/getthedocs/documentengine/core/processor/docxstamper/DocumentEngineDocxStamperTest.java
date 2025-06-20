package org.getthedocs.documentengine.core.processor.docxstamper;

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

import io.reflectoring.docxstamper.api.DocxStamperException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

class DocumentEngineDocxStamperTest {

    @Test
    void stampWithNullParamsShouldThrowDocxStamperException() {
        Exception e = assertThrows(DocxStamperException.class, () -> {
            final DocumentEngineDocxStamper<Object> stamper = new DocumentEngineDocxStamper<>();
            stamper.stamp((InputStream) null, (Object) null, (OutputStream) null);
        });
    }

    @Test
    void stampDocx4JWithNullParamsShouldThrowDocxStamperException() {
        Exception e = assertThrows(DocxStamperException.class, () -> {
            final DocumentEngineDocxStamper<Object> stamper = new DocumentEngineDocxStamper<>();
            stamper.stamp((WordprocessingMLPackage) null, (Object) null, (OutputStream) null);
        });
    }
}
