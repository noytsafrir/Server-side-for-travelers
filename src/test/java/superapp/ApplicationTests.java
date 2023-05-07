package superapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.user.NewUserBoundary;
import superapp.boundaries.user.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    protected String url;
    protected int port;
    protected RestTemplate restTemplate;
    protected String superAppName;

    @PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
        this.url = "http://localhost:" + this.port + "/superapp/users";
	}
    
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

//	@AfterEach
//	public void tearDown() {
//		// cleanup database
//		this.restTemplate
//			.delete(this.url);
//	}
	

	@Value("${spring.application.name:defaultValue}")
	public void setSuperAppName(String superAppName) {
		this.superAppName = superAppName;
	}
	
    @Test
    public final void contextLoads() {}

    @Test
    public void testCreateNewUser() throws Exception {
    	NewUserBoundary newUser =new NewUserBoundary(
                "test2@email.com",
                "ADMIN",
                "Username test",
                "Avatar URL test"
        );
    	UserBoundary createdUser = this.restTemplate.postForObject(this.url,newUser, UserBoundary.class);
        
        assertThat(createdUser.getUserId().getEmail().equals(newUser.getEmail()) &&
        			createdUser.getUsername().equals(newUser.getUsername()) &&
        			createdUser.getAvatar().equals(newUser.getAvatar()) &&
        			createdUser.getRole().equals(newUser.getRole()));
    }
    
    @Test
    public void testLoginUser() throws Exception {
    	String email = "test1@email.com";
    	String testUrl = this.url + "/login/" + this.superAppName +"/" + email;
    	
    	UserBoundary loginUser = this.restTemplate.getForObject(testUrl, UserBoundary.class);
        assertThat(loginUser.getUserId().getEmail().equals(email));
    }
}
