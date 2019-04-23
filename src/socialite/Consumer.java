package socialite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Consumer extends User {
	private Queue<Integer> inventory;
	private boolean infected;
	
	// Proposed new variables
	private double reportProbability;
	private double counterProbability;
	private double bias;
	private int age;
	
	// Consumer constants (TODO: set via environment variables)
	private final double CHECK_PROBABILITY = 0.583;
	private final double SHARE_PROBABILITY = 0.0191;
	
	public Consumer(Network<Object> network, Database database) {
		super(network, database);
		
		this.inventory = new LinkedList<Integer>();
		this.infected = false;
		this.bias = RandomHelper.nextDoubleFromTo(-5, 5);
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void run() {
		// Probability for checking their feed each hour
		if (RandomHelper.nextDouble() <= CHECK_PROBABILITY) {
			int[] topPosts = this.checkFeed();
			this.readAndShare(topPosts);
		}
	}
	
	private void readAndShare(int[] topPosts) {
		for (int i=0; i < topPosts.length; i++) {
			if (!this.getDatabase().getPost(topPosts[i]).isLegitimate()) {
				this.infected = true;
			}
			
			// Probability for sharing a post
			if (RandomHelper.nextDouble() <= SHARE_PROBABILITY) {
				this.sharePost(topPosts[i]);
			}
		}
	}
	
	public int[] checkFeed() {
		int numPosts;
		if (this.inventory.size() < 10) {
			numPosts = this.inventory.size();
		} else {
			numPosts = 10;
		}
						
		int[] topPosts = new int[numPosts];
		for (int i = 0; i < topPosts.length; i++) {
			topPosts[i] = this.inventory.poll();		
		}
		
		return topPosts;
	}
	
	public void receivePost(int postID) {
		if (!this.inventory.contains(postID)) {
			this.inventory.add(postID);
		}
	}
	
	public void sharePost(int postID) {
		double baseProbability = RandomHelper.nextDouble();
		double shareProbability = baseProbability;
		
		if (RandomHelper.nextDouble() <= shareProbability) {
			for (int y = 0; y < this.getNeighbours().size(); y++) {
				this.getNeighbours().get(y).receivePost(postID);
			}
		}
	}
	
	// Methods to check a consumer's properties, used for gathering stats in the Repast UI
	public boolean isInfected() {
		return this.infected;
	}
	
	public boolean isSusceptible() {
		return !this.infected;
	}
	
	public double getBias() {
		return this.bias;
	}
}
