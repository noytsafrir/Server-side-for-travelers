package superapp.logic;

import java.util.List;

import superapp.boundaries.object.ObjectBoundary;

public interface ObjectService {

	public ObjectBoundary createObject(ObjectBoundary obej);

	@Deprecated
	public void deleteAllObject();
	@Deprecated
	public ObjectBoundary updateObject(String superapp,String internalObjectId , ObjectBoundary update);
	@Deprecated
	public ObjectBoundary getSpecsificObject(String ObjectSuperapp ,String internalObjectId);
	@Deprecated
	public List<ObjectBoundary> getAllObjects();

}