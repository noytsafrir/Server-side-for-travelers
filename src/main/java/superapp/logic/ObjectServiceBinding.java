package superapp.logic;

import java.util.List;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;

public interface ObjectServiceBinding extends ObjectService {
	public void bindObjectToParent(String superapp,String internalObjectId , SuperAppObjectIdBoundary child);
	public List<ObjectBoundary> getAllChildrenOfObject(String superapp, String internalObjectId);
	public List<ObjectBoundary> getAllParentsOfObject(String superapp, String internalObjectId);
}
