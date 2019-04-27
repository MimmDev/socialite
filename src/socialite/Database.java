package socialite;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Database {
	private ArrayList<Post> postList;
	
	public Database(ArrayList<Post> postList) {
		this.postList = postList;
	}
	
	public Database() {
		this(new ArrayList<Post>());
	}
	
	
	@ScheduledMethod(start = 1, interval = 1, priority = 3.0)
	public void run() {
		for (int i=0; i<this.postList.size(); i++) {
			Post currentPost = this.postList.get(i);
			currentPost.setRank(Ranking.calculateRecommendation(currentPost));
		}
	}

	public Post getPost(int index) {
		return this.postList.get(index);
	}
	
	public void addPost(Post post) {
		this.postList.add(post);
	}
	
	public int size() {
		return this.postList.size();
	}
}
