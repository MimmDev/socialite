package socialite;

import repast.simphony.engine.environment.RunEnvironment;

public class ContextHelper {
	public static int getCurrentIteration() {
		return (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	}
}
