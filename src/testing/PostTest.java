package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import socialite.*;

public class PostTest {
	@Test
	public void postCreation() {
		Post testPost = new Post(false, 0.5, 0.7, 24);
		
		assertEquals(testPost.isLegitimate(), false);
		assertEquals(testPost.getBias(), 0.5);
		assertEquals(testPost.getAuthority(), 0.7);
		assertEquals(testPost.getTimeCreated(), 24);
	}
	
	@Test
	public void ageCalculation() {
		Post testPost = new Post(false, 0.0, 0.0, 52);
		
		assertEquals(testPost.getAge(100), 48);
	}
}
