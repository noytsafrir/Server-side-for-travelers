package superapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.logic.ObjectServiceWithPagination;


@RestController
public class ObjectSearchController {
	private ObjectServiceWithPagination objects;

	public ObjectServiceWithPagination getObjects() {
		return objects;
	}

	@Autowired
	public void setObjects(ObjectServiceWithPagination objects) {
		this.objects = objects;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/superapp/objects/search/byType/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectsByType(@PathVariable("type") String type,
											 @RequestParam(name = "userSuperapp", required = true) String userSuperapp,
											 @RequestParam(name = "userEmail", required = true) String userEmail,
											 @RequestParam(name = "size", required = false, defaultValue = "10") int size,
											 @RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return objects.getObjectsByType(userSuperapp, userEmail, type, size, page)
				.toArray(new ObjectBoundary[0]);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/superapp/objects/search/byAlias/{alias}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectsByAlias(@PathVariable("alias") String alias,
											  @RequestParam(name = "userSuperapp", required = true) String userSuperapp,
											  @RequestParam(name = "userEmail", required = true) String userEmail,
											  @RequestParam(name = "size", required = false, defaultValue = "10") int size,
											  @RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return objects.getObjectsByAlias(userSuperapp, userEmail, alias, size, page)
				.toArray(new ObjectBoundary[0]);
	}

	@RequestMapping(path = { "/superapp/objects/search/byLocation/{lat}/{lng}/{distance}" }, method = {
			RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getObjectsByLocationSquareSearch(@PathVariable("lat") double lat,
															 @PathVariable("lng") double lng,
															 @PathVariable("distance") double distance,
															 @RequestParam(name = "units", defaultValue = "NEUTRAL") String distanceUnits, @RequestParam(name = "userSuperapp", required = true) String userSuperapp,
															 @RequestParam(name = "userEmail", required = true) String userEmail,
															 @RequestParam(name = "size", required = false, defaultValue = "10") int size,
															 @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		double distanceInMeters;
		Distance convertedDistance = new Distance(distance, Metrics.NEUTRAL);

		if (distanceUnits.equalsIgnoreCase("KILOMETERS")) {
			convertedDistance = convertedDistance.in(Metrics.MILES);
			distanceInMeters = distance * 1000; // Convert kilometers to meters
		} else if (distanceUnits.equalsIgnoreCase("MILES")) {
			distanceInMeters = distance * 1609.34; // Convert miles to meters
		} else if (distanceUnits.equalsIgnoreCase("NEUTRAL")) {
			distanceInMeters = distance; // Regular
		} else {
			throw new RuntimeException(); // Bad convert word
		}

//		if (unit.equalsIgnoreCase("miles")) {
//			// Convert miles to meters
//			distance = distance.in(Metrics.MILES);
//		} else if (unit.equalsIgnoreCase("kilometers")) {
//			// Convert kilometers to meters
//			distance = distance.in(Metrics.KILOMETERS);
//		}

		return objects.getObjectsByLocationSquareSearch(userSuperapp, userEmail, lat, lng,
				distanceInMeters, size, page).toArray(new ObjectBoundary[0]);
	}

}