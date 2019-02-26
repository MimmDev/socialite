package socialite;

import java.util.ArrayList;

public class Database {
	private ArrayList<Post> postList;
	
	public Database(ArrayList<Post> postList) {
		this.postList = postList;
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
