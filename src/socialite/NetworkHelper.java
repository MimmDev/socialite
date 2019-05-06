package socialite;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

// Class to help with the creation of a small-world network
public class NetworkHelper {
	// Method to construct small-world network
	public static void constructSmallWorldNetwork(User[] userList, Network<Object> network, int degree, double beta) {
		// For each user, connect it to other users in a ring lattice formation
		for (int i = 0; i < userList.length; i++) {
			
			for (int j = 1; j <= degree/2; j++) {
				int index; 
				
				// If probability is met, randomly rewire the edge (small-world beta)
				if (RandomHelper.nextDouble() < beta) {
					index = RandomHelper.nextIntFromTo(0, userList.length - 1);
				} else {
					index = i + j;
					
					if (index >= userList.length) {
						index -= userList.length;
					}
				}

				// Create the desired edge between the two consumers
				network.addEdge(userList[i], userList[index]);			
			}
		}
	}
}
