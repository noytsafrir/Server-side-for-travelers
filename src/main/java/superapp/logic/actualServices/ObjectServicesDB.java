package superapp.logic.actualServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.boundaries.object.Location;
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

	private Log logger = LogFactory.getLog(MiniAppCommandServiceDB.class);

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
	public ObjectBoundary createObject(ObjectBoundary obj) {
		SuperAppObjectIdBoundary objId = new SuperAppObjectIdBoundary();
		if (obj.getType() == null || obj.getType().isBlank() || obj.getAlias() == null || obj.getAlias().isBlank()
				|| obj.getLocation() == null || obj.getCreatedBy() == null) {
			logger.warn("Invalid input for create object: " + obj);
			throw new InvalidInputException(obj, "create object");
		}

		obj.setObjectId(objId);
		obj.getObjectId().setSuperapp(this.superappName);
		obj.getObjectId().setInternalObjectId(UUID.randomUUID().toString());

		logger.trace("Creating object with id: " + obj.getObjectId().getInternalObjectId());
		obj.setCreationTimestamp(new Date());

		ObjectPrimaryKeyId id = new ObjectPrimaryKeyId(obj.getObjectId().getSuperapp(),
				obj.getObjectId().getInternalObjectId());
		Optional<ObjectEntity> newObject = this.objectCrud.findById(id);
		if (newObject.isPresent()) {
			logger.warn("Object already exist with id: " + obj.getObjectId().getInternalObjectId());
			throw new ResourceAlreadyExistException(id, "create object");
		}

		this.objectCrud.save(this.converter.toEntity(obj));
		logger.debug("Object created successfully with id: " + obj.getObjectId().getInternalObjectId());
		return obj;
	}

	@Override
	public ObjectBoundary updateObject(String superapp, String internalObjectId, ObjectBoundary update,
									   String userSuperapp, String email) {

		logger.trace("Updating object with id: " + internalObjectId);

		// User role check
		UserPrimaryKeyId user = new UserPrimaryKeyId(userSuperapp, email);
		if (!isValidUserCredentials(user, UserRole.SUPERAPP_USER, this.users)) {
			logger.warn("User: " + user.getEmail() + " is not authorized to update object: " + internalObjectId);
			throw new ForbbidenException(user.getEmail(), "update object");
		}

		// Find object in DB
		ObjectPrimaryKeyId id = new ObjectPrimaryKeyId(superapp, internalObjectId);
		ObjectEntity existing = this.objectCrud.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(id, "update object"));
		logger.trace("Object found with id: " + internalObjectId);

		// Object entity update
		if (update.getType() != null && !update.getType().isBlank()) {
			logger.trace("Updating object type to: " + update.getType());
			existing.setType(update.getType());
		}
		if (update.getAlias() != null && !update.getAlias().isBlank()) {
			logger.trace("Updating object alias to: " + update.getAlias());
			existing.setAlias(update.getAlias());
		}
		if (update.getActive() != null) {
			logger.trace("Updating object active to: " + update.getActive());
			existing.setActive(update.getActive());
		}

		if (update.getLocation() != null && update.getLocation().getLat() != null
				&& update.getLocation().getLng() != null) {
			logger.trace("Updating object location to: " + update.getLocation());
			if(existing.getLocation() == null)
				existing.setLocation(new Location());
			existing.getLocation().setLat(update.getLocation().getLat());
			existing.getLocation().setLng(update.getLocation().getLng());
		}
		if (update.getObjectDetails() != null) {
			logger.trace("Updating object details to: " + update.getObjectDetails());
			existing.setObjectDetails(update.getObjectDetails());
		}

		// Object entity save to DB
		this.objectCrud.save(existing);
		logger.debug("Object updated successfully with id: " + internalObjectId);
		// Return object as boundary
		return this.converter.toBoundary(existing);
	}

	@Override
	public ObjectBoundary getSpecificObject(String ObjectSuperapp, String internalObjectId, String userSuperapp,
											String email) {

		boolean isMiniappUser, isSuperappUser;
		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, email), users);
		isMiniappUser = user.getRole().equals(UserRole.MINIAPP_USER.toString());
		isSuperappUser = user.getRole().equals(UserRole.SUPERAPP_USER.toString());

		logger.trace("Getting object with id: " + internalObjectId);

		if (!isMiniappUser && !isSuperappUser) {
			logger.warn("User: " + user.getUserId().getEmail() + " is not authorized to find object: " + internalObjectId);
			throw new ForbbidenException(user.getUserId().getEmail(), "find object");
		}

		ObjectPrimaryKeyId pkid = new ObjectPrimaryKeyId(ObjectSuperapp, internalObjectId);
		ObjectEntity entity = this.objectCrud.findById(pkid)
				.orElseThrow(() -> new ResourceNotFoundException(pkid, "find object"));
		logger.trace("Object found with id: " + internalObjectId);

		if (isMiniappUser && !entity.getActive()) {
			logger.warn("Object: " + internalObjectId + " not found");
			throw new ResourceNotFoundException(pkid, "find object");
		}

		logger.trace("Object returned successfully with id: " + internalObjectId);
		return this.converter.toBoundary(entity);
	}

	@Override
	public List<ObjectBoundary> getAllObjects(String userSuperapp, String email, int size, int page) {
		boolean isMiniappUser, isSuperappUser;
		List<ObjectBoundary> rv = new ArrayList<>();

		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, email), users);
		isMiniappUser = user.getRole().equals(UserRole.MINIAPP_USER.toString());
		isSuperappUser = user.getRole().equals(UserRole.SUPERAPP_USER.toString());

		logger.trace("Getting all objects");

		if (isSuperappUser) {
			rv = this.objectCrud.findAll(PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else if (isMiniappUser) {
			rv = this.objectCrud
					.findAllByActive(true, PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else {
			logger.warn("User: " + user.getUserId().getEmail() + " is not authorized to get all objects");
			throw new ForbbidenException(user.getUserId().getEmail(), "get all objects");
		}

		logger.trace("All objects returned successfully (" + rv.size() + " objects)");
		return rv;
	}


	@Override
	public void bindObjectToParent(String superapp, String internalObjectId, SuperAppObjectIdBoundary childId,
								   String userSuperapp, String email) {

		logger.trace("Binding object with id: " + internalObjectId + " to parent object with id: " + childId);

		UserPrimaryKeyId user = new UserPrimaryKeyId(userSuperapp, email);
		if (!isValidUserCredentials(user, UserRole.SUPERAPP_USER, this.users)) {
			logger.warn("User: " + user.getEmail() + " is not authorized to bind objects");
			throw new ForbbidenException(user.getEmail(), "bind objects");
		}

		ObjectPrimaryKeyId parentPk = new ObjectPrimaryKeyId(superapp, internalObjectId);
		ObjectPrimaryKeyId childPk = converter.idToEntity(childId);

		ObjectEntity parent = this.objectCrud.findById(parentPk)
				.orElseThrow(() -> new ResourceNotFoundException(parentPk, "find parent object"));
		logger.trace("Parent object: " + parentPk + " found successfully");

		ObjectEntity child = this.objectCrud.findById(childPk)
				.orElseThrow(() -> new ResourceNotFoundException(parentPk, "find child object"));
		logger.trace("Child object: " + childPk + " found successfully");

		if (!child.addParent(parent) || !parent.addChild(child)) {
			logger.warn("Could not bind parent object: " + parentPk + " to child object: " + childPk);
			throw new ObjectBindingException("could bind parent object:" + parentPk + " to child object: " + childPk);
		}

		this.objectCrud.save(child);
		this.objectCrud.save(parent);
		logger.debug("Object: " + childPk + " binded successfully to object: " + parentPk);
	}

	@Override
	public List<ObjectBoundary> getAllChildrenOfObject(String superapp, String internalObjectId, String userSuperapp,
													   String email, int size, int page) {

		boolean isMiniappUser, isSuperappUser;
		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, email), users);
		isMiniappUser = user.getRole().equals(UserRole.MINIAPP_USER.toString());
		isSuperappUser = user.getRole().equals(UserRole.SUPERAPP_USER.toString());

		logger.trace("Getting all children of object with id: " + internalObjectId);

		if (!isMiniappUser && !isSuperappUser) {
			logger.warn("User: " + user.getUserId().getEmail() + " is not authorized to find children objects");
			throw new ForbbidenException(user.getUserId().getEmail(), "find children objects");
		}

		ObjectPrimaryKeyId parentPk = new ObjectPrimaryKeyId(superapp, internalObjectId);

		ObjectEntity parent = this.objectCrud.findById(parentPk)
				.orElseThrow(() -> new ResourceNotFoundException(parentPk, "find parent object"));
		logger.trace("Parent object: " + parentPk + " found successfully");

		List<ObjectEntity> children = new ArrayList<>();

		if (isSuperappUser) {
			children = this.objectCrud.findAllByParents_objectId(parent,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		} else if (isMiniappUser) {
			if (!parent.getActive())
				throw new ResourceNotFoundException(parentPk, "find parent object");
			children = this.objectCrud.findAllByActiveIsTrueAndParents_objectId(parent,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		}

		logger.trace("Children objects of parent object: " + parentPk + " found successfully (" + children.size() + " objects)");
		return children
				.stream()
				.map(this.converter::toBoundary)
				.toList();
	}

	@Override
	public List<ObjectBoundary> getAllParentsOfObject(String superapp, String internalObjectId, String userSuperapp,
													  String email, int size, int page) {

		boolean isMiniappUser, isSuperappUser;
		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, email), users);
		isMiniappUser = user.getRole().equals(UserRole.MINIAPP_USER.toString());
		isSuperappUser = user.getRole().equals(UserRole.SUPERAPP_USER.toString());

		logger.trace("Getting all parents of object with id: " + internalObjectId);

		if (!isMiniappUser && !isSuperappUser) {
			logger.warn("User: " + user.getUserId().getEmail() + " is not authorized to find parents objects");
			throw new ForbbidenException(user.getUserId().getEmail(), "find parents objects");
		}

		ObjectPrimaryKeyId childPk = new ObjectPrimaryKeyId(superapp, internalObjectId);
		ObjectEntity child = this.objectCrud.findById(childPk)
				.orElseThrow(() -> new ResourceNotFoundException(childPk, "find child object"));
		logger.trace("Child object: " + childPk + " found successfully");

		List<ObjectEntity> parents = new ArrayList<>();

		if (isMiniappUser && !child.getActive())  {
			logger.warn("Object: " + internalObjectId + " not found");
			throw new ResourceNotFoundException(childPk, "find child object");
		}
		else if (isSuperappUser) {
			parents = this.objectCrud.findAllByChildren_objectId(child,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		} else if (isMiniappUser) {
			if (!child.getActive()) {
				logger.warn("Object: " + internalObjectId + " not found");
				throw new ResourceNotFoundException(childPk, "find child object");
			}
			parents = this.objectCrud.findAllByActiveIsTrueAndChildren_objectId(child,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		}

		logger.trace("Parent objects of child object: " + childPk + " found successfully (" + parents.size() + " objects)");
		return parents.stream().map(this.converter::toBoundary).toList();
	}

	@Override
	public List<ObjectBoundary> getObjectsByType(String userSuperapp, String email, String type, int size, int page) {
		boolean isMiniappUser, isSuperappUser;
		List<ObjectBoundary> objects = new ArrayList<>();

		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, email), users);
		isMiniappUser = user.getRole().equals(UserRole.MINIAPP_USER.toString());
		isSuperappUser = user.getRole().equals(UserRole.SUPERAPP_USER.toString());

		logger.trace("Getting all objects of type: " + type);

		if (!isMiniappUser && !isSuperappUser) {
			logger.warn("User: " + user.getUserId().getEmail() + " is not authorized to find objects by type");
			throw new ForbbidenException(user.getUserId().getEmail(), "find objects by type");
		}

		if (isSuperappUser) {
			objects = objectCrud
					.findAllByType(type, PageRequest.of(page, size, Direction.DESC, "createdTimestamp", "objectId"))
					.stream()
					.map(this.converter::toBoundary)
					.toList();
		} else if (isMiniappUser) {
			objects = objectCrud
					.findAllByActiveIsTrueAndType(type, PageRequest.of(page, size, Direction.DESC, "createdTimestamp", "objectId"))
					.stream()
					.map(this.converter::toBoundary)
					.toList();
		}

		logger.trace("Objects of type: " + type + " found successfully (" + objects.size() + " objects)");
		return objects;
	}

	@Override
	public List<ObjectBoundary> getObjectsByAlias(String userSuperapp, String email, String alias, int size, int page) {
		boolean isMiniappUser, isSuperappUser;
		List<ObjectBoundary> objects = new ArrayList<>();

		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, email), users);
		isMiniappUser = user.getRole().equals(UserRole.MINIAPP_USER.toString());
		isSuperappUser = user.getRole().equals(UserRole.SUPERAPP_USER.toString());

		logger.trace("Getting all objects of alias: " + alias);

		if (!isMiniappUser && !isSuperappUser) {
			logger.warn("User: " + user.getUserId().getEmail() + " is not authorized to find objects by alias");
			throw new ForbbidenException(user.getUserId().getEmail(), "find objects by alias");
		}

		if (isSuperappUser)
			objects = objectCrud
					.findAllByAlias(alias, PageRequest.of(page, size, Direction.DESC, "createdTimestemp", "objectId"))
					.stream()
					.map(this.converter::toBoundary)
					.toList();
		else if (isMiniappUser)
			objects = objectCrud
					.findAllByActiveIsTrueAndAlias(alias, PageRequest.of(page, size, Direction.DESC, "createdTimestemp", "objectId"))
					.stream()
					.map(this.converter::toBoundary)
					.toList();

		logger.trace("Objects of alias: " + alias + " found successfully (" + objects.size() + " objects)");
		return objects;
	}

	@Override
	public List<ObjectBoundary> getObjectsByLocationSearch(String userSuperapp, String userEmail,
														   double lat, double lng, double distance, String distanceUnits, int size, int page) {
		boolean isMiniappUser, isSuperappUser;
		Point location = new Point(lat, lng);
		Distance maxDistance;
		List<ObjectBoundary> objects = new ArrayList<>();
		UserEntity user = getUser(new UserPrimaryKeyId(userSuperapp, userEmail), users);
		isMiniappUser = user.getRole().equals(UserRole.MINIAPP_USER.toString());
		isSuperappUser = user.getRole().equals(UserRole.SUPERAPP_USER.toString());

		logger.trace("Getting all objects by location search with distance: " + distance + " " + distanceUnits + " from location: " + location);

		if(!isMiniappUser && !isSuperappUser) {
			logger.warn("User: " + user.getUserId().getEmail() + " is not authorized to find objects by location");
			throw new ForbbidenException(user.getUserId().getEmail(), "find objects by location");
		}

		if (distanceUnits.equalsIgnoreCase("KILOMETERS")) {
			maxDistance = new Distance(distance, Metrics.KILOMETERS);
		} else if (distanceUnits.equalsIgnoreCase("MILES")) {
			maxDistance = new Distance(distance, Metrics.MILES);
		} else if (distanceUnits.equalsIgnoreCase("NEUTRAL")) {
			maxDistance = new Distance(distance, Metrics.NEUTRAL);
		} else {
			logger.warn("User: " + user.getUserId().getEmail() + " sent invalid distance units: " + distanceUnits);
			throw new InvalidInputException("could not process distance units : "+ distanceUnits); // Bad convert word
		}

		if (isSuperappUser)
			objects = objectCrud.findByLocationNear(location ,maxDistance,
							PageRequest.of(page, size, Direction.DESC, "createdTimestemp", "objectId"))
					.stream()
					.map(this.converter::toBoundary)
					.toList();

		else if(isMiniappUser)
			objects = objectCrud.findByActiveIsTrueAndLocationNear(location ,maxDistance,
							PageRequest.of(page, size, Direction.DESC, "createdTimestemp", "objectId"))
					.stream()
					.map(this.converter::toBoundary)
					.toList();

		logger.trace("Objects by location search with distance: " + distance + " " + distanceUnits + " from location: " + location + " found successfully (" + objects.size() + " objects)");
		return objects;
	}



	@Override
	public void deleteAllObject(String userSuperapp, String email) {
		this.logger.info("delete all objects by user: " + email);
		UserPrimaryKeyId user = new UserPrimaryKeyId(userSuperapp, email);
		if (!isValidUserCredentials(user, UserRole.ADMIN, this.users)) {
			this.logger.warn("User: " + user.getEmail() + " is not authorized to delete all objects");
			throw new ForbbidenException(user.getEmail(), "delete all objects");
		}
		this.objectCrud.deleteAll();
		logger.info("All objects deleted successfully");
	}
	@Override
	public void deleteObjectsByType(String type) {
		this.logger.info("delete all objects by type: " + type);
		this.objectCrud.deleteAllByType(type);
		logger.info("All objects of type: " + type + " deleted successfully");
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
