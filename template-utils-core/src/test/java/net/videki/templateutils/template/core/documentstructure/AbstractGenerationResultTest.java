package net.videki.templateutils.template.core.documentstructure;

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
