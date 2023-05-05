package superapp.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.converters.ObjectConvertor;
import superapp.dal.ObjectCrud;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;
import superapp.exceptions.ObjectBindingException;
import superapp.exceptions.ResourceAlreadyExistException;
import superapp.exceptions.ResourceNotFoundException;

@Service // spring create an instance from this class so we can use it
public class ObjectServicesRDB implements ObjectServiceBinding {
	private ObjectCrud objectCrud;
	private ObjectConvertor converter;
	private String superappName;

	@Autowired // put in the parameter an instance
	public void setObjectCrud(ObjectCrud oc) {
		this.objectCrud = oc;
	}

	@Autowired
	public void setObjectConverter(ObjectConvertor convertor) {
		this.converter = convertor;
	}

	// have spring inject a configuration to this method
	@Value("${spring.application.name:defaultValue}")
	public void setSuperappName(String superappName) {
		this.superappName = superappName;
	}

	@Override
	@Transactional // will be atomic - part of the transaction
	public ObjectBoundary createObject(ObjectBoundary obj) {
		// the server need to make object internal id ?
		// TODO have the database define the id, instead of the server or find another
		// mechanisms to generate the object
		SuperAppObjectIdBoundary objId = new SuperAppObjectIdBoundary();
		System.err.println("hello"+ obj.toString());
		if (obj.getType() == null || obj.getAlias() == null|| obj.getAlias() == null || obj.getLocation() == null
				|| obj.getCreatedBy() == null)
			throw new RuntimeException("could not create a object without all the valid details");
		obj.setObjectId(objId);
		obj.getObjectId().setSuperapp(this.superappName);
		obj.getObjectId().setInternalObjectId(UUID.randomUUID().toString());
		obj.setCreationTimestamp(new Date());
		ObjectPrimaryKeyId id = new ObjectPrimaryKeyId(obj.getObjectId().getSuperapp(),
				obj.getObjectId().getInternalObjectId());
		Optional<ObjectEntity> newObject = this.objectCrud.findById(id);
		if (newObject.isPresent())
			throw new ResourceAlreadyExistException(id, "create object");
		this.objectCrud.save(this.converter.toEntity(obj));
		return obj;
	}

	@Override
	public ObjectBoundary updateObject(String superapp, String internalObjectId, ObjectBoundary update) {

		ObjectPrimaryKeyId id = new ObjectPrimaryKeyId(superapp, internalObjectId);
		ObjectEntity existing = this.objectCrud.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(id, "update object"));
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
		// creation time stamp update ?
		if (update.getLocation().getLat() != null) {
			existing.setLat(update.getLocation().getLat());
		}
		if (update.getLocation().getLng() != null) {
			existing.setLng(update.getLocation().getLng());
		}
		// created by ??
		if (update.getObjectDetails() != null) {
			existing.setObjectDetails(update.getObjectDetails());
		}
		// save (UPDATE) entity to database if user was indeed updated
		this.objectCrud.save(existing);

		return this.converter.toBoundary(existing);

	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects() {
		List<ObjectEntity> entities = this.objectCrud.findAll();
		List<ObjectBoundary> rv = new ArrayList<>();
		for (ObjectEntity e : entities) {
			rv.add(this.converter.toBoundary(e));

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
		Optional<ObjectEntity> entity = this.objectCrud.findById(pkid);
		if (!entity.isPresent()) {
			throw new ResourceNotFoundException(pkid, "find object");
		}

		return this.converter.toBoundary(entity.get());

	}

	@Override
	public void bindObjectToParent(String superapp, String internalObjectId, SuperAppObjectIdBoundary childId) {
		ObjectPrimaryKeyId parentPk = new ObjectPrimaryKeyId(superapp, internalObjectId);
		ObjectPrimaryKeyId childPk = converter.idToEntity(childId);

		ObjectEntity parent = this.objectCrud.findById(parentPk)
				.orElseThrow(() -> new ResourceNotFoundException(parentPk, "find parent object"));

		ObjectEntity child = this.objectCrud.findById(childPk)
				.orElseThrow(() -> new ResourceNotFoundException(parentPk, "find child object"));

		if (!child.addParent(parent) || !parent.addChild(child))
			throw new ObjectBindingException("could bind parent object:" + parentPk + " to child object: " + childPk);

		this.objectCrud.save(child);
		this.objectCrud.save(parent);
	}

	@Override
	public List<ObjectBoundary> getAllChildrenOfObject(String superapp, String internalObjectId) {
		ObjectPrimaryKeyId parentPk = new ObjectPrimaryKeyId(superapp, internalObjectId);

		ObjectEntity parent = this.objectCrud.findById(parentPk)
				.orElseThrow(() -> new ResourceNotFoundException(parentPk, "find children objects"));

		List<ObjectBoundary> rv = new ArrayList<>();

		for (ObjectEntity entity : parent.getChildren()) {
			rv.add(this.converter.toBoundary(entity));
		}

		return rv;
	}

	// TODO: finish the function
	@Override
	public List<ObjectBoundary> getAllParentsOfObject(String superapp, String internalObjectId) {
		ObjectPrimaryKeyId childPk = new ObjectPrimaryKeyId(superapp, internalObjectId);

		ObjectEntity child = this.objectCrud.findById(childPk)
				.orElseThrow(() -> new ResourceNotFoundException(childPk, "find parents objects"));

		List<ObjectBoundary> rv = new ArrayList<>();

		for (ObjectEntity entity : child.getParents()) {
			rv.add(this.converter.toBoundary(entity));
		}

		return rv;
	}
}
