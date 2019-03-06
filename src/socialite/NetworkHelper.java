package socialite;

import repast.simphony.space.graph.Network;

public class NetworkHelper {
	public static void constructRingLattice(User[] userList, Network<Object> network, int degree) {
		for (int i = 0; i < userList.length; i++) {
			System.out.println("Constructing edges for user: " + i);
			for (int j = 0; j < degree+1; j++) {
				int index = i + (j - degree/2);
				if (index > userList.length - 1) {
					index -= userList.length - 1;
				} else if (index < 0) {
					index += userList.length;
				}
												
				if (index != i) {
					network.addEdge(userList[i], userList[index]);
				}
			}
		}
	}
}
