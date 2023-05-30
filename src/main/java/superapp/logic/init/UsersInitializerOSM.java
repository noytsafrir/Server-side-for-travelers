package superapp.logic.init;

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
import superapp.logic.UsersService;

@Component
@Profile("UserInitializerOSM")
public class UsersInitializerOSM implements CommandLineRunner{
	private UsersService usersService;
	private Log logger = LogFactory.getLog(UsersInitializerOSM.class);
	private String superAppName;
	private String email;

	@Autowired
	public UsersInitializerOSM(UsersService usersService) {
		super();
		this.usersService = usersService;
	}

	@Value("${spring.application.name}")
	public void setSuperappName(String superAppName) {
		this.superAppName = superAppName;
	}

	@Value("${osm.user.userEmail}")
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void run(String... args) throws Exception {
		this.logger.trace("UsersInitializerOSM: " + this.superAppName + " " + this.email);
		loginOrCreateOSM();
	}

	public void loginOrCreateOSM() {
		UserBoundary userOSM = null;
		try {
			userOSM = usersService.login(this.superAppName, this.email);
			this.logger.trace("OSM user already exist: " + userOSM.toString());
		} catch (ResourceNotFoundException e) {
			UserId id = new UserId(this.superAppName, this.email);
			userOSM = new UserBoundary(id, UserRole.SUPERAPP_USER.toString(), "OSM internal user", "NA");
			userOSM = usersService.createUser(userOSM);
			this.logger.trace("OSM user created: " + userOSM.toString());
		}
	}
}