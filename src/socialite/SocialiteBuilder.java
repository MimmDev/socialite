package socialite;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.context.space.graph.WattsBetaSmallWorldGenerator;

public class SocialiteBuilder implements ContextBuilder<Object> {
	private final int DEGREE = 4;
			
	public Context<Object> build(Context<Object> context) {
		context.setId("socialite");
		
		System.out.print("Before init database");
		
		// Initialise database
		Database database = new Database();
		
		// Initialise network for users to be placed in
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("social network", context, false);
		Network<Object> network = netBuilder.buildNetwork();
		
		System.out.print("Before init consumer");
		
		// Create array of users and assign random posts
		Consumer[] consumerList = new Consumer[1000];
		for (int i = 0; i < consumerList.length; i++) {
			System.out.println("Generating consumer: " + i);
			consumerList[i] = new Consumer(network, database);
			context.add(consumerList[i]);
		}
		
		// Generate small-world network
		WattsBetaSmallWorldGenerator<Object> test = new WattsBetaSmallWorldGenerator(0.1, DEGREE, true);
		NetworkHelper.constructRingLattice(consumerList, network, DEGREE);
		
		System.out.println("Generating small world network...");
		network = test.createNetwork(network);
		System.out.println("Small world network ready!");
		
		System.out.print("Before init distributor");
		
		// Generate distributors
		Distributor[] distributorList = new Distributor[2];
		distributorList[0] = new Distributor(network, database, 0.5, 0.0, 0.9, 0.5, 0.001);
		distributorList[1] = new Distributor(network, database, 0.1, -1.0, 0.1, 0.9, 1.0);
	
		for (int i = 0; i < distributorList.length; i++) {
			double popularity = distributorList[i].getPopularity();
			
			for (int y = 0; y < consumerList.length; y++) {
				if (RandomHelper.nextDouble() <= popularity) {
					network.addEdge(distributorList[i], consumerList[y]);
				}
			}
		}
		
		return context;
	}
}
