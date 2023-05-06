package superapp.data;

import java.util.Objects;

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
	
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof ObjectPrimaryKeyId)) {
            return false;
        }
        
        ObjectPrimaryKeyId other = (ObjectPrimaryKeyId) o;
        return Objects.equals(this.superapp, other.getSuperapp()) &&
               Objects.equals(this.internalObjectId, other.getInternalObjectId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.superapp, this.internalObjectId);
    }
	
}
