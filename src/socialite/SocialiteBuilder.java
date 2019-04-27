package socialite;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.context.space.graph.WattsBetaSmallWorldGenerator;

public class SocialiteBuilder implements ContextBuilder<Object> {
	private final int DEGREE = 4;
			
	public Context<Object> build(Context<Object> context) {
		context.setId("socialite");
		
		System.out.println(ScheduleParameters.FIRST_PRIORITY);
		System.out.println(ScheduleParameters.LAST_PRIORITY);
		
		// Get environment parameters
		Parameters params = RunEnvironment.getInstance().getParameters();
				
		// Initialise database
		Database database = new Database();
		context.add(database);
		
		// Initialise network for users to be placed in
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("social network", context, false);
		Network<Object> network = netBuilder.buildNetwork();
				
		// Create array of users and assign random posts
		Consumer[] consumerList = new Consumer[params.getInteger("NumConsumers")];
		for (int i = 0; i < consumerList.length; i++) {
			System.out.println("Generating consumer: " + i);
			
			// Construct bias in a way to make clustered users have similar bias
			double bias = (i/(double)consumerList.length * 2) - 1.0;
			
			consumerList[i] = new Consumer(network, database, bias, false, params.getDouble("ShareProbability"), params.getDouble("ConsumerCheckProbability"));
			context.add(consumerList[i]);
		}
		
		// Generate small-world network
		WattsBetaSmallWorldGenerator<Object> test = new WattsBetaSmallWorldGenerator(0.1, DEGREE, true);
		NetworkHelper.constructRingLattice(consumerList, network, DEGREE);
		
		for (int i = 0; i < consumerList.length; i++) {
			consumerList[i].init();		
		}
	
		System.out.println("Generating small world network...");
		network = test.createNetwork(network);
		System.out.println("Small world network ready!");
				
		// Generate distributors
		Distributor[] distributorList = new Distributor[2];
		
		// popularity, bias, authority, postProbability, fakeProbability
		distributorList[0] = new Distributor(network, database, 0.1, 1.0, 0.5, 0.1, 0.0);
		distributorList[1] = new Distributor(network, database, 0.001, -1.0, 0.01, 1, 1.0);
	
		for (int i = 0; i < distributorList.length; i++) {
			context.add(distributorList[i]);
			double popularity = distributorList[i].getPopularity();
			
			for (int y = 0; y < consumerList.length; y++) {
				if (RandomHelper.nextDouble() <= popularity) {
					network.addEdge(distributorList[i], consumerList[y]);
				}
			}
			
			distributorList[i].init();
		}
		
		return context;
	}
}
