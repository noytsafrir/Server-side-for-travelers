package superapp.logic;

import java.util.List;

import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;

public interface ObjectServiceBinding extends ObjectService {
	@Deprecated
	public void bindObjectToParent(String superapp,String internalObjectId , SuperAppObjectIdBoundary child);
	@Deprecated
	public List<ObjectBoundary> getAllChildrenOfObject(String superapp, String internalObjectId);
	@Deprecated
	public List<ObjectBoundary> getAllParentsOfObject(String superapp, String internalObjectId);
}
