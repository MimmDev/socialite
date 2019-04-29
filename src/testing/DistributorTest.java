package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import socialite.Database;
import socialite.Distributor;
import socialite.Post;

public class DistributorTest {
	@Test
	public void distributorCreation() {
		Distributor testDistributor = new Distributor(null, null, 0.6, -0.3, 0.1, 0.9, 0.01);
		
		assertEquals(testDistributor.getPopularity(), 0.6);
		assertEquals(testDistributor.getBias(), -0.3);
		assertEquals(testDistributor.getAuthority(), 0.1);
		assertEquals(testDistributor.getPostProbability(), 0.9);
		assertEquals(testDistributor.getFakeProbability(), 0.01);
	}
	
	@Test
	public void postCreation() {
		Database testDatabase = new Database();
		
		Distributor testDistributor = new Distributor(null, testDatabase, 0.6, -0.3, 0.1, 0.9, 1.0);
		int postIndex = testDistributor.createPost(10);
		Post storedPost = testDatabase.getPost(postIndex);
		
		assertEquals(storedPost.getBias(), -0.3);
		assertEquals(storedPost.getAuthority(), 0.1);
		assertEquals(storedPost.isLegitimate(), false);
		assertEquals(storedPost.getTimeCreated(), 10);
	}
	
	@Test
	public void postSharing() {
		//assertEquals(0, 1);
	}
}
