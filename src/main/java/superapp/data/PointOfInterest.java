package superapp.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PointOfInterest {

	private String type;

	private String description;
	private double rating;

	private String image;

	public PointOfInterest() {}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
