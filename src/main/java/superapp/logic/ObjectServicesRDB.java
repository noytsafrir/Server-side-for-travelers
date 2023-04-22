package superapp.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectId;
import superapp.boundaries.user.UserBoundary;
import superapp.converters.ObjectConvertor;
import superapp.converters.UserConverter;
import superapp.dal.ObjectCrud;
import superapp.dal.UserCrud;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;
import superapp.data.UserEntity;
import superapp.data.UserPrimaryKeyId;

@Service //spring create an instance from this class so we can use it
public class ObjectServicesRDB implements ObjectServiceBinding {
	 private ObjectCrud objectCrud;
	private ObjectConvertor convertor;
	private String superappName;

	
	@Autowired //put in the parameter an instance
	public void setObjectCrud(ObjectCrud oc) {
		this.objectCrud = oc;
	}
	
	@Autowired
	public void setObjectConverter(ObjectConvertor convertor) {
		this.convertor = convertor;
	}

	//have spring inject a configuration to this method
	@Value("${spring.application.name:defaultValue}")
	public void setSuperappName(String superappName) {
		this.superappName = superappName;
	}
	
	// ???
	@PostConstruct //after its build but before
	public void init() {
		System.err.println("********** spring.application.name = "+this.superappName);
	}
	
	@Override
	@Transactional //will be atomic - part of the transaction 
	public ObjectBoundary createObject(ObjectBoundary obej) {
		// the server need to make object internal id ? 
		// TODO have the database define the id, instead of the server or find another mechanisms to generate the object
		ObjectId objId = obej.getObjectId();
		
		if (objId == null || objId.getInternalObjectId() == null || obej.getType()== null 
	|| obej.getAlias() ==null || obej.getAlias() == null || obej.getCreationTimestamp() ==null
	|| obej.getLocation() ==null || obej.getCreatedBy() ==null)
			throw new RuntimeException("could not create a object without all the valid details");
		
	obej.getObjectId().setSuperapp(this.superappName);
	obej.getObjectId().setInternalObjectId(System.currentTimeMillis()+"");
	obej.setCreationTimestamp(new Date());
	ObjectPrimaryKeyId id = new ObjectPrimaryKeyId(obej.getObjectId().getSuperapp(), obej.getObjectId().getInternalObjectId());
	Optional<ObjectEntity> newObject = this.objectCrud.findById(id);
	if (newObject.isPresent())
		throw new RuntimeException("User already exist");
	this.objectCrud.save(this.convertor.toEntity(obej));
		return obej;
	}

	@Override
	public ObjectBoundary updateObject(String superapp,String internalObjectId , ObjectBoundary update) {

		ObjectPrimaryKeyId id = new ObjectPrimaryKeyId(superapp, internalObjectId);
		ObjectEntity existing = 
				this.objectCrud
				.findById(id)
				.orElseThrow(()->new RuntimeException("could not update User with id: " + id + " since it does not exist"));
		// update entity
				if (update.getType() != null) {
					existing.setType(update.getType());
				}
				
				if (update.getAlias() != null) {
					existing.setAlias(update.getAlias());
				}
				
				if (update.getActive() != null) {
					existing.setActive(update.getActive());
				}
				//creation time stamp update ?
				if (update.getLocation().getLat() != null) {
					existing.setLat(update.getLocation().getLat());
				}
				if (update.getLocation().getLng()!= null) {
					existing.setLng(update.getLocation().getLng());
				}
				// created by ?? 
				if (update.getObjectDetails() != null) {
					existing.setObjectDetails(update.getObjectDetails());
				}
				// save (UPDATE) entity to database if user was indeed updated
				this.objectCrud.save(existing);
				
				return this.convertor.toBoundary(existing);
				
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects() {
		List<ObjectEntity> entities = this.objectCrud.findAll();
		List<ObjectBoundary> rv = new ArrayList<>();
		for (ObjectEntity e : entities) {
			rv.add(this.convertor.toBoundary(e));
			
		}
		return rv;
	}

	@Override
	public void deleteAllObject() {
		 this.objectCrud.deleteAll();
		
	}

	@Override
	public ObjectBoundary getSpecsificObject(String ObjectSuperapp, String internalObjectId) {
		ObjectPrimaryKeyId pkid = new ObjectPrimaryKeyId(ObjectSuperapp, internalObjectId);
	Optional<ObjectEntity> entity =this.objectCrud.findById(pkid);
	if (!entity.isPresent())
	{
		throw new RuntimeException("Object not exist");
	}
	
	return this.convertor.toBoundary(entity.get());

}

	@Override
	public void bindObjects(ObjectEntity parent, ObjectEntity child) {
		ObjectPrimaryKeyId parent_pkid = new ObjectPrimaryKeyId(parent.getSuperapp(), parent.getInternalObjectId());
		ObjectPrimaryKeyId child_pkid = new ObjectPrimaryKeyId(child.getSuperapp(), child.getInternalObjectId());
		
		 Optional<ObjectEntity> parentEntity =this.objectCrud.findById(parent_pkid);
		 Optional<ObjectEntity> childEntity =this.objectCrud.findById(child_pkid);
		 
		 // store the id in the parent child bind map 
		 parentEntity.get().addChild(child_pkid.getInternalObjectId());
		 childEntity.get().addParent(parent_pkid.getInternalObjectId());
	}

	@Override
	public void unbindObjects(ObjectEntity parent, ObjectEntity child) {
		
		ObjectPrimaryKeyId parent_pkid = new ObjectPrimaryKeyId(parent.getSuperapp(), parent.getInternalObjectId());
		ObjectPrimaryKeyId child_pkid = new ObjectPrimaryKeyId(child.getSuperapp(), child.getInternalObjectId());
		
		 Optional<ObjectEntity> parentEntity =this.objectCrud.findById(parent_pkid);
		 Optional<ObjectEntity> childEntity =this.objectCrud.findById(child_pkid);
		 
		 // remove the id in the parent child bind map 
		 childEntity.get().RemoveParent(parent_pkid.getInternalObjectId());
		 parentEntity.get().RemoveChild(child_pkid.getInternalObjectId());
	}
}
