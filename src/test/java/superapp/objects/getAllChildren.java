package superapp.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.converters.ObjectConvertor;
import superapp.dal.ObjectCrud;
import superapp.data.ObjectEntity;

class getAllChildren extends BaseObjectsTests {

	private ObjectCrud objectCrud;
	private ObjectConvertor converter;

	@Autowired // put in the parameter an instance
	public void setObjectCrud(ObjectCrud oc) {
		this.objectCrud = oc;
	}

	@Autowired
	public void setObjectConverter(ObjectConvertor convertor) {
		this.converter = convertor;
	}

	@PostConstruct
	public void init() {
		super.init();
	}


	@Test
	public void testGetChildrenOfObjectSuperappSmallPagination(){

		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary[] children = createNumberOfObjects(5, false);

		for (ObjectBoundary child : children) {
			this.restTemplate.put(
					this.url + "/" + parent.getObjectId().getSuperapp() + "/"
							+ parent.getObjectId().getInternalObjectId() + "/children"
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		}

		ObjectBoundary[] result = this.restTemplate.getForObject(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
				ObjectBoundary[].class,
				userSuperapp.getUserId().getSuperapp(),
				userSuperapp.getUserId().getEmail(),
				10,0);

		assertThat(result)
		.isNotNull()
		.hasSize(5)
		.usingRecursiveFieldByFieldElementComparator()
		.isSubsetOf(children);
		}
	
	
	@Test
	public void testGetChildrenOfObjectSuperappBigPagination(){
		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary[] children = createNumberOfObjects(15, false);

		for (ObjectBoundary child : children) {
			this.restTemplate.put(
					this.url + "/" + parent.getObjectId().getSuperapp() + "/"
							+ parent.getObjectId().getInternalObjectId() + "/children"
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		}

		ObjectBoundary[] result = this.restTemplate.getForObject(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
				ObjectBoundary[].class,
				userSuperapp.getUserId().getSuperapp(),
				userSuperapp.getUserId().getEmail(),
				10,1);

		assertThat(result)
		.isNotNull()
		.hasSize(5)
		.usingRecursiveFieldByFieldElementComparator()
		.isSubsetOf(children);
		}

	@Test
	public void testGetChildrenOfObjectAdmin(){
		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary child = objs.createObject(createObject());

		this.restTemplate.put(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}",
				child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, ()-> {
			this.restTemplate.getForObject(
					this.url + "/" + parent.getObjectId().getSuperapp() + "/"
							+ parent.getObjectId().getInternalObjectId() + "/children"
							+ "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
					ObjectBoundary[].class, userAdmin.getUserId().getSuperapp(), userAdmin.getUserId().getEmail(),
					10, 0);
		});
		assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
	}
	
	
	@Test
	public void testGetChildrenOfObjectMiniappActive(){
		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary[] childrenActive = createNumberOfObjects(12, true);
		ObjectBoundary[] childrenNotActive = createNumberOfObjects(8, false);

		for (ObjectBoundary child : childrenActive) {
			this.restTemplate.put(
					this.url + "/" + parent.getObjectId().getSuperapp() + "/"
							+ parent.getObjectId().getInternalObjectId() + "/children"
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		}
		
		for (ObjectBoundary child : childrenNotActive) {
			this.restTemplate.put(
					this.url + "/" + parent.getObjectId().getSuperapp() + "/"
							+ parent.getObjectId().getInternalObjectId() + "/children"
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		}

		ObjectBoundary[]result  = this.restTemplate.getForObject(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
				ObjectBoundary[].class,
				userMiniapp.getUserId().getSuperapp(),
				userMiniapp.getUserId().getEmail(),
				10,1);
		
		assertThat(result)
		.isNotNull()
		.hasSize(2)
		.usingRecursiveFieldByFieldElementComparator()
		.isSubsetOf(childrenActive);
	}
	
	
	@Test
	public void testGetChildrenOfObjectMiniappNotActive(){
		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary[] childrenNotActive = createNumberOfObjects(15, false);

		for (ObjectBoundary child : childrenNotActive) {
			this.restTemplate.put(
					this.url + "/" + parent.getObjectId().getSuperapp() + "/"
							+ parent.getObjectId().getInternalObjectId() + "/children"
							+ "?userSuperapp={userSuperapp}&userEmail={email}",
					child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());
		}
		
		ObjectBoundary[]result  = this.restTemplate.getForObject(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}",
				ObjectBoundary[].class,
				userMiniapp.getUserId().getSuperapp(),
				userMiniapp.getUserId().getEmail(),
				10,1);
		
		assertThat(result)
		.isNotNull()
		.hasSize(0);
	}
}
