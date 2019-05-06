package socialite;

// The Post class is designed to store attributes of the Distributor which created it
public class Post {
	private boolean legitimate; // Whether or not the post contains fake news
	private double bias; // Bias between -1 and 1, inherited from distributor
	private double authority; // Authority between 0 and 1, inherited from distributor
	private int timeCreated; // Time (in iterations) at which it was created
	private double rank; // Rank (re-calculated each iteration based on age and authority
	private boolean blacklisted; // Whether or not the post has been reported
	
	public Post(boolean legitimate, double bias, double authority, int timeCreated) {
		this.legitimate = legitimate;
		this.bias = bias;
		this.authority = authority;
		this.timeCreated = timeCreated;
		this.blacklisted = false;
	}
	
	// Check if the post is legitimate
	public boolean isLegitimate() {
		return this.legitimate;
	}
	
	// Get the post's bias
	public double getBias() {
		return this.bias;
	}
	
	// Get the post's authority
	public double getAuthority() {
		return this.authority;
	}
	
	// Get the time that the post was created
	public int getTimeCreated() {
		return this.timeCreated;
	}
	
	// Calculate the post's age by the current iteration
	public int getAge(int currentIteration) {
		return currentIteration - timeCreated;
	}
	
	// Get the post's rank
	public double getRank() {
		return this.rank;
	}
	
	// Set the post's rank
	public void setRank(double rank) {
		this.rank = rank;
	}
	
	// Method to report the post (or un-report it, but this is not used)
	public void setBlacklisted(boolean blacklisted) {
		this.blacklisted = blacklisted;
	}
	
	// Check whether a post has been reported
	public boolean isBlacklisted() {
		return this.blacklisted;
	}
}
