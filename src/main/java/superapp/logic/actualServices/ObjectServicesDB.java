package superapp.logic.actualServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.converters.ObjectConvertor;
import superapp.dal.ObjectCrud;
import superapp.dal.UserCrud;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;
import superapp.data.UserEntity;
import superapp.data.UserPrimaryKeyId;
import superapp.data.UserRole;
import superapp.exceptions.DeprecatedException;
import superapp.exceptions.ForbbidenException;
import superapp.exceptions.InvalidInputException;
import superapp.exceptions.ObjectBindingException;
import superapp.exceptions.ResourceAlreadyExistException;
import superapp.exceptions.ResourceNotFoundException;
import superapp.logic.GeneralService;
import superapp.logic.ObjectServiceWithPagination;

@Service // spring create an instance from this class so we can use it
public class ObjectServicesDB extends GeneralService implements ObjectServiceWithPagination {
	private ObjectCrud objectCrud;
	private UserCrud users;
	private ObjectConvertor converter;

	@Autowired // put in the parameter an instance
	public void setObjectCrud(ObjectCrud oc) {
		this.objectCrud = oc;
	}

	@Autowired
	public void setUsers(UserCrud users) {
		this.users = users;
	}

	@Autowired
	public void setObjectConverter(ObjectConvertor convertor) {
		this.converter = convertor;
	}

	@Override
	@Transactional // will be atomic - part of the transaction
	public ObjectBoundary createObject(ObjectBoundary obj) {
		SuperAppObjectIdBoundary objId = new SuperAppObjectIdBoundary();
		if (obj.getType() == null || obj.getType().isBlank() || obj.getAlias() == null || obj.getAlias().isBlank()
				|| obj.getLocation() == null || obj.getCreatedBy() == null)
			throw new InvalidInputException(obj, "create object");

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
	public ObjectBoundary updateObject(String superapp, String internalObjectId, ObjectBoundary update,
			String userSuperapp, String email) {

		// User role check
		UserPrimaryKeyId user = new UserPrimaryKeyId(userSuperapp, email);
		if (!isValidUserCredentials(user, UserRole.SUPERAPP_USER, this.users))
			throw new ForbbidenException(user.getEmail(), "update object");

		// Find object in DB
		ObjectPrimaryKeyId id = new ObjectPrimaryKeyId(superapp, internalObjectId);
		ObjectEntity existing = this.objectCrud.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(id, "update object"));

		// Object entity update
		if (update.getType() != null && !update.getType().isBlank()) {
			existing.setType(update.getType());
		}
		if (update.getAlias() != null && !update.getAlias().isBlank()) {
			existing.setAlias(update.getAlias());
		}
		if (update.getActive() != null) {
			existing.setActive(update.getActive());
		}
		if (update.getLocation() != null && update.getLocation().getLat() != null
				&& update.getLocation().getLng() != null) {
			existing.setLat(update.getLocation().getLat());
			existing.setLng(update.getLocation().getLng());
		}
		if (update.getObjectDetails() != null) {
			existing.setObjectDetails(update.getObjectDetails());
		}

		// Object entity save to DB
		this.objectCrud.save(existing);

		// Return object as boundary
		return this.converter.toBoundary(existing);
	}
	
	@Override
	public ObjectBoundary getSpecsificObject(String ObjectSuperapp, String internalObjectId, String userSuperapp,
			String email) {
		
		boolean isMiniappUser;
		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, email), users);
		isMiniappUser = user.getRole().equals(UserRole.MINIAPP_USER.toString());
		
		if (!isMiniappUser && !user.getRole().equals(UserRole.SUPERAPP_USER.toString()))
			throw new ForbbidenException(user.getUserId().getEmail(), "find object");
		
		ObjectPrimaryKeyId pkid = new ObjectPrimaryKeyId(ObjectSuperapp, internalObjectId);
		ObjectEntity entity = this.objectCrud.findById(pkid)
				.orElseThrow(() -> new ResourceNotFoundException(internalObjectId, "find object"));
		
		if (isMiniappUser && !entity.getActive())
			throw new ResourceNotFoundException(internalObjectId, "find object");
		
		return this.converter.toBoundary(entity);
	}

	@Override
	public List<ObjectBoundary> getAllObjects(String userSuperapp, String email, int size, int page) {
		List<ObjectBoundary> rv;

		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, email), users);
		if (user.getRole().equals(UserRole.SUPERAPP_USER.toString())) {
			rv = this.objectCrud.findAll(PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else if (user.getRole().equals(UserRole.MINIAPP_USER.toString())) {
			rv = this.objectCrud
					.findAllByActive(true, PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else {
			throw new ForbbidenException(user.getUserId().getEmail(), "get all objects");
		}

		return rv;
	}


	@Override
	public void bindObjectToParent(String superapp, String internalObjectId, SuperAppObjectIdBoundary childId,
			String userSuperapp, String email) {

		UserPrimaryKeyId user = new UserPrimaryKeyId(userSuperapp, email);
		if (!isValidUserCredentials(user, UserRole.SUPERAPP_USER, this.users))
			throw new ForbbidenException(user.getEmail(), "bind objects");

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
	public List<ObjectBoundary> getAllChildrenOfObject(String superapp, String internalObjectId, String userSuperapp,
			String email, int size, int page) {

		boolean isMiniappUser;
		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, email), users);
		isMiniappUser = user.getRole().equals(UserRole.MINIAPP_USER.toString());

		if (isMiniappUser && !user.getRole().equals(UserRole.SUPERAPP_USER.toString()))
			throw new ForbbidenException(user.getUserId().getEmail(), "find children objects");

		ObjectPrimaryKeyId parentPk = new ObjectPrimaryKeyId(superapp, internalObjectId);

		ObjectEntity parent = this.objectCrud.findById(parentPk)
				.orElseThrow(() -> new ResourceNotFoundException(parentPk, "find parent object"));

		List<ObjectEntity> children = new ArrayList<>();

		if (!user.getRole().equals(UserRole.SUPERAPP_USER.toString())) {
			children = this.objectCrud.findChildrenByObjectId(parentPk,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		} else if (isMiniappUser) {
			if (!parent.getActive())
				throw new ResourceNotFoundException(internalObjectId, "find parent object");

			children = this.objectCrud.findByObjectIdAndChildren_Active(parentPk, true,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		}

		return children.stream().map(this.converter::toBoundary).toList();
	}

	@Override
	public List<ObjectBoundary> getAllParentsOfObject(String superapp, String internalObjectId, String userSuperapp,
			String email, int size, int page) {

		boolean isMiniappUser;
		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, email), users);
		isMiniappUser = user.getRole().equals(UserRole.MINIAPP_USER.toString());

		if (isMiniappUser && !user.getRole().equals(UserRole.SUPERAPP_USER.toString()))
			throw new ForbbidenException(user.getUserId().getEmail(), "find parents objects");

		ObjectPrimaryKeyId childPk = new ObjectPrimaryKeyId(superapp, internalObjectId);

		ObjectEntity child = this.objectCrud.findById(childPk)
				.orElseThrow(() -> new ResourceNotFoundException(childPk, "find child object"));

		List<ObjectEntity> parents = new ArrayList<>();

		if (!user.getRole().equals(UserRole.SUPERAPP_USER.toString())) {
			parents = this.objectCrud.findParentsByObjectId(childPk,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		} else if (isMiniappUser) {
			if (!child.getActive())
				throw new ResourceNotFoundException(internalObjectId, "find child object");

			parents = this.objectCrud.findByObjectIdAndParents_Active(childPk, true,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		}

		return parents.stream().map(this.converter::toBoundary).toList();
	}

	@Override
	public void deleteAllObject(String userSuperapp, String email) {
		UserPrimaryKeyId user = new UserPrimaryKeyId(userSuperapp, email);
		if (!isValidUserCredentials(user, UserRole.ADMIN, this.users))
			throw new ForbbidenException(user.getEmail(), "delete all objects");

		this.objectCrud.deleteAll();
	}

	public List<ObjectEntity> filterActiveObjects(List<ObjectEntity> objects) {
		return objects.stream().filter(obj -> obj.getActive()).toList();
	}

	@Deprecated
	@Override
	public ObjectBoundary updateObject(String superapp, String internalObjectId, ObjectBoundary update) {
		throw new DeprecatedException(LocalDate.of(2023, 5, 20));
	}

	@Deprecated
	@Override
	public ObjectBoundary getSpecsificObject(String ObjectSuperapp, String internalObjectId) {
		throw new DeprecatedException(LocalDate.of(2023, 5, 20));
	}

	@Deprecated
	@Override
	public List<ObjectBoundary> getAllObjects() {
		throw new DeprecatedException(LocalDate.of(2023, 5, 20));
	}

	@Deprecated
	@Override
	public void deleteAllObject() {
		throw new DeprecatedException(LocalDate.of(2023, 5, 20));
	}

	@Deprecated
	@Override
	public void bindObjectToParent(String superapp, String internalObjectId, SuperAppObjectIdBoundary childId) {
		throw new DeprecatedException(LocalDate.of(2023, 5, 20));
	}

	@Deprecated
	@Override
	public List<ObjectBoundary> getAllChildrenOfObject(String superapp, String internalObjectId) {
		throw new DeprecatedException(LocalDate.of(2023, 5, 20));
	}

	@Deprecated
	@Override
	public List<ObjectBoundary> getAllParentsOfObject(String superapp, String internalObjectId) {
		throw new DeprecatedException(LocalDate.of(2023, 5, 20));
	}
}
