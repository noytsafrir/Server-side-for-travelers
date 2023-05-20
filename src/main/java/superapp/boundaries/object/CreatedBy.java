package superapp.boundaries.object;

import java.util.Objects;

import superapp.boundaries.user.UserId;

public class CreatedBy {
	private UserId userId;
	
	public CreatedBy() {}

	public CreatedBy(UserId userId) {
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
	
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof UserId)) {
            return false;
        }
        
        CreatedBy other = (CreatedBy) o;
        return Objects.equals(this.userId, other.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userId);
    }
}
