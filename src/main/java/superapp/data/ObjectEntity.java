package superapp.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import superapp.boundaries.object.Location;
import superapp.boundaries.user.UserId;

@Document(collection = "Objects")
@CompoundIndexes({
    @CompoundIndex(name = "location_2dsphere", def = "{'location': '2dsphere'}")
})
public class ObjectEntity {
	@Id
	private ObjectPrimaryKeyId objectId;
	private String type;
	private String alias;
	private boolean active;
	private Date creationTimestamp;
	
	@GeoSpatialIndexed
	private Location location;
	private UserId createdBy;

	private Map<String, Object> objectDetails;

	@DBRef(lazy = true)
	private List<ObjectEntity> parents = new ArrayList<>();

	@DBRef(lazy = true)
	private List<ObjectEntity> children = new ArrayList<>();

	public ObjectEntity() {
	}


	public ObjectEntity(ObjectPrimaryKeyId objectId, String type, String alias, boolean active, Date creationTimestamp,
			Location location, UserId createdBy, Map<String, Object> objectDetails, List<ObjectEntity> parents,
			List<ObjectEntity> children) {
		this.objectId = objectId;
		this.type = type;
		this.alias = alias;
		this.active = active;
		this.creationTimestamp = creationTimestamp;
		this.location = location;
		this.createdBy = createdBy;
		this.objectDetails = objectDetails;
		this.parents = parents;
		this.children = children;
	}

	public List<ObjectEntity> getParents() {
		return parents;
	}
	
	public void setParents(List<ObjectEntity> parents) {
		this.parents = parents;
	}

	public List<ObjectEntity> getChildren() {
		return children;
	}
	


	public void setChildren(List<ObjectEntity> children) {
		this.children = children;
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

	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}


	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}

	public boolean addParent(ObjectEntity parent) {	
		return this.parents.add(parent);
	}
	
	public boolean addChild(ObjectEntity child) {		
		return this.children.add(child);
	}

	@Override
	public String toString() {
		return "ObjectEntity [objectId=" + objectId + ", type=" + type + ", alias=" + alias + ", active=" + active
				+ ", creationTimestamp=" + creationTimestamp + ", location=" + location + ", createdBy=" + createdBy
				+ ", objectDetails=" + objectDetails + ", parents=" + parents + ", children=" + children + "]";
	}

}
