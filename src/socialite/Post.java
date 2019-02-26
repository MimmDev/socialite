package socialite;

public class Post {
	private boolean legitimate;
	
	public Post(boolean legitimate) {
		this.legitimate = legitimate;
	}
	
	public boolean isLegitimate() {
		return this.legitimate;
	}
}
