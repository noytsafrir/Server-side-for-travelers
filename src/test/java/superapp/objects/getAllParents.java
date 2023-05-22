package superapp.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.object.ObjectBoundary;


class getAllParents extends BaseObjectsTests {

	@PostConstruct
	public void init() {
		super.init();
	}

	@Test
	public void testGetParentsOfObjectSuperappSmallPagination() throws Exception {

		ObjectBoundary child = objs.createObject(createObject());
		ObjectBoundary[] parents = createNumberOfObjects(5, false);

		for (ObjectBoundary parent : parents) {
			this.restTemplate.put(
					this.url + "/" +
							parent.getObjectId().getSuperapp() + "/"+
							parent.getObjectId().getInternalObjectId() +
							"/children"+ "?userSuperapp={userSuperapp}&userEmail={email}",
							child.getObjectId(),
							userSuperapp.getUserId().getSuperapp(),
							userSuperapp.getUserId().getEmail());
		}

		ObjectBoundary[] result = this.restTemplate.getForObject(
				this.url + "/" + child.getObjectId().getSuperapp() + "/" + child.getObjectId().getInternalObjectId()
						+ "/parents" + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
				ObjectBoundary[].class,
				userSuperapp.getUserId().getSuperapp(),
				userSuperapp.getUserId().getEmail(),
				10,0);

		assertThat(result)
		.isNotNull()
		.hasSize(5)
		.usingRecursiveFieldByFieldElementComparator()
		.isSubsetOf(parents);
		}
	
	
	@Test
	public void testGetParentsOfObjectSuperappBigPagination() throws Exception {
		ObjectBoundary child = objs.createObject(createObject());
		ObjectBoundary[] parents = createNumberOfObjects(15, false);

		for (ObjectBoundary parent : parents) {
			this.restTemplate.put(
					this.url + "/" +parent.getObjectId().getSuperapp() + "/"+
							parent.getObjectId().getInternalObjectId() +
							"/children"+ "?userSuperapp={userSuperapp}&userEmail={email}",
							child.getObjectId(),
							userSuperapp.getUserId().getSuperapp(),
							userSuperapp.getUserId().getEmail());
		}

		ObjectBoundary[] result = this.restTemplate.getForObject(
				this.url + "/" + child.getObjectId().getSuperapp() + "/" + child.getObjectId().getInternalObjectId()
						+ "/parents" + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
				ObjectBoundary[].class,
				userSuperapp.getUserId().getSuperapp(),
				userSuperapp.getUserId().getEmail(),
				10,1);

		assertThat(result)
		.isNotNull()
		.hasSize(5)
		.usingRecursiveFieldByFieldElementComparator()
		.isSubsetOf(parents);
		}

	@Test
	public void testGetChildrenOfObjectAdmin() throws Exception {
		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary child = objs.createObject(createObject());

		this.restTemplate.put(
				this.url + "/" + 
						parent.getObjectId().getSuperapp() + "/" + 
						parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}",
						child.getObjectId(), 
						userSuperapp.getUserId().getSuperapp(), 
						userSuperapp.getUserId().getEmail());
		
		HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, ()-> {
			this.restTemplate.getForObject(
					this.url + "/" + child.getObjectId().getSuperapp() + "/"
							+ child.getObjectId().getInternalObjectId() + "/parents"
							+ "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
					ObjectBoundary[].class, userAdmin.getUserId().getSuperapp(), userAdmin.getUserId().getEmail(),
					10, 0);
		});
		assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
	}
	
	
	@Test
	public void testGetParentsOfObjectMiniappActive(){
		ObjectBoundary child = objs.createObject(createObject());
		ObjectBoundary[] ParentActive = createNumberOfObjects(14, true);
		ObjectBoundary[] parentsNotActive = createNumberOfObjects(6, false);

		for (ObjectBoundary parent : ParentActive) {
			this.restTemplate.put(
					this.url + "/" + parent.getObjectId().getSuperapp() + "/"
							+ parent.getObjectId().getInternalObjectId() + "/children"
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		}
		
		for (ObjectBoundary parent : parentsNotActive) {
			this.restTemplate.put(
					this.url + "/" + parent.getObjectId().getSuperapp() + "/"
							+ parent.getObjectId().getInternalObjectId() + "/children"
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		}

		ObjectBoundary[]result  = this.restTemplate.getForObject(
				this.url + "/" + child.getObjectId().getSuperapp() + "/" + child.getObjectId().getInternalObjectId()
						+ "/parents" + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
				ObjectBoundary[].class,
				userMiniapp.getUserId().getSuperapp(),
				userMiniapp.getUserId().getEmail(),
				10,1);
		
		assertThat(result)
		.isNotNull()
		.hasSize(4)
		.usingRecursiveFieldByFieldElementComparator()
		.isSubsetOf(ParentActive);
	}
	
	
	@Test
	public void testGetChildrenOfObjectMiniappNotActive(){
		ObjectBoundary child = objs.createObject(createObject());
		ObjectBoundary[] parentsNotActive = createNumberOfObjects(15, false);

		
		for (ObjectBoundary parent : parentsNotActive) {
			this.restTemplate.put(
					this.url + "/" + parent.getObjectId().getSuperapp() + "/"
							+ parent.getObjectId().getInternalObjectId() + "/children"
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		}

		ObjectBoundary[]result  = this.restTemplate.getForObject(
				this.url + "/" + child.getObjectId().getSuperapp() + "/" + child.getObjectId().getInternalObjectId()
						+ "/parents" + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
				ObjectBoundary[].class,
				userMiniapp.getUserId().getSuperapp(),
				userMiniapp.getUserId().getEmail(),
				10,0);
		
		assertThat(result)
		.isNotNull()
		.hasSize(0);
	}
}
