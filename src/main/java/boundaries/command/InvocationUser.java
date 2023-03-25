package boundaries.command;

import boundaries.user.UserId;

public class InvocationUser {

	private UserId userID;
	
	public InvocationUser() {}

	public InvocationUser(UserId userID) {
		super();
		this.userID = userID;
	}

	public UserId getUserID() {
		return userID;
	}

	public void setUserID(UserId userID) {
		this.userID = userID;
	}

	@Override
	public String toString() {
		return "InvocationUser [userID=" + userID + "]";
	}
	
}
