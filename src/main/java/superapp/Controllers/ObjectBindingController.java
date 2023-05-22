package superapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;

import superapp.logic.ObjectServiceWithPagination;


@RestController
public class ObjectBindingController {
	private ObjectServiceWithPagination objects;
	
	public ObjectServiceWithPagination getObjects() {
		return objects;
	}
	@Autowired //when you will make the instance - you should implement this class
	public void setObjects(ObjectServiceWithPagination objects) {
		this.objects = objects;
	}

	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}/children"},
			method = {RequestMethod.PUT},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void bindObject(
			@PathVariable("superapp")String superapp , 
			@PathVariable("internalObjectId") String internalObjectId,
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") String userSuperapp,
			@RequestParam(name = "userEmail", required = false, defaultValue = "") String email,
			@RequestBody SuperAppObjectIdBoundary child){
	
		this.objects.bindObjectToParent(superapp, internalObjectId, child, userSuperapp, email);
	}
	
	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}/children"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ObjectBoundary[] getObjectChildren(
			@PathVariable("superapp")String superapp , 
			@PathVariable("internalObjectId") String internalObjectId,
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") String userSuperapp,
			@RequestParam(name = "userEmail", required = false, defaultValue = "") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page){
	
		return this.objects
				.getAllChildrenOfObject(superapp, internalObjectId, userSuperapp, email, size, page)
				.toArray(new ObjectBoundary[0]);
	}
	
	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}/parents"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ObjectBoundary[] getObjectParents(
			@PathVariable("superapp")String superapp , 
			@PathVariable("internalObjectId") String internalObjectId,
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") String userSuperapp,
			@RequestParam(name = "userEmail", required = false, defaultValue = "") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page){

		return this.objects
				.getAllParentsOfObject(superapp, internalObjectId, userSuperapp, email, size, page)
				.toArray(new ObjectBoundary[0]);
	}
}