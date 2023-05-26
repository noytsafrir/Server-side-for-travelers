package superapp.boundaries.object;

import java.util.Map;

public class OSMObjectBoundary {
	private String type;
	private long id;
	private double lat;
	private double lon;
	private Map<String, Object> tags;

	public OSMObjectBoundary() {}

	public OSMObjectBoundary(String type, long id, double lat, double lon, Map<String, Object> tags) {
		this.type = type;
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.tags = tags;
	}

	// Getters and setters
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public Map<String, Object> getTags() {
		return tags;
	}

	public void setTags(Map<String, Object> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "OSMObjectBoundary{" +
				"type='" + type + '\'' +
				", id=" + id +
				", lat=" + lat +
				", lon=" + lon +
				", tags=" + tags +
				'}';
	}
}

