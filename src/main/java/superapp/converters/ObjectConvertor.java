package superapp.converters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectId;
import superapp.boundaries.user.UserId;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;
import superapp.data.UserPrimaryKeyId;
@Component //create an instance from this class 
public class ObjectConvertor {
	


	@SuppressWarnings("unchecked")
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
		
		UserId uid = new UserId(entity.getEmail());
		uid.setSuperapp(entity.getUserSuperapp());
		CreatedBy cb = new CreatedBy(uid);
		
		boundary.setCreatedBy( cb );
		
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
		
		entity.setEmail(obj.getCreatedBy().getUserId().getEmail());
		entity.setUserSuperapp(obj.getCreatedBy().getUserId().getSuperapp());
		
	    entity.setObjectDetails(obj.getObjectDetails());
	    
	    entity.setBindings(obj.getBinding());
		
		return entity;
	}
	

//	public String MapToString (HashMap<String,Object> objectDetails)
//	{
//		// Convert the objectDetails map to a serialized string
//	    ObjectMapper mapper = new ObjectMapper();
//	    String objectDetailsString = null;
//	    try {
//	        objectDetailsString = mapper.writeValueAsString(objectDetails);
//	    } catch (JsonProcessingException e) {
//	        e.printStackTrace();
//	    }
//	    return objectDetailsString ; 
//		
//	}
}
	



