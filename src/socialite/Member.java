package socialite;

import repast.simphony.space.graph.Network;

public abstract class Member {
	private Network<Object> network;
	private Database database;
	
	public Member(Network<Object> network, Database database) {
		this.network = network;
		this.database = database;
	}
	
	public abstract void run();
	
	public abstract void receivePost(int postID);
	
	public Database getDatabase() {
		return this.database;
	}
	
	public Network<Object> getNetwork() {
		return this.network;
	}
}
