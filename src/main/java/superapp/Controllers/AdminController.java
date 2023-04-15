package superapp.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import superapp.boundaries.command.InvocationUser;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandID;
import superapp.boundaries.command.TargetObject;
import superapp.boundaries.object.ObjectId;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;
import superapp.logic.ObjectService;
import superapp.logic.UsersService;

@RestController
public class AdminController {
	
	private UsersService users;
	private ObjectService objects;
	
	@Autowired 
	public void setUsers(UsersService users) {
		this.users = users;
	}


	@Autowired 
	public void setObjects(ObjectService objects) {
		this.objects = objects;
	}

	

	@RequestMapping(
			path = "/superapp/admin/users",
			method = {RequestMethod.DELETE})
	public void deleteAllUsers() {
		this.users.deleteAllUsers();
	}
		
	@RequestMapping(
			path = "/superapp/admin/objects",
			method = {RequestMethod.DELETE})
	public void deleteAllObjects() {
		this.objects.deleteAllObject();
	}
	
	@RequestMapping(
			path = "/superapp/admin/miniapp",
			method = {RequestMethod.DELETE})
	public void deleteAllCommandsHistory() {
		System.err.println("all miniapp commands have been deleted");
	}
	
	
	@RequestMapping(
			path = {"/superapp/admin/users"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public UserBoundary[] getAllUsers () {
		return this.users.getAllUsers().toArray(new UserBoundary[0]);
	}
	
	
	@RequestMapping(
			path = {"/superapp/admin/miniapp"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public MiniAppCommandBoundary[] getAllMiniappsCommandsHistory () {
		ArrayList<MiniAppCommandBoundary> commands = new ArrayList<MiniAppCommandBoundary>();

		// create first MiniAppCommandBoundary object
		MiniAppCommandBoundary command1 = new MiniAppCommandBoundary();
		command1.setCommandID(new MiniAppCommandID("mySuperapp", "myMiniapp", "123"));
		command1.setCommand("open");
		command1.setTargetObject(new TargetObject(new ObjectId("mySuperapp", "456")));
		command1.setInvocationTimestamp(new Date());
		command1.setInvokedBy(new InvocationUser(new UserId("mySuperapp", "user789@example.com")));
		command1.setCommandAttributes(new HashMap<String, Object>());
		command1.getCommandAttributes().put("temperture", 17);
		commands.add(command1);

		// create second MiniAppCommandBoundary object
		MiniAppCommandBoundary command2 = new MiniAppCommandBoundary();
		command2.setCommandID(new MiniAppCommandID("yourSuperapp", "yourMiniapp", "456"));
		command2.setCommand("close");
		command2.setTargetObject(new TargetObject(new ObjectId("yourSuperapp", "789")));
		command2.setInvocationTimestamp(new Date());
		command2.setInvokedBy(new InvocationUser(new UserId("yourSuperapp", "user456@example.com")));
		command2.setCommandAttributes(new HashMap<String, Object>());
		command2.getCommandAttributes().put("reason", "user requested");
		commands.add(command2);

		// create third MiniAppCommandBoundary object
		MiniAppCommandBoundary command3 = new MiniAppCommandBoundary();
		command3.setCommandID(new MiniAppCommandID("ourSuperapp", "ourMiniapp", "789"));
		command3.setCommand("edit");
		command3.setTargetObject(new TargetObject(new ObjectId("ourSuperapp", "123")));
		command3.setInvocationTimestamp(new Date());
		command3.setInvokedBy(new InvocationUser(new UserId("ourSuperapp", "user123@example.com")));
		command3.setCommandAttributes(new HashMap<String, Object>());
		command3.getCommandAttributes().put("field", "name");
		command3.getCommandAttributes().put("value", "John Doe");
		commands.add(command3);
	return commands.toArray(new MiniAppCommandBoundary[0]);
	}
	
	@RequestMapping(
			path = {"/superapp/admin/miniapp/{miniAppName}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public MiniAppCommandBoundary[] getSpecificMiniappCommandsHistory (
			@PathVariable("miniAppName")String miniAppName) {
		ArrayList<MiniAppCommandBoundary> commands = new ArrayList<MiniAppCommandBoundary>();

		// create first MiniAppCommandBoundary object
		MiniAppCommandBoundary command1 = new MiniAppCommandBoundary();
		command1.setCommandID(new MiniAppCommandID("mySuperapp", miniAppName, "123"));
		command1.setCommand("open");
		command1.setTargetObject(new TargetObject(new ObjectId("mySuperapp", "456")));
		command1.setInvocationTimestamp(new Date());
		command1.setInvokedBy(new InvocationUser(new UserId("mySuperapp", "user789@example.com")));
		command1.setCommandAttributes(new HashMap<String, Object>());
		command1.getCommandAttributes().put("temperture", 17);
		commands.add(command1);

	return commands.toArray(new MiniAppCommandBoundary[0]);
	}
}
