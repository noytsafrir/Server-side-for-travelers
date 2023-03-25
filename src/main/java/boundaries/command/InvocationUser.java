package boundaries.command;

import boundaries.user.UserId;

public class InvocationUser {
	private UserId userId;
	
	public InvocationUser() {}

	public InvocationUser(UserId userId) {
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
