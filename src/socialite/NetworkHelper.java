package socialite;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

public class NetworkHelper {
	public static void test(User[] userList, Network<Object> network, int degree) {
		RepastEdge edge = network.addEdge(userList[0], userList[1]);
		RepastEdge edge2 = network.addEdge(userList[1], userList[0]);
		network.removeEdge(edge);
		network.removeEdge(edge2);
	}
	
	public static void constructRingLattice(User[] userList, Network<Object> network, int degree, double beta) {
		for (int i = 0; i < userList.length; i++) {
			
			for (int j = 1; j <= degree/2; j++) {
				int index; 
				
				if (RandomHelper.nextDouble() < beta) {
					index = RandomHelper.nextIntFromTo(0, userList.length - 1);
				} else {
					index = i + j;
					
					if (index >= userList.length) {
						index -= userList.length;
					}
				}

				network.addEdge(userList[i], userList[index]);			
			}
		}
	}
	
	public static void constructSmallWorldNetwork(User[] userList, Network<Object> network, int degree) {
		
	}
}
