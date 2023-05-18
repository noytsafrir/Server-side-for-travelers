package superapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.user.NewUserBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserTests {

	protected String url;
	protected String deleteUrl;
	protected String loginUrl;
	protected int port;
	protected RestTemplate restTemplate;
	protected String superAppName;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + this.port + "/superapp/users";
		this.deleteUrl = "http://localhost:" + this.port + "/superapp/admin/users";
		this.loginUrl = "http://localhost:" + this.port + "/superapp/users/login";
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@AfterEach
	public void tearDown() {
		// cleanup database
		this.restTemplate.delete(this.deleteUrl);
	}

	@Value("${spring.application.name:defaultValue}")
	public void setSuperAppName(String superAppName) {
		this.superAppName = superAppName;
	}

	@Test
	public final void contextLoads() {
	}

	@Test
	public void testCreateValidUser() throws Exception {
		NewUserBoundary newUser = new NewUserBoundary("user@test.com", "ADMIN", "username", "avatar");
		UserBoundary result = this.restTemplate.postForObject(this.url, newUser, UserBoundary.class);

		assertThat(result.getUserId().getEmail().equals(newUser.getEmail())
				&& result.getUsername().equals(newUser.getUsername()) && result.getAvatar().equals(newUser.getAvatar())
				&& result.getRole().equals(newUser.getRole()));
	}

	@Test
	public void testCreateUserWithMissingDetails() {
		NewUserBoundary newUser = new NewUserBoundary(null, null, null, null);
		try {
			restTemplate.postForEntity(url, newUser, UserBoundary.class);
		} catch (HttpClientErrorException ex) {
			assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
		}
	}

	@Test
	public void testCreateUserWithInvalidRole() {
		NewUserBoundary newUser = new NewUserBoundary("usertest.com", "X", "username", "avatar");
		try {
			restTemplate.postForEntity(url, newUser, UserBoundary.class);
		} catch (HttpClientErrorException ex) {
			assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
		}
	}

	@Test
	public void testCreateUserWithInvalidEmail() {
		NewUserBoundary newUser = new NewUserBoundary("usertest.com", "ADMIN", "username", "avatar");

		try {
			restTemplate.postForEntity(url, newUser, UserBoundary.class);
		} catch (HttpClientErrorException ex) {
			assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
		}
	}

	@Test
	public void testLogin() {
		String email = "user@test.com";
		NewUserBoundary newUser = new NewUserBoundary(email, "ADMIN", "username", "avatar");
		UserBoundary user = this.restTemplate.postForObject(this.url, newUser, UserBoundary.class);

		UserBoundary login = this.restTemplate.getForObject(
				this.loginUrl + "/" + user.getUserId().getSuperapp() + "/" + user.getUserId().getEmail(),
				UserBoundary.class);
		assertThat(user.getUserId().getEmail().equals(login.getUserId().getEmail())
				&& user.getUsername().equals(login.getUsername()) && login.getAvatar().equals(newUser.getAvatar())
				&& user.getRole().equals(login.getRole()));
	}

	@Test
	public void testUpdateValidUser() {
		NewUserBoundary newUser = new NewUserBoundary("user@test.com", "ADMIN", "username", "avatar");
		UserBoundary user = this.restTemplate.postForObject(this.url, newUser, UserBoundary.class);

		String updateEmail = "updateEmail";
		String updateSuperapp = "updateSuperapp";
		String updateUsername = "updateUsername";
		String updateRole = "SUPERAPP_USER";
		String updateAvatar = "updateAvatar";

		UserId updateId = new UserId(updateSuperapp, updateEmail);
		UserBoundary update = new UserBoundary(updateId, updateRole, updateUsername, updateAvatar);

		this.restTemplate.put(this.url + "/" + user.getUserId().getSuperapp() + "/" + user.getUserId().getEmail(),
				update);

		UserBoundary afterUpdate = this.restTemplate.getForObject(
				this.loginUrl + "/" + user.getUserId().getSuperapp() + "/" + user.getUserId().getEmail(),
				UserBoundary.class);

		assertEquals(afterUpdate.getUserId().getSuperapp(), user.getUserId().getSuperapp());
		assertEquals(afterUpdate.getUserId().getEmail(), user.getUserId().getEmail());
		assertEquals(update.getUsername(), updateUsername);
		assertEquals(update.getAvatar(), updateAvatar);
		assertEquals(update.getRole().toString(), updateRole);
	}
	
	@Test
	public void testUpdateUsername() {
		NewUserBoundary newUser = new NewUserBoundary("user@test.com", "ADMIN", "username", "avatar");
		UserBoundary user = this.restTemplate.postForObject(this.url, newUser, UserBoundary.class);

		String updateUsername = "updateUsername";

		UserBoundary update = new UserBoundary();
		update.setUsername(updateUsername);
		
		this.restTemplate.put(this.url + "/" + user.getUserId().getSuperapp() + "/" + user.getUserId().getEmail(),
				update);

		UserBoundary afterUpdate = this.restTemplate.getForObject(
				this.loginUrl + "/" + user.getUserId().getSuperapp() + "/" + user.getUserId().getEmail(),
				UserBoundary.class);

		assertEquals(afterUpdate.getUsername(), updateUsername);

	}

}
