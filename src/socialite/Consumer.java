package socialite;

import java.util.PriorityQueue;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

// The Consumer agent
public class Consumer extends User {
	private PriorityQueue<Integer> inventory;
	
	// Boolean holding the infected state of the Consumer
	private boolean infected;
	
	private boolean isFactChecker; // If set to true, will ignore fake news
	private double shareProbability; // Probability that any given post will be shared
	private double checkProbability; // Probability that the user will check their feed each timestep
	private double bias; // Political bias between -1 and 1
	private boolean isYoung; // If set to true, will ignore high-authority news
	
	// Constructor method
	public Consumer(Network<Object> network, Database database, double bias, boolean isFactChecker, double shareProbability, double checkProbability, boolean isYoung) {
		super(network, database);
		
		this.bias = bias;
		this.isFactChecker = isFactChecker;
		this.shareProbability = shareProbability;
		this.checkProbability = checkProbability;
		this.isYoung = isYoung;
		
		// Inventory is a PriorityQueue sorted by a Post's rank, the sorting procedure for which is found in RankingComparator
		this.inventory = new PriorityQueue<Integer>(1, new RankingComparator(database));
		
		// All agents are initialised as susceptible
		this.infected = false;
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 1.0)
	public void run() {
		// Probability for checking their feed each hour
		if (RandomHelper.nextDouble() <= this.checkProbability) {
			// Get top 10 posts (or less if the user's inventory doesn't contain that many)
			int[] topPosts = this.checkFeed();
			
			// Call method to determine whether to read/believe/share each post
			this.readAndShare(topPosts);
		}
	}
	
	// Method which determines whether a user's top 10 posts will be read/believed/shared
	private void readAndShare(int[] topPosts) {
		for (int i=0; i < topPosts.length; i++) {
			// Get each post from the database
			Post currentPost = this.getDatabase().getPost(topPosts[i]);
			
			// If the user is a fact checker, ignore all illegitimate (fake) posts
			if (this.isFactChecker && !currentPost.isLegitimate()) {
				// If fact checker reporting condition is active, report the post else ignore it completely
				if (RunEnvironment.getInstance().getParameters().getBoolean("FactCheckersWillReport")) {
					currentPost.setBlacklisted(true);
				}
				
			// If a post is blacklisted, ignore it
			} else if (!currentPost.isBlacklisted()) {
				// If age is set to influence trust, and the individual is young, and the post's authority is high, ignore the post
				if (RunEnvironment.getInstance().getParameters().getBoolean("AgeImpactsTrust") && this.isYoung && currentPost.getAuthority() >= 0.7) {
					// Do nothing
				} else {
					// Increase/decrease bias relative to the post's bias
					if (currentPost.getBias() > this.bias) {
						this.bias += 0.01;
					} else if (currentPost.getBias() < this.bias) {
						this.bias -= 0.01;
					}
					
					// Set consumer to infected if fake news is encountered
					if (!currentPost.isLegitimate()) {
						this.infected = true;
					}
					
					// Probability for sharing a post
					if (RandomHelper.nextDouble() <= this.shareProbability) {
						// Call method to share posts with connected consumers
						this.sharePost(topPosts[i]);
					}
				}
			}
		}
	}
	
	// Get the top 10 items from the consumer's inventory
	public int[] checkFeed() {
		// If inventory has less than 10 items, return all remaining items
		int numPosts;
		if (this.inventory.size() < 10) {
			numPosts = this.inventory.size();
		} else {
			numPosts = 10;
		}
						
		// De-queue the desired number of posts and return them
		int[] topPosts = new int[numPosts];
		for (int i = 0; i < topPosts.length; i++) {
			topPosts[i] = this.inventory.poll();		
		}
		
		return topPosts;
	}
	
	// Method to handle receiving of posts
	public void receivePost(int postID) {
		// Don't add to inventory if it already exists, else add it
		if (!this.inventory.contains(postID)) {
			this.inventory.add(postID);
		}
	}

	// Method to share a post with others
	public void sharePost(int postID) {	
		// Iterate through each neighbour (connected consumer) and send them the post ID
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
