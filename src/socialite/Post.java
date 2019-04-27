package socialite;

public class Post {
	private boolean legitimate;
	private double bias;
	private double authority;
	private int timeCreated;
	private double rank;
	
	public Post(boolean legitimate, double bias, double authority, int timeCreated) {
		this.legitimate = legitimate;
		this.bias = bias;
		this.authority = authority;
		this.timeCreated = timeCreated;
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
	
	public int getAge(int currentIteration) {
		return currentIteration - timeCreated;
	}
	
	public double getRank() {
		return this.rank;
	}
	
	public void setRank(double rank) {
		this.rank = rank;
	}
}
