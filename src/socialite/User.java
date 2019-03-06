package socialite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class User extends Member {
	private Queue<Integer> inventory;
	private boolean infected;
	
	public User(Network<Object> network, Database database) {
		super(network, database);
		
		this.inventory = new LinkedList<Integer>();
		this.infected = false;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void run() {
		this.share(this.check());
	}
	
	public int[] check() {
		int numPosts;
		if (this.inventory.size() < 10) {
			numPosts = this.inventory.size();
		} else {
			numPosts = 10;
		}
						
		int[] topPosts = new int[numPosts];
		for (int i = 0; i < topPosts.length; i++) {
			topPosts[i] = this.inventory.poll();
						
			if (!this.getDatabase().getPost(topPosts[i]).isLegitimate()) {
				this.infected = true;
			}
		}
		
		return topPosts;
	}
	
	public void share(int[] postList) {
		ArrayList<Member> userList = new ArrayList<Member>();
		Iterable<Object> neighbours = this.getNetwork().getAdjacent(this);
		neighbours.forEach(neighbour -> userList.add((Member)neighbour));
		
		for (int i = 0; i < postList.length; i++) {
			if (RandomHelper.nextDouble() <= 0.0191) {
				for (int y = 0; y < userList.size(); y++) {
					userList.get(y).receivePost(postList[i]);
				}
			}
		}
	}
	
	public void receivePost(int postID) {
		if (!this.inventory.contains(postID)) {
			this.inventory.add(postID);
		}
	}
	
	public boolean isInfected() {
		return this.infected;
	}
	
	public boolean isSusceptible() {
		return !this.infected;
	}
}
