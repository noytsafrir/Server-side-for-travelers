package superapp.data;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import superapp.boundaries.user.UserId;


@Document(collection = "Objects")
public class ObjectEntity {
	@Id
	private ObjectPrimaryKeyId objectId;
	private String type;
	private String alias;
	private boolean active;
	private Date creationTimestamp;	
	private Double lat;
	private Double lng;

	private UserId createdBy;

	private Map<String,Object> objectDetails;

	// this map will be for children and parents of the object
	private Map<String,String> bindings ;	
	
	public ObjectEntity() {}

	public ObjectEntity(ObjectPrimaryKeyId objectId, String type, String alias, boolean active, Date creationTimestamp,
			Double lat, Double lng, UserId createdBy, Map<String, Object> objectDetails, Map<String, String> bindings) {
		this.objectId = objectId;
		this.type = type;
		this.alias = alias;
		this.active = active;
		this.creationTimestamp = creationTimestamp;
		this.lat = lat;
		this.lng = lng;
		this.createdBy = createdBy;
		this.objectDetails = objectDetails;
		this.bindings = bindings;
	}

	public Map<String, String> getBindings() {
		return bindings;
	}

	public void setBindings(Map<String, String> bindings) {
		this.bindings = bindings;
	}

	public UserId getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserId createdBy) {
		this.createdBy = createdBy;
	}

	public ObjectPrimaryKeyId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectPrimaryKeyId objectId) {
		this.objectId = objectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}


	public Map<String,Object>  getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String,Object>  objectDetails) {
		this.objectDetails = objectDetails;
	}
	
	public void addChild(String childID ) {
		this.bindings.put("Child",childID );
	}
	
	public void addParent(String parentID ) {
		this.bindings.put("Parent",parentID );
	}
	
	public void RemoveChild(String childID ) {
		this.bindings.remove("Child", childID);
	}
	
	public void RemoveParent(String parentID ) {
		this.bindings.remove("Parent",parentID );
	}

	@Override
	public String toString() {
		return "ObjectEntity [objectId=" + objectId + ", type=" + type + ", alias=" + alias + ", active=" + active
				+ ", creationTimestamp=" + creationTimestamp + ", lat=" + lat + ", lng=" + lng + ", createdBy="
				+ createdBy + ", objectDetails=" + objectDetails + ", bindings=" + bindings + "]";
	}
}
