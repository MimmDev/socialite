package socialite;

public class Post {
	private boolean legitimate;
	private double bias;
	private double authority;
	
	public Post(boolean legitimate, double bias, double authority) {
		this.legitimate = legitimate;
		this.bias = -5;
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
}
