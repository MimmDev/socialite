package socialite;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Distributor extends User {
	private double popularity;
	private double bias;
	private double authority;
	private double postProbability;
	private double fakeProbability;
	
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
			int postID = this.createPost(ContextHelper.getCurrentIteration());	
			this.sharePost(postID);
		}
	}
	
	public int createPost(int currentIteration) {
		boolean legitimatePost = true;
		if (RandomHelper.nextDouble() <= this.fakeProbability) {
			legitimatePost = false;
		}
		
		// TODO: Get num iterations for life
		Post newPost = new Post(legitimatePost, this.bias, this.authority, currentIteration);
		
		this.getDatabase().addPost(newPost);
		
		return this.getDatabase().size() - 1;
	}
	
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
