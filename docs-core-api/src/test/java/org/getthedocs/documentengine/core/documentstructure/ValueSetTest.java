package org.getthedocs.documentengine.core.documentstructure;

/*-
 * #%L
 * docs-core-api
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

import org.getthedocs.documentengine.core.context.TemplateContext;
import org.getthedocs.documentengine.core.documentstructure.descriptors.TemplateElementId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ValueSetTest {

    /**
     * Tests for the ` getGlobalContext ` method in the `ValueSet` class.
     * <p>
     * This method returns an `Optional<TemplateContext>` containing the global template context
     * if it exists, or an empty `Optional` if the global context is not available.
     */

    @Test
    public void testGetGlobalContextWhenGlobalTemplateExists() {
        // Arrange
        final ValueSet valueSet = new ValueSet("transaction-1", Locale.ENGLISH);
        final String elementId = "element-1";
        final String contextKey = "key-1";
        final String dto = "test-data";

        final String contextKey2 = "key-2";
        final String dto2 = "test-data2";

        // Act
        valueSet.addContext(elementId, contextKey, dto);
        valueSet.addContext(TemplateElementId.getGlobalTemplateElementId().getId(), contextKey2, dto2);

        final Optional<TemplateContext> result = valueSet.getGlobalContext();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getCtx().size());
        assertEquals(dto2, result.get().getCtx().values().stream().findFirst().get());
    }

    @Test
    public void testGetGlobalContextWhenGlobalTemplateDoesNotExist() {
        // Arrange
        final ValueSet valueSet = new ValueSet("transaction-2", Locale.ENGLISH);

        // Act
        Optional<TemplateContext> result = valueSet.getGlobalContext();

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void testAddContextWithElementIdAndContextKey() {
        // Arrange
        final ValueSet valueSet = new ValueSet("transaction-1", Locale.ENGLISH);
        final String elementId = "element-1";
        final String contextKey = "key-1";
        final String dto = "test-data";

        // Act
        valueSet.addContext(elementId, contextKey, dto);

        // Assert
        final TemplateContext storedContext = valueSet.getValues().get(new TemplateElementId(elementId));
        assertNotNull(storedContext);
        assertEquals(dto, storedContext.getCtx().get(contextKey));
    }

    @Test
    public void testAddDefaultContext() {
        // Arrange
        final ValueSet valueSet = new ValueSet("transaction-2", Locale.ENGLISH);
        final String elementId = "element-2";
        final String dto = "default-data";

        // Act
        valueSet.addDefaultContext(elementId, dto);

        // Assert
        final TemplateContext storedContext = valueSet.getValues().get(new TemplateElementId(elementId));
        assertNotNull(storedContext);
        assertEquals(dto, storedContext.getCtx().get(TemplateContext.CONTEXT_ROOT_KEY_MODEL));
    }

    @Test
    public void testAddContextWithElementIdAndTemplateContext() {
        // Arrange
        final ValueSet valueSet = new ValueSet("transaction-3", Locale.ENGLISH);
        final String elementId = "element-3";
        final TemplateContext mockContext = new TemplateContext();

        // Act
        valueSet.addContext(elementId, mockContext);

        // Assert
        final TemplateContext storedContext = valueSet.getValues().get(new TemplateElementId(elementId));
        assertNotNull(storedContext);
        assertEquals(mockContext, storedContext);
    }
}
