package socialite;

import java.util.ArrayList;

import repast.simphony.space.graph.Network;

public abstract class User {
	private Network<Object> network;
	private Database database;
	private ArrayList<User> neighbourList;
	
	// Define User with references to the network and database
	public User(Network<Object> network, Database database) {
		this.network = network;
		this.database = database;
	}
	
	// Cache neighbours in faster & more accessible format (only call this after network has been created)
	public void init() {
		neighbourList = new ArrayList<User>();
		Iterable<Object> neighbours = this.network.getAdjacent(this);
		neighbours.forEach(neighbour -> neighbourList.add((User)neighbour));
	}
	
	// Method intended to run every X timesteps (customised using Java annotations)
	public abstract void run();
	
	// Method serving as an event handler when a post is received from a neighbour
	public abstract void receivePost(int postID);
	
	// Method serving as an event handler when a post is shared with neighbours
	public abstract void sharePost(int postID);
	
	// Get reference to database
	public Database getDatabase() {
		return this.database;
	}
	
	// Get reference to network
	public Network<Object> getNetwork() {
		return this.network;
	}
	
	// Get reference to user's neighbours
	public ArrayList<User> getNeighbours() {
		return this.neighbourList;
	}
}
