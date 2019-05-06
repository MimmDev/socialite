package socialite;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;

// The database is designed to store posts for easy access by all consumers (akin to a global message board)
public class Database {
	// ArrayList of posts
	private ArrayList<Post> postList;
	
	public Database(ArrayList<Post> postList) {
		this.postList = postList;
	}
	
	public Database() {
		this(new ArrayList<Post>());
	}
	
	// Each iteration, refresh all post ranks
	@ScheduledMethod(start = 1, interval = 1, priority = 3.0)
	public void run() {
		this.refreshRanks(ContextHelper.getCurrentIteration());
	}
	
	// Method to update each post's ranks based on age and authority
	public void refreshRanks(int currentIteration) {
		for (int i=0; i<this.postList.size(); i++) {
			// Get each post and set its rank based on current iteration (goes down over time as the post ages)
			Post currentPost = this.postList.get(i);
			currentPost.setRank(Ranking.calculateRecommendation(currentPost, currentIteration));
		}
	}

	// Method for retrieving post from the database
	public Post getPost(int index) {
		return this.postList.get(index);
	}
	
	// Method for adding post to the database
	public void addPost(Post post) {
		this.postList.add(post);
	}
	
	
	// Method for returning the database's size (used to get the index of a newly added post)
	public int size() {
		return this.postList.size();
	}
}
