package superapp.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "Users")
public class UserEntity {
	
	@Id
	private String superapp;
	@Id
	private String name;
	
	private String role;
	private String username;
	private String avatar;

	public UserEntity() {}
	
	public String getSuperapp() {
		return superapp;
	}
	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return "UserEntity [superapp=" + superapp + ", name=" + name + ", role=" + role + ", username=" + username
				+ ", avatar=" + avatar + "]";
	}
	
}


