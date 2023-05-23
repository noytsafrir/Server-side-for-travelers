package superapp.converters;


import org.springframework.stereotype.Component;


import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;


@Component //create an instance from this class 
public class ObjectConvertor {

	public ObjectBoundary toBoundary(ObjectEntity entity)
	{
		ObjectBoundary boundary = new ObjectBoundary();
		
		SuperAppObjectIdBoundary objectId = new SuperAppObjectIdBoundary();
		objectId.setSuperapp(entity.getObjectId().getSuperapp());
		objectId.setInternalObjectId(entity.getObjectId().getInternalObjectId());
		
		boundary.setObjectId(objectId);
		
		boundary.setType(entity.getType());
		boundary.setAlias(entity.getAlias());
		boundary.setActive(entity.getActive());
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		
		Location location = new Location(entity.getLocation().getLat(), entity.getLocation().getLng());
		boundary.setLocation(location);
		
		CreatedBy cb = new CreatedBy(entity.getCreatedBy());
		boundary.setCreatedBy(cb);
		
	    boundary.setObjectDetails(entity.getObjectDetails());
	    return boundary;
	}
	

	public ObjectEntity toEntity(ObjectBoundary obj)
	{
		ObjectEntity entity = new ObjectEntity();
		ObjectPrimaryKeyId objectPrimaryKeyId = new ObjectPrimaryKeyId();
		objectPrimaryKeyId.setSuperapp(obj.getObjectId().getSuperapp());
		objectPrimaryKeyId.setInternalObjectId(obj.getObjectId().getInternalObjectId());
		
		entity.setObjectId(objectPrimaryKeyId);
		entity.setType(obj.getType());
		entity.setAlias(obj.getAlias());
		entity.setActive(obj.getActive());
		entity.setCreationTimestamp(obj.getCreationTimestamp());
		entity.setLocation(new Location(obj.getLocation().getLat(),obj.getLocation().getLng()));
		entity.setCreatedBy(obj.getCreatedBy().getUserId());
	    entity.setObjectDetails(obj.getObjectDetails());
		
		return entity;
	}
	
	public ObjectPrimaryKeyId idToEntity(SuperAppObjectIdBoundary boundaryId) {
		ObjectPrimaryKeyId entityPk = new ObjectPrimaryKeyId();
		entityPk.setSuperapp(boundaryId.getSuperapp());
		entityPk.setInternalObjectId(boundaryId.getInternalObjectId());
		
		return entityPk;
	}
	
	public SuperAppObjectIdBoundary idToBoundary(ObjectPrimaryKeyId entityPk) {
		SuperAppObjectIdBoundary boundaryId = new SuperAppObjectIdBoundary();
		boundaryId.setSuperapp(entityPk.getSuperapp());
		boundaryId.setInternalObjectId(entityPk.getInternalObjectId());
		
		return boundaryId;
	}
}
	



