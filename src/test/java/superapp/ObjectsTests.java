package superapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;
import superapp.data.UserRole;

class ObjectsTests extends BaseControllerTest {

	protected String url;
	protected String deleteUrl;

	@PostConstruct
	public void init() {
		super.init();
		this.url = this.baseUrl + "/objects";
		this.deleteUrl = this.adminUrl + "objects";
	}

	@AfterEach
	public void tearDown() {
		this.restTemplate.delete(this.deleteUrl);
	}

	@Value("${spring.application.name:defaultValue}")
	public void setSuperAppName(String superAppName) {
		this.superAppName = superAppName;
	}

	@Test
	public void testCreateValidObject() throws Exception {
		ObjectBoundary newObject = new ObjectBoundary();
		SuperAppObjectIdBoundary objId = new SuperAppObjectIdBoundary();
		UserId userId = new UserId(this.superAppName, "bobo@gmail.com");
		HashMap<String, Object> details = new HashMap<String, Object>();
		details.put("username", "Bobo The Panda");
		details.put("age", 42);
		details.put("virtual money", 45);
		details.put("rate", 4.6);
		newObject.setObjectId(objId);
		newObject.setType("traveler");
		newObject.setAlias("Bobo");
		newObject.setActive(true);
		newObject.setCreationTimestamp(new Date());
		newObject.setLocation(new Location(1.1, 2.2));
		newObject.setCreatedBy(new CreatedBy(userId));
		newObject.setObjectDetails(details);

		ObjectBoundary result = this.restTemplate.postForObject(this.url, newObject, ObjectBoundary.class);

		assertEquals(newObject.getType(), result.getType());
		assertEquals(newObject.getAlias(), result.getAlias());
		assertEquals(newObject.getLocation(), result.getLocation());
		assertEquals(newObject.getCreatedBy().getUserId().getSuperapp(),
				result.getCreatedBy().getUserId().getSuperapp());
		assertEquals(newObject.getCreatedBy().getUserId().getEmail(), result.getCreatedBy().getUserId().getEmail());
		assertNotNull(result.getObjectId());
		assertNotNull(result.getObjectId().getSuperapp());
		assertNotNull(result.getObjectId().getInternalObjectId());
	}

	@Test
	public void testUpdateValidObjectValidUser() throws Exception {
		ObjectBoundary newObject = createObject();
		ObjectBoundary createResult = this.restTemplate.postForObject(this.url, newObject, ObjectBoundary.class);

		newObject.setAlias("updateAlias");
		newObject.setType("updateType");

		UserBoundary user = super.createUser(UserRole.SUPERAPP_USER);

		try {
			restTemplate.postForEntity(url, user, UserBoundary.class);
		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.CONFLICT))
				user = super.loginUser(user.getUserId().getEmail());
			else
				throw ex;
		}

		Map<String, String> vars = new HashMap<>();
		vars.put("userSuperapp", user.getUserId().getSuperapp());
		vars.put("userEmail", user.getUserId().getEmail());

		this.restTemplate.put(this.url, newObject, vars);
		ObjectBoundary updateResult = this.restTemplate.getForObject(this.url + "/" + createResult.getObjectId().getSuperapp()
				+ "/" + createResult.getObjectId().getInternalObjectId(), ObjectBoundary.class);

		assertEquals(updateResult.getType(), "updateAlias");
		assertEquals(updateResult.getAlias(), "updateType");
		assertEquals(updateResult.getCreationTimestamp(), createResult.getCreationTimestamp());
		assertEquals(updateResult.getObjectId().getSuperapp(), createResult.getObjectId().getSuperapp());
		assertEquals(updateResult.getObjectId().getInternalObjectId(), createResult.getObjectId().getInternalObjectId());
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
