package socialite;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class SocialiteBuilder implements ContextBuilder<Object> {
	public Context<Object> build(Context<Object> context) {
		context.setId("socialite");
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("social network", context, false);
		Network<Object> network = netBuilder.buildNetwork();
		
		Post[] postList = new Post[10];
		for (int i = 0; i < 10; i++) {
			Post post = new Post(true);
			postList[i] = post;
		}
		
		Queue<Post> postQueue = new LinkedList<Post>();
		Collections.addAll(postQueue, postList);
		
		User[] userList = new User[10];
		for (int i = 0; i < userList.length; i++) {
			userList[i] = new User(network, new LinkedList<Post>(postQueue));
			context.add(userList[i]);
		}
		
		userList[0].receivePost(new Post(false));
		userList[1].receivePost(new Post(false));
		userList[2].receivePost(new Post(false));
		userList[3].receivePost(new Post(false));
		userList[4].receivePost(new Post(false));
		
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
