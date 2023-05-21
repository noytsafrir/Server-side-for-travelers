package superapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.user.NewUserBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.data.UserRole;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseControllerTest {

	protected String baseUrl;
	protected String adminUrl;
	protected String userUrl = this.baseUrl + "/users";
	protected String loginUrl;

	protected int port;
	protected RestTemplate restTemplate;
	protected String superAppName;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.baseUrl = "http://localhost:" + this.port + "/superapp/";
		this.adminUrl = this.baseUrl + "admin/";
		this.userUrl = this.baseUrl + "/users";
		this.loginUrl = this.userUrl + "/login";
	}	

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@Value("${spring.application.name:defaultValue}")
	public void setSuperAppName(String superAppName) {
		this.superAppName = superAppName;
	}
	
	public void deleteUsers() throws Exception {
		this.restTemplate.delete(this.adminUrl + "/users");
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
	public final void contextLoads() {
	}

}
