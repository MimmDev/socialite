package socialite;

import java.util.Comparator;

public class RankingComparator implements Comparator {
	private Database database;
	
	public RankingComparator(Database database) {
		this.database = database;
	}
	
	@Override
	public int compare(Object object1, Object object2) {
		Post post1 = this.database.getPost((int)object1);
		Post post2 = this.database.getPost((int)object2);
		
		if (post1.getRank() == post2.getRank()) {
			return 0;
		} else if (post1.getRank() > post2.getRank()) {
			return -1;
		} else {
			return 1;
		}
	}
}
