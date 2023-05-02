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
import superapp.converters.ObjectConvertor;
import superapp.dal.ObjectCrud;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;
import superapp.exceptions.ObjectBindingException;
import superapp.exceptions.ObjectNotFoundException;

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

	// ???
	@PostConstruct // after its build but before
	public void init() {
		System.err.println("********** spring.application.name = " + this.superappName);
	}

	@Override
	@Transactional // will be atomic - part of the transaction
	public ObjectBoundary createObject(ObjectBoundary obej) {
		// the server need to make object internal id ?
		// TODO have the database define the id, instead of the server or find another
		// mechanisms to generate the object
		ObjectId objId = obej.getObjectId();

		if (objId == null || objId.getInternalObjectId() == null || obej.getType() == null || obej.getAlias() == null
				|| obej.getAlias() == null || obej.getCreationTimestamp() == null || obej.getLocation() == null
				|| obej.getCreatedBy() == null)
			throw new RuntimeException("could not create a object without all the valid details");

		obej.getObjectId().setSuperapp(this.superappName);
		obej.getObjectId().setInternalObjectId(System.currentTimeMillis() + "");
		obej.setCreationTimestamp(new Date());
		ObjectPrimaryKeyId id = new ObjectPrimaryKeyId(obej.getObjectId().getSuperapp(),
				obej.getObjectId().getInternalObjectId());
		Optional<ObjectEntity> newObject = this.objectCrud.findById(id);
		if (newObject.isPresent())
			throw new RuntimeException("User already exist");
		this.objectCrud.save(this.converter.toEntity(obej));
		return obej;
	}

	@Override
	public ObjectBoundary updateObject(String superapp, String internalObjectId, ObjectBoundary update) {

		ObjectPrimaryKeyId id = new ObjectPrimaryKeyId(superapp, internalObjectId);
		ObjectEntity existing = this.objectCrud.findById(id).orElseThrow(
				() -> new RuntimeException("could not update User with id: " + id + " since it does not exist"));
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
			throw new RuntimeException("Object not exist");
		}

		return this.converter.toBoundary(entity.get());

	}

	@Override
	public void bindObjectToParent(String superapp, String internalObjectId, ObjectId childId) {
		ObjectPrimaryKeyId parentPk = new ObjectPrimaryKeyId(superapp, internalObjectId);
		ObjectPrimaryKeyId childPk = converter.idToEntity(childId);

		ObjectEntity parent = this.objectCrud.findById(parentPk)
				.orElseThrow(() -> new ObjectNotFoundException("could not find parent object by id: " + parentPk));

		ObjectEntity child = this.objectCrud.findById(childPk)
				.orElseThrow(() -> new ObjectNotFoundException("could not find child object by id: " + childPk));

		if (!child.addParent(parent) || !parent.addChild(child))
			throw new ObjectBindingException("could bind parent object:" + parentPk + " to child object: " + childPk);

		this.objectCrud.save(child);
		this.objectCrud.save(parent);
	}

	@Override
	public List<ObjectBoundary> getAllChildrenOfObject(String superapp, String internalObjectId) {
		ObjectPrimaryKeyId parentPk = new ObjectPrimaryKeyId(superapp, internalObjectId);

		ObjectEntity parent = this.objectCrud.findById(parentPk)
				.orElseThrow(() -> new ObjectNotFoundException("could not find parent object by id: " + parentPk));

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
				.orElseThrow(() -> new ObjectNotFoundException("could not find child object by id: " + childPk));

		List<ObjectBoundary> rv = new ArrayList<>();

		for (ObjectEntity entity : child.getParents()) {
			rv.add(this.converter.toBoundary(entity));
		}

		return rv;
	}
}
