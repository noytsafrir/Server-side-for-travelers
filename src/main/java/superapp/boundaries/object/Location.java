package superapp.boundaries.object;

import java.util.Objects;

public class Location {
	
	private Double lat;
	private Double lng;
	
	public Location() {}

	public Location(Double lat, Double lng) {
		this.lat = lat;
		this.lng = lng;
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

	@Override
	public String toString() {
		return "Location [lat=" + lat + ", lng=" + lng + "]";
	}
	
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Location)) {
            return false;
        }
        
        Location other = (Location) o;
        return Objects.equals(this.lat, other.getLat()) &&
               Objects.equals(this.lng, other.getLng());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.lat, this.lng);
    }
}
