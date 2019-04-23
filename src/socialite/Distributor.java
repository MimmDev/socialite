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
		
		this.bias = bias;
		this.authority = authority;
		this.postProbability = postProbability;
		this.fakeProbability = fakeProbability;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void run() {
		// Probability of creating a post
		if (RandomHelper.nextDouble() <= this.postProbability) {
			int postID = this.createPost();	
			this.sharePost(postID);
		}
	}
	
	public int createPost() {
		boolean fakePost = false;
		if (RandomHelper.nextDouble() <= this.fakeProbability) {
			fakePost = true;
		}
		
		// TODO: Get num iterations for life
		//this code is silly haha I ruined your code for you :)
		//get ownd stupid dumb noob haha im beter then u
		Post newPost = new Post(fakePost, this.bias, this.authority, 1);
		
		this.getDatabase().addPost(newPost);
		
		return this.getDatabase().size();
	}
	
	public void sharePost(int postID) {
		ArrayList<User> neighbours = this.getNeighbours();
		for (int i = 0; i < neighbours.size(); i++) {
			neighbours.get(i).receivePost(1);
		}
	}
	
	public void receivePost(int postID) {
		// Do nothing
	}
	
	public double getPopularity() {
		return this.popularity;
	}
	
}
