package socialite;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

// The Distributor agent
public class Distributor extends User {
	private double popularity; // Between 0 and 1, determines what percentage of the population it will be connected to
	private double bias; // Representation of political bias between -1 and 1 (posts created by the distributor will have this bias value)
	private double authority; // Representation of a distributor's authority (posts created by the distributor will have this authority value)
	private double postProbability; // Probability of creating a post each iteration
	private double fakeProbability; // Probability of the posts it creates to contain fake news
	
	public Distributor(Network<Object> network, Database database, double popularity, double bias, double authority, double postProbability, double fakeProbability) {
		super(network, database);
		
		this.popularity = popularity;
		this.bias = bias;
		this.authority = authority;
		this.postProbability = postProbability;
		this.fakeProbability = fakeProbability;
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 2.0)
	public void run() {
		// Probability of creating a post
		if (RandomHelper.nextDouble() <= this.postProbability) {
			// Create new post and share it with all connected consumers
			int postID = this.createPost(ContextHelper.getCurrentIteration());	
			this.sharePost(postID);
		}
	}
	
	// Method to create a new post based on the distributor's attributes
	public int createPost(int currentIteration) {
		// Create fake/legitimate post based on probability
		boolean legitimatePost = true;
		if (RandomHelper.nextDouble() <= this.fakeProbability) {
			legitimatePost = false;
		}
		
		// Create new post
		Post newPost = new Post(legitimatePost, this.bias, this.authority, currentIteration);
		
		// Add new post to database
		this.getDatabase().addPost(newPost);
		
		// Return new post's database index
		return this.getDatabase().size() - 1;
	}
	
	// Share post with all connected consumers
	public void sharePost(int postID) {
		ArrayList<User> neighbours = this.getNeighbours();
		for (int i = 0; i < neighbours.size(); i++) {
			neighbours.get(i).receivePost(postID);
		}
	}
	
	public void receivePost(int postID) {
		// Distributor doesn't act on posts which it receives via the network
	}
	
	public double getPopularity() {
		return this.popularity;
	}
	
	public double getBias() {
		return this.bias;
	}
	
	public double getAuthority() {
		return this.authority;
	}
	
	public double getPostProbability() {
		return this.postProbability;
	}
	
	public double getFakeProbability() {
		return this.fakeProbability;
	}
}
