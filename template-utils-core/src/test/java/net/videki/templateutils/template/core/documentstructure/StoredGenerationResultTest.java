package net.videki.templateutils.template.core.documentstructure;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static org.junit.Assert.fail;

public class StoredGenerationResultTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoredGenerationResultTest.class);

    @Test
    public void nullResultsShouldReturnEmpty() {
        try {
            final var gr = new StoredGenerationResult(null);

            gr.setTransactionId("TRAN_ID");
            gr.setGenerationStartTime(Instant.now());
            gr.setGenerationEndTime(Instant.now());

            Assert.assertEquals(0, gr.getResults().size());
        } catch (final Exception e) {
            fail();
        }
    }

    @Test
    public void nullResultsShouldReturnRandomTranId() {
        try {
            final var gr = new StoredGenerationResult(null);
            final var gr2 = new StoredGenerationResult(null);

            Assert.assertNotNull(gr.getTransactionId());
            Assert.assertNotEquals(gr.getTransactionId(), gr2.getTransactionId());
        } catch (final Exception e) {
            fail();
        }
    }

    @Test
    public void toStringShouldDumpMembers() {
        try {
            final StoredGenerationResult gr = new StoredGenerationResult(null);

            gr.setTransactionId("TRAN_ID");
            gr.setGenerationStartTime(Instant.now());
            gr.setGenerationEndTime(Instant.now());

            LOGGER.debug("Generation result: {}", gr.toString());

            Assert.assertTrue(gr.toString().contains("generationEndTime="));
        } catch (final Exception e) {
            fail();
        }
    }


}
