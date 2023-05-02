package superapp.logic;

import java.util.List;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectId;

public interface ObjectServiceBinding extends ObjectService {
	public void bindObjectToParent(String superapp,String internalObjectId , ObjectId child);
	public List<ObjectBoundary> getAllChildrenOfObject(String superapp, String internalObjectId);
	public List<ObjectBoundary> getAllParentsOfObject(String superapp, String internalObjectId);
}
