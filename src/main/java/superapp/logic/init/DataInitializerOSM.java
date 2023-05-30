package superapp.logic.init;

import jakarta.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;
import superapp.data.UserRole;
import superapp.exceptions.ResourceNotFoundException;
import superapp.logic.ObjectServiceWithPagination;
import superapp.logic.OpenStreetMapService;
import superapp.logic.UsersService;

import java.util.ArrayList;

@Component
@Profile("InitializerOSM")
public class DataInitializerOSM implements CommandLineRunner{
	private Log logger = LogFactory.getLog(DataInitializerOSM.class);
	private String superAppName;
	private String email;
	private OpenStreetMapService osmService;

	private ObjectServiceWithPagination objectService;

	private String typeOSM;

	private UserId userId = null;
	private double lat;
	private double lon;
	private double radius;

	private final String tagTourism = "tourism";
	private final String tagAmenity = "amenity";
	private final String tagWater = "water";
	private final String tagWaterway = "waterway";
	private final static ArrayList<String> tourismValues = new ArrayList<String>() {{
		add("attraction");
		add("museum");
		add("picnic_site");
		add("viewpoint");}};
	private final static ArrayList<String> amenityValues = new ArrayList<String>() {{
		add("fast_food");
		add("cafe");
		add("restaurant");
		add("fountain");
		add("water_point");}};
	private final static ArrayList<String> waterValues = new ArrayList<String>() {{
		add("lake");}};
	private final static ArrayList<String> waterwayValues = new ArrayList<String>() {{
		add("waterfall");}};

	@PostConstruct
	public void init() {
		this.userId = new UserId(this.superAppName, this.email);
	}

	@Autowired
	public void setOsmService(OpenStreetMapService osmService) {
		this.osmService = osmService;
	}

	@Autowired
	public void setObjectService(ObjectServiceWithPagination objectService) {
		this.objectService = objectService;
	}

	@Value("${spring.application.name:defaultValue}")
	public void setSuperappName(String superAppName) {
		this.superAppName = superAppName;
	}

	@Value("${osm.user.userEmail}")
	public void setEmail(String email) {
		this.email = email;
	}

	@Value("${osm.object.type}")
	public void setTypeOSM(String typeOSM) {
		this.typeOSM = typeOSM;
	}

	@Value("${osm.init.lat:defaultValue}")
	public void setLat(double lat) {
		this.lat = lat;
	}

	@Value("${osm.init.lon:defaultValue}")
	public void setLon(double lon) {
		this.lon = lon;
	}

	@Value("${osm.init.radius:defaultValue}")
	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Initializing OSM data");
		objectService.deleteObjectsByType(this.typeOSM);
		getTourism();
		getAmenity();
		getWater();
		getWaterway();
	}

	private void getTourism() {
		tourismValues
				.stream()
				.forEach(value -> osmService.createPOIsFromOSM(this.lat, this.lon, this.radius, this.tagTourism, value, this.userId));
	}

	private void getAmenity() {
		amenityValues
				.stream()
				.forEach(value -> osmService.createPOIsFromOSM(this.lat, this.lon, this.radius, this.tagAmenity, value, this.userId));
	}

	private void getWater() {
		waterValues
				.stream()
				.forEach(value -> osmService.createPOIsFromOSM(this.lat, this.lon, this.radius, this.tagWater, value, this.userId));
	}

	private void getWaterway() {
		waterwayValues
				.stream()
				.forEach(value -> osmService.createPOIsFromOSM(this.lat, this.lon, this.radius, this.tagWaterway, value, this.userId));
	}

}