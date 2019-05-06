package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import socialite.Consumer;

public class ConsumerTest {
	@Test
	public void consumerCreation() {
		Consumer consumer = new Consumer(null, null, -1.0, false, 0.00191, 0.583, false);
		
		assertEquals(consumer.getBias(), -1.0);
		assertEquals(consumer.isFactChecker(), false);
		assertEquals(consumer.isSusceptible(), true);
		assertEquals(consumer.isInfected(), false);
		assertEquals(consumer.getShareProbability(), 0.00191);
		assertEquals(consumer.getCheckProbability(), 0.583);
	}
}
