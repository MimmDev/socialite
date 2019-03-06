package socialite;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Distributor extends Member {
	public Distributor(Network<Object> network, Database database) {
		super(network, database);
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void run() {
		if (RandomHelper.nextDouble() > 0.5) {
			this.publish();	
		}
	}
	
	public void publish() {
		ArrayList<Member> userList = new ArrayList<Member>();
		Iterable<Object> neighbours = this.getNetwork().getAdjacent(this);
		neighbours.forEach(neighbour -> userList.add((Member)neighbour));
		
		for (int i = 0; i < userList.size(); i++) {
			userList.get(i).receivePost(1);
		}
	}
	
	public void receivePost(int postID) {
		// Do nothing
	}
}
