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
@Component //create an instance from this class 
public class ObjectConvertor {
	

	public ObjectEntity toEntity(ObjectBoundary obj)
	{
		ObjectEntity entity = new ObjectEntity();
		entity.setSuperapp(obj.getObjectId().getSuperapp());
		entity.setInternalObjectId(obj.getObjectId().getInternalObjectId());
		
		entity.setType(obj.getType());
		entity.setAlias(obj.getAlias());
		entity.setActive(obj.getActive());
		entity.setCreationTimestamp(obj.getCreationTimestamp());
		
		entity.setLat(obj.getLocation().getLat());
		entity.setLng(obj.getLocation().getLng());
		
		entity.setEmail(obj.getCreatedBy().getUserId().getEmail());
		entity.setUserSuperapp(obj.getCreatedBy().getUserId().getSuperapp());
		
		// Convert the objectDetails map to a serialized string
//	    ObjectMapper mapper = new ObjectMapper();
//	    String objectDetailsString = null;
//	    try {
//	        objectDetailsString = mapper.writeValueAsString(obj.getObjectDetails());
//	    } catch (JsonProcessingException e) {
//	        e.printStackTrace();
//	    }
	    entity.setObjectDetails(obj.getObjectDetails());
		
		return entity;
	}
	@SuppressWarnings("unchecked")
	public ObjectBoundary toBoundary(ObjectEntity obj)
	{
		ObjectBoundary boundary = new ObjectBoundary();
		
		ObjectId oid = new ObjectId(obj.getSuperapp() , obj.getInternalObjectId());
		boundary.setObjectId(oid);
		boundary.setType(obj.getType());
		boundary.setAlias(obj.getAlias());
		boundary.setActive(obj.getActive());
		boundary.setCreationTimestamp(obj.getCreationTimestamp());
		
		Location location = new Location(obj.getLat(), obj.getLng());
		boundary.setLocation(location);
		
		UserId uid = new UserId(obj.getEmail());
		uid.setSuperapp(obj.getUserSuperapp());
		CreatedBy cb = new CreatedBy(uid);
		
		boundary.setCreatedBy( cb );
	
		
		  // Convert the objectDetails string to a Map
//	    ObjectMapper mapper = new ObjectMapper();
//	    HashMap<String, Object> objectDetailsMap = null;
//	    try {
//	        objectDetailsMap = (HashMap<String, Object>) mapper.readValue(obj.getObjectDetails(), Map.class);
//	    } catch (JsonProcessingException e) {
//	        e.printStackTrace();
//	    }
	    boundary.setObjectDetails(obj.getObjectDetails());

	    return boundary;

	}
	public String MapToString (HashMap<String,Object> objectDetails)
	{
		// Convert the objectDetails map to a serialized string
	    ObjectMapper mapper = new ObjectMapper();
	    String objectDetailsString = null;
	    try {
	        objectDetailsString = mapper.writeValueAsString(objectDetails);
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	    }
	    return objectDetailsString ; 
		
	}
}
	



