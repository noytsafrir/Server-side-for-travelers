package superapp.boundaries.user;

public class UserAction {
	private UserId userId;
	
	public UserAction() {}

	public UserAction(UserId userId) {
		super();
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "CreatedBy [userId=" + userId + "]";
	}
	

}
