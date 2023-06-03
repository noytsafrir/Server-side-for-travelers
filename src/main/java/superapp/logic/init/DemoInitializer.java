package superapp.logic.init;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;
import superapp.data.UserRole;
import superapp.exceptions.ResourceNotFoundException;
import superapp.logic.ObjectServiceWithPagination;
import superapp.logic.UsersService;

import java.util.Date;
import java.util.HashMap;

@Component
@Profile("DemoInitializer")
public class DemoInitializer implements CommandLineRunner{
	private UsersService usersService;

	private ObjectServiceWithPagination objectServiceWithPagination;
	private Log logger = LogFactory.getLog(DemoInitializer.class);
	private String superAppName;
	private String email = "noy@gmail.com";

	@Autowired
	public DemoInitializer(UsersService usersService, ObjectServiceWithPagination objectServiceWithPagination) {
		super();
		this.usersService = usersService;
		this.objectServiceWithPagination = objectServiceWithPagination;
	}

	@Value("${spring.application.name}")
	public void setSuperappName(String superAppName) {
		this.superAppName = superAppName;
	}

	@Override
	public void run(String... args) throws Exception {
		this.logger.trace("DemoInitializer: " + this.superAppName + " " + this.email);
		loginOrCreateUsers();
		createObjects();
	}

	public void loginOrCreateUsers() {
		UserBoundary userNoy = null;
		try {
			userNoy = usersService.login(this.superAppName, this.email);
			this.logger.trace("Noy user already exist: " + userNoy.toString());
		} catch (ResourceNotFoundException e) {
			UserId id = new UserId(this.superAppName, this.email);
			userNoy = new UserBoundary(id, UserRole.MINIAPP_USER.toString(), "Noy Tsafrir", "https://seeklogo.com/images/S/stitch-logo-4F8524AEFA-seeklogo.com.png");
			userNoy = usersService.createUser(userNoy);
			this.logger.trace("Noy user created: " + userNoy.toString());
		}
	}

	public void createObjects() {
		ObjectBoundary Object1 = new ObjectBoundary();
		UserId userId = new UserId(this.superAppName, this.email);
		Object1.setType("Point");
		Object1.setAlias("מסעדה של מומו");
		Object1.setLocation(new Location(31.670566022009478, 34.55536242574453));
		Object1.setCreatedBy(new CreatedBy(userId));
		Object1.setActive(true);
		HashMap<String, Object> details1 = new HashMap<String, Object>();
		details1.put("image", "https://firebasestorage.googleapis.com/v0/b/travelapp-1f5c4.appspot.com/o/images%2F2023_06_04_00_58_57?alt=media&token=36b3687f-1be2-4145-95a2-5ad504d728fe");
		details1.put("rating", 0);
		details1.put("description", "אוכל מדהים, שירות לבבי, נוף מטורף ומחירים מצוינים, ממליצה בחום לעבור אצל מומו");
		details1.put("totalRating", 0);
		details1.put("type", "Restaurant");
		details1.put("ratingCount", 0);
		Object1.setObjectDetails(details1);

		ObjectBoundary Object2 = new ObjectBoundary();
		Object2.setType("Point");
		Object2.setAlias("הגן החי בפתח תקווה");
		Object2.setLocation(new Location(32.08375914015444, 34.891999773681164));
		Object2.setCreatedBy(new CreatedBy(userId));
		Object2.setActive(true);
		HashMap<String, Object> details2 = new HashMap<String, Object>();
		details2.put("image", "https://firebasestorage.googleapis.com/v0/b/travelapp-1f5c4.appspot.com/o/images%2F2023_06_04_01_05_50?alt=media&token=1bdfc003-1214-41e8-b50b-b08bdb4207bc");
		details2.put("rating", 0);
		details2.put("description", "חוויה מאוד מיוחדת, המון חיות, נקי ומטופח, מצוין למשפחות עם ילדים");
		details2.put("totalRating", 0);
		details2.put("type", "Attraction");
		details2.put("ratingCount", 0);
		Object2.setObjectDetails(details2);

		objectServiceWithPagination.createObject(Object1);
		objectServiceWithPagination.createObject(Object2);
	}
}