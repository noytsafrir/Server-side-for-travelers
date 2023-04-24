package superapp.converters;


import org.springframework.stereotype.Component;


import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectId;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;


@Component //create an instance from this class 
public class ObjectConvertor {

	public ObjectBoundary toBoundary(ObjectEntity entity)
	{
		ObjectBoundary boundary = new ObjectBoundary();
		
		ObjectId objectId = new ObjectId();
		objectId.setSuperapp(entity.getObjectId().getSuperapp());
		objectId.setInternalObjectId(entity.getObjectId().getInternalObjectId());
		
		boundary.setObjectId(objectId);
		
		boundary.setType(entity.getType());
		boundary.setAlias(entity.getAlias());
		boundary.setActive(entity.getActive());
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		
		Location location = new Location(entity.getLat(), entity.getLng());
		boundary.setLocation(location);
		
		CreatedBy cb = new CreatedBy(entity.getCreatedBy());
		boundary.setCreatedBy(cb);
		
	    boundary.setObjectDetails(entity.getObjectDetails());
	    boundary.setBinding(entity.getBindings());
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
		
		entity.setLat(obj.getLocation().getLat());
		entity.setLng(obj.getLocation().getLng());
		entity.setCreatedBy(obj.getCreatedBy().getUserId());
		
	    entity.setObjectDetails(obj.getObjectDetails());
	    entity.setBindings(obj.getBinding());
		
		return entity;
	}
}
	



