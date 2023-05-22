package superapp.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.converters.ObjectConvertor;
import superapp.dal.ObjectCrud;
import superapp.data.ObjectEntity;

class BindObjects extends BaseObjectsTests {

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

	// TODO: fix the get children
	@Test
	public void testBindObject() throws Exception {

		ObjectBoundary parent = objs.createObject(createObject());
		ObjectBoundary child = objs.createObject(createObject());

		this.restTemplate.put(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}",
				child.getObjectId(), userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());

//		ObjectBoundary parentBind = objs.getSpecsificObject(parent.getObjectId().getSuperapp(), parent.getObjectId().getInternalObjectId(),
//				userSuperapp.getUserId().getSuperapp(), userSuperapp.getUserId().getEmail());

		Optional<ObjectEntity> parentBindE = this.objectCrud.findById(converter.idToEntity(parent.getObjectId()));
		
		List<ObjectEntity> children = parentBindE.get().getChildren();
		
		assertThat(children)
			.isNotNull()
			.hasSize(1);

		assertEquals(child.getObjectId().getInternalObjectId(), children.get(0).getObjectId().getInternalObjectId());
	}

	public ObjectBoundary[] getChildrenAs(UserBoundary user, ObjectBoundary parent) {
		return this.restTemplate.getForObject(
				this.url + "/" + parent.getObjectId().getSuperapp() + "/" + parent.getObjectId().getInternalObjectId()
						+ "/children" + "?userSuperapp={userSuperapp}&userEmail={email}",
				ObjectBoundary[].class, user.getUserId().getSuperapp(), user.getUserId().getEmail());
	}
}
