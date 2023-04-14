package superapp.logic;

import java.util.List;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectId;
import superapp.boundaries.user.UserBoundary;

public interface ObjectService {

	public ObjectBoundary createObject(ObjectBoundary obej);
	public ObjectBoundary updateObject(String superapp,String internalObjectId , ObjectBoundary update);
	public List<ObjectBoundary> getAllObjects();
	public void deleteAllObject();
	public void deleteObject(ObjectId id );
}
