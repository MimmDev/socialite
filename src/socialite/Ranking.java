package socialite;

public class Ranking {
	// Function returning a double based on a post's details combined with a consumer's details
	public static double calculateRecommendation(Post post) {
		int currentIteration = ContextHelper.getCurrentIteration();
		
		return evaluateAge(post.getAge(currentIteration)); // TODO: PLUS EVALUATE AUTHORITY
	}
	
	// Function returning a double based on a post's age
	private static double evaluateAge(int postAge) {
		if (postAge <= 672) {
			return (672 - postAge) / 672.0;
		} else {
			return 0.0;
		}
	}
	
	// Function returning a double based on a post's bias as well as a user's bias
	private static double evaluateBias(double postBias, double userBias) {
		double biasDifference = Math.abs(postBias - userBias);
		return (2.0 - biasDifference) / 2.0;
	}
}
