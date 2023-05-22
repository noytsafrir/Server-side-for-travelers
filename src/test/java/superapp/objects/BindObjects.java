package superapp.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.converters.ObjectConvertor;
import superapp.dal.ObjectCrud;
import superapp.data.ObjectEntity;

class BindObjects extends BaseObjectsTests {

	private ObjectCrud objectCrud;
	private ObjectConvertor converter;

	@Autowired
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
	public void testBindObjectSuperapp() throws Exception {

		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary child = objs.createObject(createObject());

		this.restTemplate.put(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}",
				child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());

		Optional<ObjectEntity> parentBindE = this.objectCrud.findById(converter.idToEntity(parent.getObjectId()));
		List<ObjectEntity> children = parentBindE.get().getChildren();
		
		assertThat(children).isNotNull().hasSize(1);
		assertEquals(child.getObjectId().getInternalObjectId(), children.get(0).getObjectId().getInternalObjectId());
	}
	
	@Test
	public void testBindObjectAdmin() throws Exception {
		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary child = objs.createObject(createObject());
		
		HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, ()-> {
			this.restTemplate.put(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}",
				child.getObjectId(),
				userAdmin.getUserId().getSuperapp(),
				userAdmin.getUserId().getEmail());
			});
			assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
	}
	
	@Test
	public void testBindObjectMiniApp() throws Exception {
		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary child = objs.createObject(createObject());
		
		HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, ()-> {
			this.restTemplate.put(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}",
				child.getObjectId(),
				userMiniapp.getUserId().getSuperapp(),
				userMiniapp.getUserId().getEmail());
			});
			assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
	}
	
	
	@Test
	public void testBindObjectInvalidParent() throws Exception {
		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary child = objs.createObject(createObject());
		
		HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, ()-> {
			this.restTemplate.put(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + "unRealParentID"
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}",
				child.getObjectId(),
				userSuperapp.getUserId().getSuperapp(),
				userSuperapp.getUserId().getEmail());
			});
			assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
	}
	
	@Test
	public void testBindObjectInvalidChild() throws Exception {
		ObjectBoundary parent = objs.createObject(createObject());
		SuperAppObjectIdBoundary objectId= new SuperAppObjectIdBoundary();
		
		HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, ()-> {
			this.restTemplate.put(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}",
				objectId,
				userSuperapp.getUserId().getSuperapp(),
				userSuperapp.getUserId().getEmail());
			});
			assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
	}
	
	
	public ObjectBoundary[] getChildrenAs(UserBoundary user, ObjectBoundary parent) {
		return this.restTemplate.getForObject(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}",
				ObjectBoundary[].class, user.getUserId().getSuperapp(), user.getUserId().getEmail());
	}
}
