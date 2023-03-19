package Controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import Boundary.UserBoundary;
import Boundary.UserId;

@RestController
public class UserController {
	public UserController() {}

	@RequestMapping(
			path = {"/2023b.noy.tsafrir/users/login/{superapp}/{email}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	
	public UserBoundary login(@PathVariable("superapp")String superapp , @PathVariable("email") String email) {
		UserId user = new UserId(superapp,email);
		return new UserBoundary(user, "Role", "Name", "Avatar");
	}

}
