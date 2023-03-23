package Boundary.Objects;

import Boundary.User.UserId;

public class CreatedBy {
	private UserId userId;
	
	public CreatedBy() {}

	public CreatedBy(UserId userId) {
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
