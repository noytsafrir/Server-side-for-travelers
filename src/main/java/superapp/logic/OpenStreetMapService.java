package superapp.logic;

import superapp.boundaries.object.OSMObjectBoundary;
import superapp.boundaries.user.UserId;

import java.util.List;

public interface OpenStreetMapService {
    public List<OSMObjectBoundary> retrievePOIs(double latitude, double longitude, double radiusInMeters, String category, String type);
    public void createPOIsFromOSM(double latitude, double longitude, double radiusInMeters, String category, String type, UserId userId);

}
