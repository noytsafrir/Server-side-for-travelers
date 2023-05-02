package superapp.logic;

import java.util.List;

import superapp.boundaries.object.ObjectBoundary;

public interface ObjectService {

	public ObjectBoundary createObject(ObjectBoundary obej);
	public ObjectBoundary updateObject(String superapp,String internalObjectId , ObjectBoundary update);
	public List<ObjectBoundary> getAllObjects();
	public void deleteAllObject();

	public ObjectBoundary getSpecsificObject( String ObjectSuperapp ,String internalObjectId );
}