package superapp.boundaries.object;

import superapp.utils.BoundaryObject;
import superapp.utils.ResourceIdentifier;

public class SuperAppObjectIdBoundary implements ResourceIdentifier, BoundaryObject {
	private String superapp;
	private String internalObjectId;
	
	public SuperAppObjectIdBoundary() {}

	public SuperAppObjectIdBoundary(String superapp, String internalObjectId) {
		this.superapp = superapp;
		this.internalObjectId = internalObjectId;
	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}

	public String getInternalObjectId() {
		return internalObjectId;
	}

	public void setInternalObjectId(String internalObjectId) {
		this.internalObjectId = internalObjectId;
	}

	@Override
	public String toString() {
		return "SuperAppObjectIdBoundary [superapp=" + superapp + ", internalObjectId=" + internalObjectId + "]";
	}

	
}
