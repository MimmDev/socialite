package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import socialite.Database;
import socialite.Post;

public class DatabaseTest {
	@Test
	public void databaseCreation() {
		Database testDatabase = new Database();
		
		assertEquals(testDatabase.size(), 0);
	}
	
	@Test
	public void postAdding() {
		Database testDatabase = new Database();
		Post newPost = new Post(false, 0.0, 0.0, 1);
		testDatabase.addPost(newPost);
		assertEquals(testDatabase.size(), 1);
	}
	
	@Test
	public void postRetrieval() {
		Database testDatabase = new Database();
		Post newPost = new Post(false, 0.0, 0.0, 1);
		testDatabase.addPost(newPost);
		
		Post retrievedPost = testDatabase.getPost(0);
		assertEquals(retrievedPost.isLegitimate(), false);
		assertEquals(retrievedPost.getBias(), 0.0);
		assertEquals(retrievedPost.getAuthority(), 0.0);
		assertEquals(retrievedPost.getTimeCreated(), 1);
	}
}
