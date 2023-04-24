package superapp.data;


import org.springframework.data.mongodb.core.mapping.Document;

//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.IdClass;
//import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;


@Document(collection = "Users")
public class UserEntity {
	@Id
	private UserPrimaryKeyId userId;
	
	private String role;
	private String username;
	private String avatar;

	public UserEntity() {}
	
	
	public UserPrimaryKeyId getUserId() {
		return userId;
	}

	public void setUserId(UserPrimaryKeyId userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", role=" + role + ", username=" + username + ", avatar=" + avatar
				+ "]";
	}
}


