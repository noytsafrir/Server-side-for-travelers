package superapp.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.HashMap;


import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;

import superapp.boundaries.user.UserId;

class CreateUpdateObjects extends BaseObjectsTests {

	@PostConstruct
	public void init() {
		super.init();
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

		this.restTemplate.put(this.url + "/" + createResult.getObjectId().getSuperapp() + "/"
				+ createResult.getObjectId().getInternalObjectId() + "?userSuperapp={userSuperapp}&userEmail={email}",
				newObject, userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());

		ObjectBoundary updateResult = this.restTemplate.getForObject(
				this.url + "/" + createResult.getObjectId().getSuperapp() + "/"
						+ createResult.getObjectId().getInternalObjectId()
						+ "?userSuperapp={userSuperapp}&userEmail={email}",
				ObjectBoundary.class, userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());

		assertEquals(updateResult.getType(), "updateType");
		assertEquals(updateResult.getAlias(), "updateAlias");
		assertEquals(updateResult.getCreationTimestamp(), createResult.getCreationTimestamp());
		assertEquals(updateResult.getObjectId().getSuperapp(), createResult.getObjectId().getSuperapp());
		assertEquals(updateResult.getObjectId().getInternalObjectId(),
				createResult.getObjectId().getInternalObjectId());
	}

	@Test
	public void testUpdateValidObjectInvalidUser() throws Exception {

		ObjectBoundary newObject = createObject();
		ObjectBoundary createResult = this.restTemplate.postForObject(this.url, newObject, ObjectBoundary.class);

		newObject.setAlias("updateAlias");
		newObject.setType("updateType");

		try {
			this.restTemplate.put(
					this.url + "/" + createResult.getObjectId().getSuperapp() + "/"
							+ createResult.getObjectId().getInternalObjectId()
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					newObject, userMiniapp.getUserId().getSuperapp(), userMiniapp.getUserId().getEmail());
		} catch (HttpClientErrorException ex) {
			assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
		}
	}
}
