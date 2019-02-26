package socialite;

import java.util.ArrayList;
import java.util.Queue;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class User {
	private Network<Object> network;
	private Queue<Post> inventory;
	private boolean infected;
	
	public User(Network<Object> network, Queue<Post> inventory) {
		this.network = network;
		this.inventory = inventory;
		this.infected = false;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void run() {
		this.share(this.check());
	}
	
	public Post[] check() {
		int numPosts;
		if (this.inventory.size() < 10) {
			numPosts = this.inventory.size();
		} else {
			numPosts = 10;
		}
		
		System.out.println(this.inventory.size());
				
		Post[] topPosts = new Post[numPosts];
		for (int i = 0; i < topPosts.length; i++) {
			topPosts[i] = this.inventory.poll();
			
			
			if (!topPosts[i].isLegitimate()) {
				this.infected = true;
			}
		}
		
		return topPosts;
	}
	
	public void share(Post[] postList) {
		ArrayList<User> userList = new ArrayList<User>();
		Iterable<Object> neighbours = network.getAdjacent(this);
		neighbours.forEach(neighbour -> userList.add((User)neighbour));
		
		for (int i = 0; i < postList.length; i++) {
			if (RandomHelper.nextDouble() > 0.0191) {
				for (int y = 0; y < userList.size(); y++) {
						// TODO: If double > probability, share to ALL users not to one!
						userList.get(y).receivePost(postList[i]);
				}
			}
		}
	}
	
	public void receivePost(Post post) {
		this.inventory.add(post);
	}
	
	public boolean isInfected() {
		return this.infected;
	}
	
	public boolean isSusceptible() {
		return !this.infected;
	}
}
