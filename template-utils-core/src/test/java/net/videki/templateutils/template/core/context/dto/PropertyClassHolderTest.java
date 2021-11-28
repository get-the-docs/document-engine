package net.videki.templateutils.template.core.context.dto;

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

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyClassHolderTest {

    @Test(expected = TemplateServiceRuntimeException.class)
    public void buildArbitaryTypeNullNameShouldThrowTemplateServiceRuntimeException() {
        new PropertyClassHolder(null, null);
    }

    @Test(expected = TemplateServiceRuntimeException.class)
    public void buildArbitaryTypeNullTypeShouldThrowTemplateServiceRuntimeException() {
        new PropertyClassHolder("name", null);
    }

    @Test
    public void buildArbitaryTypeOk() {
        new PropertyClassHolder("name", String.class);
    }

    @Test
    public void buildArbitaryTypeWithGeneratedClass() {
        final ClassPool pool = ClassPool.getDefault();

        final CtClass cc;
        try {
            cc = pool.get(this.getClass().getName());

            final var holder = new PropertyClassHolder("name", null, cc, null);

            assertNotNull(holder);
        } catch (final NotFoundException e) {
            fail();
        }
    }

}
