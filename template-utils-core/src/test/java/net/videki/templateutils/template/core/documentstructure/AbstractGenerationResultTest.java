package net.videki.templateutils.template.core.documentstructure;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static org.junit.Assert.fail;

public class AbstractGenerationResultTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGenerationResultTest.class);

    @Test
    public void noArgConstructorShouldReturnRandomTranId() {
        try {
            final var gr = new GenerationResult(null);
            final var gr2 = new GenerationResult(null);

            Assert.assertNotEquals(gr.getTransactionId(), gr2.getTransactionId());
        } catch (final Exception e) {
            fail();
        }
    }

    @Test
    public void nullResultsShouldReturnEmpty() {
        try {
            final var gr = new GenerationResult(null);

            gr.setTransactionId("TRAN_ID");
            gr.setGenerationStartTime(Instant.now());
            gr.setGenerationEndTime(Instant.now());

            Assert.assertEquals(0, gr.getResults().size());
        } catch (final Exception e) {
            fail();
        }
    }

    @Test
    public void abstractAncestorShouldReturnEmpty() {
        try {
            final AbstractGenerationResult gr = new GenerationResult(null);

            gr.setTransactionId("TRAN_ID");
            gr.setGenerationStartTime(Instant.now());
            gr.setGenerationEndTime(Instant.now());

            LOGGER.debug("Generation result: {}", gr.toString());

            Assert.assertNotNull(gr.getGenerationEndTime());
        } catch (final Exception e) {
            fail();
        }
    }


}
