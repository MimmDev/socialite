package socialite;

public class Post {
	private boolean legitimate;
	private double bias;
	private double authority;
	private int timeCreated;
	private double rank;
	private boolean blacklisted;
	
	public Post(boolean legitimate, double bias, double authority, int timeCreated) {
		this.legitimate = legitimate;
		this.bias = bias;
		this.authority = authority;
		this.timeCreated = timeCreated;
		this.blacklisted = false;
	}
	
	public boolean isLegitimate() {
		return this.legitimate;
	}
	
	public double getBias() {
		return this.bias;
	}
	
	public double getAuthority() {
		return this.authority;
	}
	
	public int getTimeCreated() {
		return this.timeCreated;
	}
	
	public int getAge(int currentIteration) {
		return currentIteration - timeCreated;
	}
	
	public double getRank() {
		return this.rank;
	}
	
	public void setRank(double rank) {
		this.rank = rank;
	}
	
	public void setBlacklisted(boolean blacklisted) {
		this.blacklisted = blacklisted;
	}
	
	public boolean isBlacklisted() {
		return this.blacklisted;
	}
}
