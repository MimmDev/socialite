package socialite;

import java.util.ArrayList;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class SocialiteBuilder implements ContextBuilder<Object> {
	public Context<Object> build(Context<Object> context) {
		context.setId("socialite");
		
		// Create network for users to be placed in
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("social network", context, false);
		Network<Object> network = netBuilder.buildNetwork();
		
		// Create database of posts
		ArrayList<Post> postList = new ArrayList<Post>();
		for (int i = 0; i < 100; i++) {
			Post post = new Post(true);
			postList.add(post);
		}
		Database database = new Database(postList);
		database.addPost(new Post(false));
		
		// Create array of users and assign random posts
		User[] userList = new User[100];
		for (int i = 0; i < userList.length; i++) {
			userList[i] = new User(network, database);
			context.add(userList[i]);

			for (int j = 0; j < 1; j++) {
				userList[i].receivePost(RandomHelper.nextIntFromTo(0, database.size()-1));
			}
		}

		// Add random edges between users
		for (int i = 0; i < userList.length; i++) {
			for (int y = 0; y < userList.length; y++) {
				if (i != y) {
					double randDouble = RandomHelper.nextDouble();
					if (randDouble > 0.99) {
						network.addEdge(userList[i], userList[y]);
					}
				}
			}
		}
		
		return context;
	}
}
