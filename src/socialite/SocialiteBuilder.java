package socialite;

import java.util.ArrayList;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.context.space.graph.WattsBetaSmallWorldGenerator;

public class SocialiteBuilder implements ContextBuilder<Object> {
	public Context<Object> build(Context<Object> context) {
		context.setId("socialite");
		
		// Initialise network for users to be placed in
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("social network", context, false);
		Network<Object> network = netBuilder.buildNetwork();
		
		// Create database of posts
		ArrayList<Post> postList = new ArrayList<Post>();
		for (int i = 0; i < 1; i++) {
			Post post = new Post(true);
			postList.add(post);
		}
		Database database = new Database(postList);
		database.addPost(new Post(false));
		
		// Create array of users and assign random posts
		User[] userList = new User[1000];
		for (int i = 0; i < userList.length; i++) {
			System.out.println("Generating user: " + i);
			userList[i] = new User(network, database);
			context.add(userList[i]);
		}
		
		final int DEGREE = 4;
		WattsBetaSmallWorldGenerator<Object> test = new WattsBetaSmallWorldGenerator(0.1, DEGREE, true);
		NetworkHelper.constructRingLattice(userList, network, DEGREE);
		
		System.out.println("Generating small world network...");
		network = test.createNetwork(network);
		System.out.println("Small world network ready!");
		
		Distributor distributor = new Distributor(network, database);
		context.add(distributor);
		for (int i = 0; i < 1; i++) {
			//network.addEdge(distributor, userList[RandomHelper.nextIntFromTo(0, userList.length - 1)]);
			network.addEdge(distributor, userList[0]);
		}
		
		return context;
	}
}
