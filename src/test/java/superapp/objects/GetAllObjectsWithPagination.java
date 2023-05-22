package superapp.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;

import superapp.boundaries.object.ObjectBoundary;


class GetAllObjectsWithPagination extends BaseObjectsTests {

	@PostConstruct
	public void init() {
		super.init();
	}

	@Test
	public void testGetAllObjectSupperappUserSmallPagination() throws Exception {
		ObjectBoundary[] allObjects = createNumberOfObjects(5, false);
		ObjectBoundary[] getResult = this.restTemplate.getForObject(
				this.url + "?userSuperapp={userSuperapp}&userEmail={email}", ObjectBoundary[].class,
				userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());

		assertThat(getResult).isNotNull().hasSize(5).usingRecursiveFieldByFieldElementComparator()
				.isSubsetOf(allObjects);
	}

	@Test
	public void testGetAllObjectSupperappUserBigPagination() throws Exception {
		ObjectBoundary[] allObjects = createNumberOfObjects(15, false);
		ObjectBoundary[] getResult = this.restTemplate.getForObject(
				this.url + "?userSuperapp={userSuperapp}&userEmail={email}", ObjectBoundary[].class,
				userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());

		assertThat(getResult).isNotNull().hasSize(10).usingRecursiveFieldByFieldElementComparator()
				.isSubsetOf(allObjects);
	}

	@Test
	public void testGetAllObjectSupperappUserNoDefaultPagination() throws Exception {
		ObjectBoundary[] allObjects = createNumberOfObjects(15, false);
		ObjectBoundary[] getResult = this.restTemplate.getForObject(
				this.url + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
				ObjectBoundary[].class, userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail(), 10,
				1);

		assertThat(getResult).isNotNull().hasSize(5).usingRecursiveFieldByFieldElementComparator()
				.isSubsetOf(allObjects);
	}

	@Test
	public void testGetAllObjectAdminUser() throws Exception {
		try {
			this.restTemplate.getForObject(this.url + "?userSuperapp={userSuperapp}&userEmail={email}",
					ObjectBoundary[].class, userAdmin.getUserId().getSuperapp(), userAdmin.getUserId().getEmail());

		} catch (HttpClientErrorException ex) {
			assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
		}
	}

	@Test
	public void testGetAllObjectMiniappUser() throws Exception {
		createNumberOfObjects(5, false);
		ObjectBoundary[] activeObjects = createNumberOfObjects(5, true);
		ObjectBoundary[] getResult = this.restTemplate.getForObject(
				this.url + "?userSuperapp={userSuperapp}&userEmail={email}", ObjectBoundary[].class,
				userMiniapp.getUserId().getSuperapp(), userMiniapp.getUserId().getEmail());

		assertThat(getResult)
		.isNotNull()
		.hasSize(5)
		.usingRecursiveFieldByFieldElementComparator()
		.isSubsetOf(activeObjects);
	}

	@Test
	public void testGetAllObjectMiniappUserJustNoActive() throws Exception {
		createNumberOfObjects(10, false);
		ObjectBoundary[] getResult = this.restTemplate.getForObject(
				this.url + "?userSuperapp={userSuperapp}&userEmail={email}", ObjectBoundary[].class,
				userMiniapp.getUserId().getSuperapp(), userMiniapp.getUserId().getEmail());

		assertThat(getResult)
		.isNotNull()
		.hasSize(0);
	}

}
