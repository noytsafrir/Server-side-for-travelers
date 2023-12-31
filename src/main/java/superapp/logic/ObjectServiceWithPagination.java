package superapp.logic;

import java.util.List;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;

public interface ObjectServiceWithPagination extends ObjectServiceBinding {

	public ObjectBoundary updateObject(String superapp, String internalObjectId, ObjectBoundary update,
			String userSuperapp, String email);

	public ObjectBoundary getSpecificObject(String ObjectSuperapp, String internalObjectId, String userSuperapp,
			String email);

	public List<ObjectBoundary> getAllObjects(String userSuperapp, String email, int size, int page);

	public void deleteAllObject(String userSuperapp, String email);

	public void bindObjectToParent(String superapp, String internalObjectId, SuperAppObjectIdBoundary child,
			String userSuperapp, String email);

	public List<ObjectBoundary> getAllChildrenOfObject(String superapp, String internalObjectId, String userSuperapp,
			String email, int size, int page);

	public List<ObjectBoundary> getAllParentsOfObject(String superapp, String internalObjectId, String userSuperapp,
			String email, int size, int page);
	
	public List<ObjectBoundary> getObjectsByType(String userSuperapp, String email, String type, 
			int size, int page);
	
	public List<ObjectBoundary> getObjectsByAlias(String userSuperapp, String email, String alias,
			int size, int page);
	
	public List<ObjectBoundary> getObjectsByLocationSearch(String userSuperapp, String email, 
			double lat, double lng, double distance,String distanceUnits, int size, int page);

	public void deleteObjectsByType(String type);

}