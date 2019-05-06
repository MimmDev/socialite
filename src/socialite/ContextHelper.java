package socialite;

import repast.simphony.engine.environment.RunEnvironment;

// Class to assist with getting information from the context
public class ContextHelper {
	// Shorthand method for getting the current iteration from the context
	public static int getCurrentIteration() {
		return (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	}
}
