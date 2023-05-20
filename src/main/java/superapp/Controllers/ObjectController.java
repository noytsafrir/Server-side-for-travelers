package superapp.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import superapp.boundaries.object.ObjectBoundary;
import superapp.logic.ObjectService;


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
	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}"},
			method = {RequestMethod.PUT},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void updateObject(
			@PathVariable("superapp")String superapp , 
			@PathVariable("internalObjectId") String internalObjectId,
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") String userSuperapp,
			@RequestParam(name = "userEmail", required = false, defaultValue = "") String email,
			@RequestBody ObjectBoundary update){
	
		this.objects.updateObject(superapp, internalObjectId, update);
	}
	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = {"/superapp/objects"},
			method = {RequestMethod.POST},
			produces = {MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ObjectBoundary createObject(
			@RequestBody ObjectBoundary newObject){
		
		return this.objects.createObject(newObject);
		
	}
	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ObjectBoundary retrieveObject(
			@PathVariable("superapp")String superapp , 
			@PathVariable("internalObjectId") String internalObjectId){
		
		return this.objects.getSpecsificObject(superapp, internalObjectId);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(
			path = {"/superapp/objects"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<ObjectBoundary> getAllObjects(){
		
		return this.objects.getAllObjects();
	}

}