package superapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.user.NewUserBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;
import superapp.data.UserRole;
import superapp.logic.UsersServiceNew;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseControllerTest {

	protected String baseUrl;
	protected String adminUrl;
	protected String userUrl = this.baseUrl + "/users";
	protected String loginUrl;

	protected int port;
	protected RestTemplate restTemplate;
	protected String superAppName;
	protected UserBoundary userAdmin;
	protected UserBoundary userSuperapp;
	protected UserBoundary userMiniapp;
	protected UsersServiceNew users;

	
	@PostConstruct
	public void init(){
		this.restTemplate = new RestTemplate();
		this.baseUrl = "http://localhost:" + this.port + "/superapp/";
		this.adminUrl = this.baseUrl + "admin/";
		this.userUrl = this.baseUrl + "/users";
		this.loginUrl = this.userUrl + "/login";
		loginUsers();
		deleteAllUsers();
		setUsers();
	}	
	
	@Autowired
	public void setUsers(UsersServiceNew users) {
		this.users = users;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@Value("${spring.application.name:defaultValue}")
	public void setSuperAppName(String superAppName) {
		this.superAppName = superAppName;
	}
	
	public void loginUsers () {
		this.userAdmin = users.login(this.superAppName, "admin@test.com");
		this.userSuperapp = users.login(this.superAppName, "superapp@test.com");
		this.userMiniapp = users.login(this.superAppName, "miniapp@test.com");
	}
	
	public void deleteAllUsers() {
		users.deleteAllUsers(userAdmin.getUserId().getSuperapp(), userAdmin.getUserId().getEmail());
	}
	
	public void setUsers() {
		setUserAdmin();
		setUserSuperapp();
		setUserMiniapp();
	}
	

	public void setUserAdmin(){
		UserId id = new UserId(this.superAppName, "admin@test.com");
		this.userAdmin =  new UserBoundary(id, UserRole.ADMIN.toString(), "admin", "adminAvatar");
		this.userAdmin = users.createUser(userAdmin);
	}
	
	public void setUserSuperapp(){
		UserId id = new UserId(this.superAppName, "superapp@test.com");
		this.userSuperapp =  new UserBoundary(id, UserRole.SUPERAPP_USER.toString(), "superappUser", "superappAvatar");
		this.userSuperapp = users.createUser(userSuperapp);
	}

	
	public void setUserMiniapp(){
		UserId id = new UserId(this.superAppName, "miniapp@test.com");
		this.userMiniapp =  new UserBoundary(id, UserRole.MINIAPP_USER.toString(), "miniappUser", "miniappAvatar");
		this.userMiniapp = users.createUser(userMiniapp);
	}

	public UserBoundary createUser(UserRole role) throws Exception {
		NewUserBoundary newUser = new NewUserBoundary("user@test.com", role.toString(), "username", "avatar");
		return this.restTemplate.postForObject(this.userUrl, newUser, UserBoundary.class);
	}

	public UserBoundary loginUser(String email) throws Exception {
		return this.restTemplate.getForObject(
				this.loginUrl + "/" + this.superAppName + "/" + email,
				UserBoundary.class);
	}
	
	@Test
	public final void contextLoads() {}

}
