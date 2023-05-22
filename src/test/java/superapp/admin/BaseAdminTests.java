package superapp.admin;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import superapp.BaseControllerTest;
import superapp.boundaries.command.InvocationUser;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandID;
import superapp.boundaries.command.TargetObject;
import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserId;
import superapp.logic.MiniAppCommandService;
import superapp.logic.ObjectServiceWithPagination;

abstract class BaseAdminTests extends BaseControllerTest {

	protected String urlObject;
	protected String urlUser;
	protected String urlCommand;
	protected ObjectServiceWithPagination objs;
	protected MiniAppCommandService miniapps;

	@PostConstruct
	public void init() {
		super.init();
		this.urlUser	= this.adminUrl + "users";
		this.urlObject	= this.adminUrl + "objects";
		this.urlCommand = this.adminUrl + "miniapp";
	}

	@Autowired
	public void setObjs(ObjectServiceWithPagination objs) {
		this.objs = objs;
	}

	@Autowired
	public void setMiniapps(MiniAppCommandService miniapp) {
		this.miniapps = miniapp;
	}

	@AfterEach
	public void tearDown() throws Exception {}

	public ObjectBoundary createObject() {
		ObjectBoundary newObject = new ObjectBoundary();
		SuperAppObjectIdBoundary objId = new SuperAppObjectIdBoundary();
		UserId userId = new UserId(this.superAppName, "test@email.com");
		HashMap<String, Object> details = new HashMap<String, Object>();
		details.put("attr1", "testAttr1");
		details.put("attr2", true);
		details.put("attr3", 1);
		details.put("attr4", 0.5);
		newObject.setObjectId(objId);
		newObject.setType("type");
		newObject.setAlias("alias");
		newObject.setActive(true);
		newObject.setCreationTimestamp(new Date());
		newObject.setLocation(new Location(0.0, 1.1));
		newObject.setCreatedBy(new CreatedBy(userId));
		newObject.setObjectDetails(details);
		return newObject;
	}
	
	public ObjectBoundary[] createNumberOfObjects(int num) {
		List<ObjectBoundary> allObjects = new ArrayList<ObjectBoundary>();
		ObjectBoundary newObject;
		for (int i = 0; i < num; i++) {
			newObject = createObject();
			newObject.setAlias("object. " + i);
			newObject.setActive(true);
			objs.createObject(newObject);
			allObjects.add(newObject);
		}
		return allObjects.toArray(new ObjectBoundary[0]);
	}
	
	public MiniAppCommandBoundary createCommand() {
		ObjectBoundary obj = createObject();
		objs.createObject(obj);
		MiniAppCommandBoundary newCommand = new MiniAppCommandBoundary();
		MiniAppCommandID commandId = new MiniAppCommandID(this.superAppName, "miniapp", "test");
		HashMap<String, Object> details = new HashMap<String, Object>();

		newCommand.setCommandId(commandId);
		newCommand.setCommand("command");
		newCommand.setTargetObject(new TargetObject(obj.getObjectId()));
		newCommand.setInvocationTimestamp(new Date());
		newCommand.setInvokedBy(new InvocationUser(userMiniapp.getUserId()));
		newCommand.setCommandAttributes(details);
		return newCommand;
	}
	
	
	public MiniAppCommandBoundary[] createNumberOfCommands(int num) {
		List<MiniAppCommandBoundary> allCommands = new ArrayList<MiniAppCommandBoundary>();
		MiniAppCommandBoundary newCommand;
		for (int i = 0; i < num; i++) {
			newCommand = createCommand();
			newCommand.setCommand("command. " + i);
			miniapps.invokeCommand(newCommand);
			allCommands.add(newCommand);
		}
		return allCommands.toArray(new MiniAppCommandBoundary[0]);
	}
	
	public ObjectBoundary[] getAllObjects() {
		ObjectBoundary[] allObjects = this.restTemplate.getForObject(
				this.baseUrl +"objects" + "?userSuperapp={userSuperapp}&userEmail={email}", 
				ObjectBoundary[].class,
				userSuperapp.getUserId().getSuperapp(), 
				userSuperapp.getUserId().getEmail());
		return allObjects;
	}
	
}
