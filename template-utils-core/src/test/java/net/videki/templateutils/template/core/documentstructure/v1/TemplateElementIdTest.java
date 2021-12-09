package net.videki.templateutils.template.core.documentstructure.v1;

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

import net.videki.templateutils.template.core.documentstructure.TemplateElementId;
import org.junit.Assert;
import org.junit.Test;

public class TemplateElementIdTest {

    @Test
    public void noArgConstructorShouldReturnRandomUuid() {
        final var te = new TemplateElementId();

        Assert.assertNotNull(te.getId());
    }

    @Test
    public void setGlobalShouldReturnKindGlobal() {
        final var te = new TemplateElementId();

        te.setGlobal();
        Assert.assertEquals(TemplateElementId.TEMPLATE_KIND_GLOBAL, te.getId());
    }

    @Test
    public void randomIdOverrideShouldReturnNewId() {
        final var te = new TemplateElementId();

        te.setId("WATCH_THIS");
        Assert.assertEquals("WATCH_THIS", te.getId());
    }

}
