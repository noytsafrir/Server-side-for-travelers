package superapp.converters;

import java.util.Date;
import java.util.HashMap;

import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectId;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;
import superapp.data.ObjectEntity;

public class ObjectConvertor {

	public ObjectEntity toEntity(ObjectBoundary obj)
	{
		ObjectEntity entity = new ObjectEntity();
		entity.setObjectId(obj.getObjectId());
		entity.setType(obj.getType());
		entity.setAlias(obj.getAlias());
		entity.setActive(obj.getActive());
		entity.setCreationTimestamp(obj.getCreationTimestamp());
		entity.setLocation(obj.getLocation());
		entity.setCreatedBy(obj.getCreatedBy());
		entity.setObjectDetails(obj.getObjectDetails());
		return entity;
	}
	public ObjectBoundary toBoundary(ObjectEntity obj)
	{
		ObjectBoundary boundary = new ObjectBoundary();
		boundary.setObjectId(obj.getObjectId());
		boundary.setType(obj.getType());
		boundary.setAlias(obj.getAlias());
		boundary.setActive(obj.getActive());
		boundary.setCreationTimestamp(obj.getCreationTimestamp());
		boundary.setLocation(obj.getLocation());
		boundary.setCreatedBy(obj.getCreatedBy());
		boundary.setObjectDetails(obj.getObjectDetails());
		return boundary;
	}
	}



