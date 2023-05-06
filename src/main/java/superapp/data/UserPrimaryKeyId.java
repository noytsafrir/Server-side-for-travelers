package superapp.data;

import superapp.utils.ResourceIdentifier;

public class UserPrimaryKeyId implements ResourceIdentifier{
	private String superapp;
	private String email;
	
	
	public UserPrimaryKeyId() {}
	
	public UserPrimaryKeyId(String superapp, String email) {
		this.superapp = superapp;
		this.email = email;
	}

	
	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	@Override
	public String toString() {
		return "UserPrimaryKeyId [superapp=" + superapp + ", email=" + email + "]";
	}

}
