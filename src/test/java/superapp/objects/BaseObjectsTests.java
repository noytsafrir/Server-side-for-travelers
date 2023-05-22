package superapp.objects;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import superapp.BaseControllerTest;
import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserId;

import superapp.logic.ObjectServiceWithPagination;

abstract class BaseObjectsTests extends BaseControllerTest {

	protected String url;
	protected String deleteUrl;
	protected ObjectServiceWithPagination objs;

	@PostConstruct
	public void init() {
		super.init();
		this.url = this.baseUrl + "/objects";
		this.deleteUrl = this.adminUrl + "objects";
	}

	@Autowired
	public void setObjs(ObjectServiceWithPagination objs) {
		this.objs = objs;
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.restTemplate.delete(this.deleteUrl + "?userSuperapp={superapp}&userEmail={email}",
				userAdmin.getUserId().getSuperapp(), userAdmin.getUserId().getEmail());
	}

	public ObjectBoundary[] createNumberOfObjects(int num, boolean isActive) {
		List<ObjectBoundary> allObjects = new ArrayList<ObjectBoundary>();
		ObjectBoundary newObject;
		for (int i = 0; i < num; i++) {
			newObject = createObject();
			newObject.setAlias("object. " + i);
			newObject.setActive(isActive);
			objs.createObject(newObject);
			allObjects.add(newObject);
		}
		return allObjects.toArray(new ObjectBoundary[0]);
	}

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
}
