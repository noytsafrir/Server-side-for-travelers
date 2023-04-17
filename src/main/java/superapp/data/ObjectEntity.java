package superapp.data;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.IdClass;
//import jakarta.persistence.Lob;
//import jakarta.persistence.Table;


@Document(collection = "Objects")
//@IdClass(ObjectPrimaryKeyId.class)
public class ObjectEntity {
	@Id
	private String superapp;
	@Id
	private String internalObjectId;
	
	private String type;
	private String alias;
	private boolean active;
	private Date creationTimestamp;
	
	private Double lat;
	private Double lng;
	
	private String email;
	
	private Map<String,Object> objectDetails;
	
	public ObjectEntity() {}
	
	 public String getUserSuperapp() {
			return userSuperapp;
		}

		public void setUserSuperapp(String userSuperapp) {
			this.userSuperapp = userSuperapp;
		}

		private String userSuperapp;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String,Object>  getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String,Object>  objectDetails) {
		this.objectDetails = objectDetails;
	}

	public ObjectEntity(String superapp, String internalObjectId, String type, String alias, boolean active,
			Date creationTimestamp, Double lat, Double lng, String email, Map<String,Object>  objectDetails,String usersuperapp) {
		super();
		this.superapp = superapp;
		this.internalObjectId = internalObjectId;
		this.type = type;
		this.alias = alias;
		this.active = active;
		this.creationTimestamp = creationTimestamp;
		this.lat = lat;
		this.lng = lng;
		this.email = email;
		this.objectDetails = objectDetails;
		this.userSuperapp =  usersuperapp;
	}

	@Override
	public String toString() {
		return "ObjectEntity [superapp=" + superapp + ", internalObjectId=" + internalObjectId + ", type=" + type
				+ ", alias=" + alias + ", active=" + active + ", creationTimestamp=" + creationTimestamp + ", lat="
				+ lat + ", lng=" + lng + ", email=" + email + ", objectDetails=" + objectDetails + ", userSuperapp="
				+ userSuperapp + "]";
	}


	
}
