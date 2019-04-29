package socialite;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.context.space.graph.WattsBetaSmallWorldGenerator;

public class SocialiteBuilder implements ContextBuilder<Object> {			
	public Context<Object> build(Context<Object> context) {
		context.setId("socialite");
		
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
			
			boolean isFactChecker = false;
			if (RandomHelper.nextDouble() <= params.getDouble("FactCheckerProbability")) {
				isFactChecker = true;
			}
			
			boolean isYoung = false;
			if (RandomHelper.nextDouble() <= 0.057) {
				isYoung = true;
			}
			
			consumerList[i] = new Consumer(network, database, bias, isFactChecker, params.getDouble("ShareProbability"), params.getDouble("ConsumerCheckProbability"), isYoung);
			context.add(consumerList[i]);
		}
		
		// Generate small-world network
		WattsBetaSmallWorldGenerator<Object> smallWorldGenerator = new WattsBetaSmallWorldGenerator(params.getDouble("SmallWorldBeta"), params.getInteger("SmallWorldDegree"), true);
		NetworkHelper.constructRingLattice(consumerList, network, params.getInteger("SmallWorldDegree"));
	
		System.out.println("Generating small world network...");
		network = smallWorldGenerator.createNetwork(network);
		System.out.println("Small world network ready!");
		
		for (int i = 0; i < consumerList.length; i++) {
			consumerList[i].init();		
		}
		
		Distributor legitimateDistributor = new Distributor(network, database, 1.0, 0.0, 0.5, 0.01, 0.0);
		Distributor fakeNewsDistributor = new Distributor(network, database, 0.1, 1.0, 0.0, 1.0, 1.0);
	
		context.add(legitimateDistributor);
		double popularity = legitimateDistributor.getPopularity();
		
		for (int y = 0; y < consumerList.length; y++) {
			if (RandomHelper.nextDouble() <= popularity) {
				network.addEdge(legitimateDistributor, consumerList[y]);
			}
		}	
		legitimateDistributor.init();
		
		context.add(fakeNewsDistributor);
		for (int i=0; i<1000; i++) {
			network.addEdge(fakeNewsDistributor, consumerList[i]);
		}
		fakeNewsDistributor.init();
		
		return context;
	}
}
