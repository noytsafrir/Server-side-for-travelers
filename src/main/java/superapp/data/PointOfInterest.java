package superapp.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PointOfInterest {

	private String type;

	private String privateOrPublic;

	private double rating;

	private String image;

	private Map<String, Object> details;

	public PointOfInterest() {}

	public PointOfInterest(String type, String privateOrPublic, double rating, String image) {
		this.type = type;
		this.privateOrPublic = privateOrPublic;
		this.rating = rating;
		this.image = image;
		this.details = new HashMap<>();
	}

	public PointOfInterest(String type, String privateOrPublic, double rating, String image, Map<String, Object> details) {
		this.type = type;
		this.privateOrPublic = privateOrPublic;
		this.rating = rating;
		this.image = image;
		this.details = details;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String isPrivateOrPublic() {
		return privateOrPublic;
	}

	public void setPrivateOrPublic(String privateOrPublic) {
		this.privateOrPublic = privateOrPublic;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getPrivateOrPublic() {
		return privateOrPublic;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}
}
