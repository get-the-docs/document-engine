package net.videki.templateutils.template.core.service;

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

import org.junit.Assert;
import org.junit.Test;

public class TemplateRegistryTest {

    @Test
    public void templateServiceInstanceTest() {
        final TemplateService ts = TemplateServiceRegistry.getInstance();

        Assert.assertNotNull(ts);
    }

    @Test
    public void templateServiceInstanceIsSingletonTest() {
        final TemplateService ts1 = TemplateServiceRegistry.getInstance();
        final TemplateService ts2 = TemplateServiceRegistry.getInstance();

        Assert.assertEquals(ts1, ts2);
    }

    @Test
    public void templateServiceSetInstanceTest() {
        final TemplateService ts1 = TemplateServiceRegistry.getInstance();
        final var registry = new TemplateServiceRegistry();
        registry.setTemplateService(null);
        final TemplateService ts2 = TemplateServiceRegistry.getInstance();

        Assert.assertNotEquals(ts1, ts2);
    }

    @Test
    public void templateServiceCheckInstanceReinitTest() {
        final var registry = new TemplateServiceRegistry();
        registry.setTemplateService(null);
        final TemplateService ts2 = TemplateServiceRegistry.getInstance();

        Assert.assertNotNull(ts2);
    }
}
