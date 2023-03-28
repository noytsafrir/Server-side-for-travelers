package superapp.boundaries.command;

import superapp.boundaries.object.ObjectId;

public class TargetObject {

	private ObjectId objectId;

	public TargetObject() {}

	public TargetObject(ObjectId objectId) {
		this.objectId = objectId;
	}

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	@Override
	public String toString() {
		return "TargetObject [objectId=" + objectId + "]";
	}

	
}
