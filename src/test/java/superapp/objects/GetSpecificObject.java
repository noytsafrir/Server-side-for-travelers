package superapp.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;

import superapp.boundaries.object.ObjectBoundary;


class GetSpecificObject extends BaseObjectsTests {

	@PostConstruct
	public void init() {
		super.init();
	}

	@Test
	public void testGetValidSpecificObjectInvalidAdminUser() throws Exception {

		ObjectBoundary newObject = createObject();
		ObjectBoundary createResult = this.restTemplate.postForObject(this.url, newObject, ObjectBoundary.class);

		try {
			this.restTemplate.getForObject(
					this.url + "/" + createResult.getObjectId().getSuperapp() + "/"
							+ createResult.getObjectId().getInternalObjectId()
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					ObjectBoundary.class, userAdmin.getUserId().getSuperapp(), userAdmin.getUserId().getEmail());
		} catch (HttpClientErrorException ex) {
			assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
		}
	}

	@Test
	public void testGetValidSpecificObjectValidSuperAppUser() throws Exception {

		ObjectBoundary newObject = createObject();
		ObjectBoundary createResult = this.restTemplate.postForObject(this.url, newObject, ObjectBoundary.class);

		ObjectBoundary getResult = this.restTemplate.getForObject(
				this.url + "/" + createResult.getObjectId().getSuperapp() + "/"
						+ createResult.getObjectId().getInternalObjectId()
						+ "?userSuperapp={userSuperapp}&userEmail={email}",
				ObjectBoundary.class, userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());

		assertEquals(getResult.getType(), createResult.getType());
		assertEquals(getResult.getAlias(), createResult.getAlias());
		assertEquals(getResult.getObjectId().getSuperapp(), createResult.getObjectId().getSuperapp());
		assertEquals(getResult.getObjectId().getInternalObjectId(), createResult.getObjectId().getInternalObjectId());
	}

	@Test
	public void testGetValidSpecificObjectActiveMiniAppUser() throws Exception {

		ObjectBoundary newObject = createObject();
		ObjectBoundary createResult = this.restTemplate.postForObject(this.url, newObject, ObjectBoundary.class);

		ObjectBoundary getResult = this.restTemplate.getForObject(
				this.url + "/" + createResult.getObjectId().getSuperapp() + "/"
						+ createResult.getObjectId().getInternalObjectId()
						+ "?userSuperapp={userSuperapp}&userEmail={email}",
				ObjectBoundary.class, userMiniapp.getUserId().getSuperapp(), userMiniapp.getUserId().getEmail());

		assertEquals(getResult.getActive(), true);
		assertEquals(getResult.getType(), createResult.getType());
		assertEquals(getResult.getAlias(), createResult.getAlias());
		assertEquals(getResult.getObjectId().getSuperapp(), createResult.getObjectId().getSuperapp());
		assertEquals(getResult.getObjectId().getInternalObjectId(), createResult.getObjectId().getInternalObjectId());
	}

	@Test
	public void testGetValidSpecificObjectNotActiveMiniAppUser() throws Exception {

		ObjectBoundary newObject = createObject();
		newObject.setActive(false);
		ObjectBoundary createResult = this.restTemplate.postForObject(this.url, newObject, ObjectBoundary.class);

		try {
			this.restTemplate.getForObject(
					this.url + "/" + createResult.getObjectId().getSuperapp() + "/"
							+ createResult.getObjectId().getInternalObjectId()
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					ObjectBoundary.class, userMiniapp.getUserId().getSuperapp(), userMiniapp.getUserId().getEmail());
		} catch (HttpClientErrorException ex) {
			assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
		}
	}

	@Test
	public void testGetInValidSpecificObject() throws Exception {

		try {
			this.restTemplate.getForObject(
					this.url + "/" + this.superAppName + "/" + "UnexistId"
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					ObjectBoundary.class, userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		} catch (HttpClientErrorException ex) {
			assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
		}
	}

}
