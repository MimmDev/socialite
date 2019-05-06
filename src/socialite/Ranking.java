package socialite;

// Ranking class created to determine a post's rank at any given time
public class Ranking {
	// Function returning a double based on a post's age and authority
	public static double calculateRecommendation(Post post, int currentIteration) {
		return evaluateAge(post.getAge(currentIteration)) + evaluateAuthority(post.getAuthority());
	}
	
	// Function returning a double based on a post's age
	public static double evaluateAge(int postAge) {
		// If post age is older than 4 weeks it is no longer relevant, but before then the relevance decreases over time
		if (postAge <= 672) {
			return Math.round(((672 - postAge) / 672.0) * 100) / 100.0;
		} else {
			return 0.0;
		}
	}
	
	// Function returning a double based on a post's authority
	public static double evaluateAuthority(double postAuthority) {
		// Return the post's authority un-modified
		return postAuthority;
	}
}
