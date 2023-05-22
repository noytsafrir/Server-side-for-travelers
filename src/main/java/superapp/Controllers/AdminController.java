package superapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.logic.MiniAppCommandAsyncService;
import superapp.logic.ObjectServiceWithPagination;
import superapp.logic.UsersServiceNew;

@RestController
public class AdminController {

	private UsersServiceNew users;
	private ObjectServiceWithPagination objects;
	private MiniAppCommandAsyncService commands;

	@Autowired
	public void setUsers(UsersServiceNew users) {
		this.users = users;
	}

	@Autowired
	public void setObjects(ObjectServiceWithPagination objects) {
		this.objects = objects;
	}

	@Autowired
	public void setCommands(MiniAppCommandAsyncService commands) {
		this.commands = commands;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = "/superapp/admin/users", 
			method = { RequestMethod.DELETE })
	public void deleteAllUsers(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		this.users.deleteAllUsers(userSuperapp, email);;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = "/superapp/admin/objects",
			method = { RequestMethod.DELETE })
	public void deleteAllObjects(
			@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		this.objects.deleteAllObject(superapp, email);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = "/superapp/admin/miniapp",
			method = { RequestMethod.DELETE })
	public void deleteAllCommandsHistory(
			@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		this.commands.deleteAllCommands(superapp, email);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = { "/superapp/admin/users" },
			method = { RequestMethod.GET },
			produces = {MediaType.APPLICATION_JSON_VALUE })
	public UserBoundary[] getAllUsers(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.users.getAllUsers().toArray(new UserBoundary[0]);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = { "/superapp/admin/miniapp" },
			method = { RequestMethod.GET },
			produces = {MediaType.APPLICATION_JSON_VALUE })
	public MiniAppCommandBoundary[] getAllMiniappsCommandsHistory(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.commands.getAllCommands(userSuperapp, email, size, page).toArray(new MiniAppCommandBoundary[0]);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = { "/superapp/admin/miniapp/{miniAppName}" },
			method = { RequestMethod.GET },
			produces = {MediaType.APPLICATION_JSON_VALUE })
	public MiniAppCommandBoundary[] getSpecificMiniappCommandsHistory(
			@PathVariable("miniAppName") String miniAppName,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.commands.getAllMiniAppCommands(miniAppName, userSuperapp, email, size, page).toArray(new MiniAppCommandBoundary[0]);
	}
}
