package demo.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import boundaries.command.InvocationUser;
import boundaries.command.MiniAppCommandBoundary;
import boundaries.command.MiniAppCommandID;
import boundaries.command.TargetObject;
import boundaries.object.ObjectId;
import boundaries.user.UserBoundary;
import boundaries.user.UserId;

@RestController
public class AdminController {
	

	@RequestMapping(
			path = "/2023b.noy.tsafrir/admin/users",
			method = {RequestMethod.DELETE})
	public void deleteAllUsers() {
		System.err.println("all users have been deleted");
	}
		
	@RequestMapping(
			path = "/2023b.noy.tsafrir/admin/objects",
			method = {RequestMethod.DELETE})
	public void deleteAllObjects() {
		System.err.println("all objects have been deleted");
	}
	
	@RequestMapping(
			path = "/2023b.noy.tsafrir/admin/miniapp",
			method = {RequestMethod.DELETE})
	public void deleteAllCommandsHistory() {
		System.err.println("all miniapp commands have been deleted");
	}
	
	
	@RequestMapping(
			path = {"/2023b.noy.tsafrir/admin/users"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ArrayList<UserBoundary> getAllUsers () {
		ArrayList<UserBoundary> userList = new ArrayList<UserBoundary>();
        // Create 3 UserBoundary objects
        for (int i = 1; i < 4; i++) {
            UserBoundary user = new UserBoundary();
            user.setUserId(new UserId("2023b.noy.tsafrir" + i, "user" + i + "@example.com"));
            user.setRole("role" + i);
            user.setUsername("user" + i);
            user.setAvatar("https://example.com/avatar" + i);
            userList.add(user);
        }
		return userList;
	}
	
	
	@RequestMapping(
			path = {"/2023b.noy.tsafrir/admin/miniapp"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ArrayList<MiniAppCommandBoundary> getAllMiniappsCommandsHistory () {
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
	return commands;
	}
	
	
	@RequestMapping(
			path = {"/2023b.noy.tsafrir/admin/miniapp/{miniAppName}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ArrayList<MiniAppCommandBoundary> getSpecificMiniappCommandsHistory (
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

	return commands;
	}
	
	
	
	
	
}
