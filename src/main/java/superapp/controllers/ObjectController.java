package superapp.controllers;

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
import superapp.logic.ObjectServiceWithPagination;

@RestController
public class ObjectController {
	private ObjectServiceWithPagination objects;

	public ObjectServiceWithPagination getObjects() {
		return objects;
	}

	@Autowired
	public void setObjects(ObjectServiceWithPagination objects) {
		this.objects = objects;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/superapp/objects/{superapp}/{internalObjectId}" }, method = {
			RequestMethod.PUT }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void updateObject(@PathVariable("superapp") String superapp,
			@PathVariable("internalObjectId") String internalObjectId,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestBody ObjectBoundary update) {
		this.objects.updateObject(superapp, internalObjectId, update, userSuperapp, email);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/superapp/objects" }, method = { RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary createObject(@RequestBody ObjectBoundary newObject) {
		return this.objects.createObject(newObject);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/superapp/objects/{superapp}/{internalObjectId}" }, method = {
			RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary retrieveObject(@PathVariable("superapp") String superapp,
			@PathVariable("internalObjectId") String internalObjectId,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		return this.objects.getSpecsificObject(superapp, internalObjectId, userSuperapp, email);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/superapp/objects" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getAllObjects(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.objects.getAllObjects(userSuperapp, email, size, page).toArray(new ObjectBoundary[0]);
	}

}