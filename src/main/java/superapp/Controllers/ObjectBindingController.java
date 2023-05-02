package superapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectId;
import superapp.logic.ObjectService;
import superapp.logic.ObjectServiceBinding;


@RestController
public class ObjectBindingController {
	private ObjectServiceBinding objects;
	
	public ObjectService getObjects() {
		return objects;
	}
	@Autowired //when you will make the instance - you should implement this class
	public void setObjects(ObjectServiceBinding objects) {
		this.objects = objects;
	}

	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}/children"},
			method = {RequestMethod.PUT},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void bindObject(
			@PathVariable("superapp")String superapp , 
			@PathVariable("internalObjectId") String internalObjectId,
			@RequestBody ObjectId child){
	
		this.objects.bindObjectToParent(superapp, internalObjectId, child);
	}
	
	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}/children"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ObjectBoundary[] getObjectChildren(
			@PathVariable("superapp")String superapp , 
			@PathVariable("internalObjectId") String internalObjectId){
		
		return this.objects
				.getAllChildrenOfObject(superapp, internalObjectId)
				.toArray(new ObjectBoundary[0]);
	}
	
	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}/parents"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ObjectBoundary[] getObjectParents(
			@PathVariable("superapp")String superapp , 
			@PathVariable("internalObjectId") String internalObjectId){
		
		return this.objects
				.getAllParentsOfObject(superapp, internalObjectId)
				.toArray(new ObjectBoundary[0]);
	}
}