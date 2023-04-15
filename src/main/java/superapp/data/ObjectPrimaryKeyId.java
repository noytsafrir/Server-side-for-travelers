package superapp.data;

import java.io.Serializable;

public class ObjectPrimaryKeyId implements Serializable{
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