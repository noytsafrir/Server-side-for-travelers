package superapp.data;

import java.util.Objects;

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
	
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof UserPrimaryKeyId)) {
            return false;
        }
        
        UserPrimaryKeyId other = (UserPrimaryKeyId) o;
        return Objects.equals(this.superapp, other.getSuperapp()) &&
               Objects.equals(this.email, other.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.superapp, this.email);
    }

}
