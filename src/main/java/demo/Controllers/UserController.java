package demo.Controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Boundary.User.NewUserBoundary;
import Boundary.User.UserBoundary;
import Boundary.User.UserId;

@RestController
public class UserController {

	@RequestMapping(
			path = {"/2023b.noy.tsafrir/users/login/{superapp}/{email}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public UserBoundary login(
			@PathVariable("superapp")String superapp , 
			@PathVariable("email") String email) {
		UserId user = new UserId(superapp,email);
		return new UserBoundary(user, "Role", "Name", "Avatar");
	}
	
	
	@RequestMapping(
			path = {"/2023b.noy.tsafrir/users/{superapp}/{userEmail}"},
			method = {RequestMethod.PUT},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void updateUser(
			@PathVariable("superapp")String superapp , 
			@PathVariable("userEmail") String userEmail,
			@RequestBody UserBoundary update) {
			System.err.println("superapp: "+ superapp);
			System.err.println("userEmail: "+ userEmail);
			System.err.println("update: "+ update);
	}
	
	@RequestMapping(
			path = {"/2023b.noy.tsafrir/users"},
			method = {RequestMethod.POST},
			produces = {MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public UserBoundary createNewUser(
			@RequestBody NewUserBoundary newUser) {
			UserBoundary user = new UserBoundary();
			UserId userId = new UserId("2023b.noy.tsafrir", newUser.getEmail());
			user.setUserId(userId);
			user.setRole(newUser.getRole());
			user.setUsername(newUser.getUsername());
			user.setAvatar(newUser.getAvatar());
			return user;
	}
}
