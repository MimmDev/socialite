package socialite;

public class Ranking {
	// Function returning a double based on a post's details combined with a consumer's details
	public static double calculateRecommendation(Post post, int currentIteration) {
		return evaluateAge(post.getAge(currentIteration)) + evaluateAuthority(post.getAuthority()); // TODO: PLUS EVALUATE AUTHORITY
	}
	
	// Function returning a double based on a post's age
	public static double evaluateAge(int postAge) {
		if (postAge <= 672) {
			return Math.round(((672 - postAge) / 672.0) * 100) / 100.0;
		} else {
			return 0.0;
		}
	}
	
	// Function returning a double based on a post's authority
	public static double evaluateAuthority(double postAuthority) {
		return postAuthority;
	}
}
