package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.space.graph.Network;
import socialite.*;

public class RankingTest {
	@Test
	public void ageEvaluation() {
		// Completely new Post should receive a maximum age score
		assertEquals(Ranking.evaluateAge(0), 1.0);
		
		// Boundary case where Post is the oldest allowed age
		assertEquals(Ranking.evaluateAge(672), 0.0);
		
		// Any Posts older than 672 should also receive a score of 0
		assertEquals(Ranking.evaluateAge(999), 0.0);
		
		assertEquals(Ranking.evaluateAge(336), 0.5);
	}
	
	@Test
	public void authorityEvaluation() {
		assertEquals(Ranking.evaluateAuthority(1.0), 1.0);
		assertEquals(Ranking.evaluateAuthority(0.5), 0.5);
		assertEquals(Ranking.evaluateAuthority(0.0), 0.0);
	}
	
	@Test
	public void rankCalculation() {
		Post highAuthorityOld = new Post(false, 0.0, 1.0, 1);
		Post highAuthorityYoung = new Post(false, 0.0, 1.0, 672);
		Post lowAuthorityOld = new Post(false, 0.0, 0.0, 1);
		Post lowAuthorityYoung = new Post(false, 0.0, 0.0, 672);
		Post medium = new Post(false, 0.0, 0.5, 336);
		
		final int CURRENT_ITERATION = 672;
		
		assertEquals(Ranking.calculateRecommendation(highAuthorityOld, CURRENT_ITERATION), 1.0);
		assertEquals(Ranking.calculateRecommendation(highAuthorityYoung, CURRENT_ITERATION), 2.0);
		assertEquals(Ranking.calculateRecommendation(lowAuthorityOld, CURRENT_ITERATION), 0.0);
		assertEquals(Ranking.calculateRecommendation(lowAuthorityYoung, CURRENT_ITERATION), 1.0);
		assertEquals(Ranking.calculateRecommendation(medium, CURRENT_ITERATION), 1.0);
	}
	
	@Test
	public void consumerInventorySorting() {
		Post rank1 = new Post(false, 0.0, 1.0, 672);
		Post rank2 = new Post(false, 0.0, 1.0, 600);
		Post rank3 = new Post(false, 0.0, 0.9, 600);
		Post rank4 = new Post(false, 0.0, 0.2, 40);
		Post rank5 = new Post(false, 0.0, 0.0, 1);
		
		final int CURRENT_ITERATION = 672;
		
		Database database = new Database();
		database.addPost(rank1);
		database.addPost(rank2);
		database.addPost(rank3);
		database.addPost(rank4);
		database.addPost(rank5);
		
		database.refreshRanks(CURRENT_ITERATION);
		
		Consumer consumer = new Consumer(null, database, 0.0, false, 0.00191, 0.583);
		consumer.receivePost(4);
		consumer.receivePost(2);
		consumer.receivePost(1);
		consumer.receivePost(3);
		consumer.receivePost(0);
				
		int[] feed = consumer.checkFeed();

		assertEquals(feed[0], 0);
		assertEquals(feed[1], 1);
		assertEquals(feed[2], 2);
		assertEquals(feed[3], 3);
		assertEquals(feed[4], 4);
	}
	
	@Test
	public void databaseRefresh() {
		Post highAuthorityOld = new Post(false, 0.0, 1.0, 1);
		Post highAuthorityYoung = new Post(false, 0.0, 1.0, 672);
		Post lowAuthorityOld = new Post(false, 0.0, 0.0, 1);
		Post lowAuthorityYoung = new Post(false, 0.0, 0.0, 672);
		Post medium = new Post(false, 0.0, 0.5, 336);
		
		final int CURRENT_ITERATION = 672;
		
		Database database = new Database();
		database.addPost(highAuthorityOld);
		database.addPost(highAuthorityYoung);
		database.addPost(lowAuthorityOld);
		database.addPost(lowAuthorityYoung);
		database.addPost(medium);
		
		database.refreshRanks(CURRENT_ITERATION);
		
		assertEquals(database.getPost(0).getRank(), 1.0);
		assertEquals(database.getPost(1).getRank(), 2.0);
		assertEquals(database.getPost(2).getRank(), 0.0);
		assertEquals(database.getPost(3).getRank(), 1.0);
		assertEquals(database.getPost(4).getRank(), 1.0);
	}
	
	@Test
	public void rankingComparator() {
		Database database = new Database();
		
		Post leftBiasYoung = new Post(false, -1.0, 1.0, 672);
		Post rightBiasOld = new Post(false, 1.0, 1.0, 1);
		
		database.addPost(leftBiasYoung);
		database.addPost(rightBiasOld);
		
		database.refreshRanks(672);
		
		RankingComparator testComparator = new RankingComparator(database);

		assertEquals(testComparator.compare(0, 0), 0);
		assertEquals(testComparator.compare(0, 1), -1);
		assertEquals(testComparator.compare(1, 0), 1);
	}
}
