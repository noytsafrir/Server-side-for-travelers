package superapp.data;

import superapp.utils.ResourceIdentifier;

public class ObjectPrimaryKeyId implements ResourceIdentifier {
	private String superapp;
	private String internalObjectId;
	
	public ObjectPrimaryKeyId(String superapp, String internalObjectId) {

		this.superapp = superapp;
		this.internalObjectId = internalObjectId;
	}

	public ObjectPrimaryKeyId() {
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
		return "ObjectPrimaryKeyId [superapp=" + superapp + ", internalObjectId=" + internalObjectId + "]";
	}
	

}
