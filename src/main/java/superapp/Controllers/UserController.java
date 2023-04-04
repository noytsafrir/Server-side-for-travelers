package superapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import superapp.boundaries.user.NewUserBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;
import superapp.logic.UsersService;

@RestController //create an instance of this class
public class UserController {
	private UsersService users;
	
	@Autowired //when you will make the instance - you should implement this class
	public void setUsers(UsersService users) {
		this.users = users;
	}
	

	@RequestMapping(
			path = {"/superapp/users/login/{superapp}/{email}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public UserBoundary login(
			@PathVariable("superapp")String superapp , 
			@PathVariable("email") String email) {		
		return this.users.login(superapp, email);
	}
	

	@RequestMapping(
			path = {"/superapp/users/{superapp}/{userEmail}"},
			method = {RequestMethod.PUT},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void updateUser(
			@PathVariable("superapp")String superapp , 
			@PathVariable("userEmail") String userEmail,
			@RequestBody UserBoundary update) {
		this.users.updateUser(superapp, userEmail, update);
	}
	
	@RequestMapping(
			path = {"/superapp/users"},
			method = {RequestMethod.POST},
			produces = {MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public UserBoundary createNewUser(
			@RequestBody NewUserBoundary newUser) {
		
		//TODO: check if this is the right logic
			UserBoundary user = new UserBoundary();
			UserId userId = new UserId("2023b.noy.tsafrir", newUser.getEmail());
			user.setUserId(userId);
			user.setRole(newUser.getRole());
			user.setUsername(newUser.getUsername());
			user.setAvatar(newUser.getAvatar());
			return this.users.createUser(user);
	}
}
