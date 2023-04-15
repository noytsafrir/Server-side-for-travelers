package superapp.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectId;
import superapp.boundaries.user.UserId;
import superapp.logic.ObjectService;
import superapp.logic.UsersService;

@RestController
public class ObjectController {
	private ObjectService objects;
	
	public ObjectService getObjects() {
		return objects;
	}
	@Autowired //when you will make the instance - you should implement this class
	public void setObjects(ObjectService objects) {
		this.objects = objects;
	}

	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}"},
			method = {RequestMethod.PUT},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void updateObject(
			@PathVariable("superapp")String superapp , 
			@PathVariable("internalObjectId") String internalObjectId,
			@RequestBody ObjectBoundary update){
	
		this.objects.updateObject(superapp, internalObjectId, update);
	}

	@RequestMapping(
			path = {"/superapp/objects"},
			method = {RequestMethod.POST},
			produces = {MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ObjectBoundary createObject(
			@RequestBody ObjectBoundary newObject){

		
		return this.objects.createObject(newObject);
		
	}
	
	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ObjectBoundary retrieveObject(
			@PathVariable("superapp")String superapp , 
			@PathVariable("internalObjectId") String internalObjectId){
		
		return this.objects.getSpecsificObject(superapp, internalObjectId);
	}
	
	
	@RequestMapping(
			path = {"/superapp/objects"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ObjectBoundary[] getAllObjects(){
		ArrayList<ObjectBoundary> allObjects = new ArrayList<ObjectBoundary>();
		ObjectBoundary object1 = new ObjectBoundary();
		HashMap<String,Object> details1 = new HashMap<String, Object>();
		details1.put("username", "Bobo The Panda");
		details1.put("age", 42);
		details1.put("virtual money", 45);
		details1.put("rate", 4.6);
		object1.setObjectId(new ObjectId("2023b.noy.tsafrir", "1"));
		object1.setType("traveler");
		object1.setAlias("Bobo");
		object1.setActive(true);
		object1.setCreationTimestamp(new Date());
		object1.setLocation(new Location(1.1,2.2));
		object1.setCreatedBy(new CreatedBy(new UserId("2023b.noy.tsafrir", "bobo@gmail.com")));
		object1.setObjectDetails(details1);
		
		ObjectBoundary object2 = new ObjectBoundary();
		HashMap<String,Object> details2 = new HashMap<String, Object>();
		details2.put("username", "Koko Maroko");
		details2.put("age", 29);
		details2.put("virtual money", 12);
		details2.put("rate", 4.2);
		object2.setObjectId(new ObjectId("2023b.noy.tsafrir", "2"));
		object2.setType("traveler");
		object2.setAlias("Koko");
		object2.setActive(false);
		object2.setCreationTimestamp(new Date());
		object2.setLocation(new Location(1.2345,5.215));
		object2.setCreatedBy(new CreatedBy(new UserId("2023b.noy.tsafrir", "kokoMaroko@gmail.com")));
		object2.setObjectDetails(details2);
		
		ObjectBoundary object3 = new ObjectBoundary();
		HashMap<String,Object> details3 = new HashMap<String, Object>();
		details3.put("username", "WonderStitch");
		details3.put("age", 37.5);
		details3.put("virtual money", 90);
		details3.put("rate", 4.9);
		object3.setObjectId(new ObjectId("2023b.noy.tsafrir", "3"));
		object3.setType("BusinessOwner");
		object3.setAlias("Stitch");
		object3.setActive(true);
		object3.setCreationTimestamp(new Date());
		object3.setLocation(new Location(5.265,4.111));
		object3.setCreatedBy(new CreatedBy(new UserId("2023b.noy.tsafrir", "Stitch@gmail.com")));
		object3.setObjectDetails(details3);
	
		allObjects.add(object1);
		allObjects.add(object2);
		allObjects.add(object3);
		
		return allObjects.toArray(new ObjectBoundary[0]);
	}

}