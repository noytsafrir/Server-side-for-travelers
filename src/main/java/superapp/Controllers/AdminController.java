package superapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.logic.MiniAppCommandService;
import superapp.logic.ObjectService;
import superapp.logic.UsersService;

@RestController
public class AdminController {
	
	private UsersService users;
	private ObjectService objects;
	private MiniAppCommandService commands;

	@Autowired 
	public void setUsers(UsersService users) {
		this.users = users;
	}


	@Autowired 
	public void setObjects(ObjectService objects) {
		this.objects = objects;
	}
	
	@Autowired 
	public void setCommands(MiniAppCommandService commands) {
		this.commands = commands;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = "/superapp/admin/users",
			method = {RequestMethod.DELETE})
	public void deleteAllUsers() {
		this.users.deleteAllUsers();
	}
	@CrossOrigin(origins = "*")	
	@RequestMapping(
			path = "/superapp/admin/objects",
			method = {RequestMethod.DELETE})
	public void deleteAllObjects() {
		this.objects.deleteAllObject();
	}
	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = "/superapp/admin/miniapp",
			method = {RequestMethod.DELETE})
	public void deleteAllCommandsHistory() {
		this.commands.deleteAllCommands();
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = {"/superapp/admin/users"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public UserBoundary[] getAllUsers () {
		return this.users.getAllUsers().toArray(new UserBoundary[0]);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = {"/superapp/admin/miniapp"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public MiniAppCommandBoundary[] getAllMiniappsCommandsHistory () {
		return this.commands.getAllCommands().toArray(new MiniAppCommandBoundary[0]);
	}
	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = {"/superapp/admin/miniapp/{miniAppName}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public MiniAppCommandBoundary[] getSpecificMiniappCommandsHistory (
			@PathVariable("miniAppName")String miniAppName) {
		return this.commands.getAllMiniAppCommands(miniAppName).toArray(new MiniAppCommandBoundary[0]);
	}
}
