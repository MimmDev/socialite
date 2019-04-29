package socialite;

import java.util.PriorityQueue;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Consumer extends User {
	private PriorityQueue<Integer> inventory;
	private boolean infected;
	
	private boolean isFactChecker;
	private double shareProbability;
	private double checkProbability;
	private double bias;
	private boolean isYoung;
	
	public Consumer(Network<Object> network, Database database, double bias, boolean isFactChecker, double shareProbability, double checkProbability, boolean isYoung) {
		super(network, database);
		
		this.bias = bias;
		this.isFactChecker = isFactChecker;
		this.shareProbability = shareProbability;
		this.checkProbability = checkProbability;
		this.isYoung = isYoung;
		
		this.inventory = new PriorityQueue<Integer>(1, new RankingComparator(database));
		this.infected = false;
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 1.0)
	public void run() {
		// Probability for checking their feed each hour
		if (RandomHelper.nextDouble() <= this.checkProbability) {
			int[] topPosts = this.checkFeed();
			this.readAndShare(topPosts);
		}
	}
	
	private void readAndShare(int[] topPosts) {
		for (int i=0; i < topPosts.length; i++) {
			Post currentPost = this.getDatabase().getPost(topPosts[i]);
			
			if (this.isFactChecker && !currentPost.isLegitimate()) {
				currentPost.setBlacklisted(true);
			} else if (!currentPost.isBlacklisted()) {
				if (RunEnvironment.getInstance().getParameters().getBoolean("AgeImpactsTrust") && this.isYoung && currentPost.getAuthority() >= 0.7) {
					// Ignore post
				} else {
					if (currentPost.getBias() > this.bias) {
						this.bias += 0.01;
					} else if (currentPost.getBias() < this.bias) {
						this.bias -= 0.01;
					}
					
					if (!currentPost.isLegitimate()) {
						this.infected = true;
					}
					
					// Probability for sharing a post
					if (RandomHelper.nextDouble() <= this.shareProbability) {
						this.sharePost(topPosts[i]);
					}
				}
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
		for (int y = 0; y < this.getNeighbours().size(); y++) {
			this.getNeighbours().get(y).receivePost(postID);
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
	
	public double getShareProbability() {
		return this.shareProbability;
	}
	
	public double getCheckProbability() {
		return this.checkProbability;
	}
	
	public boolean isFactChecker() {
		return this.isFactChecker;
	}
}
