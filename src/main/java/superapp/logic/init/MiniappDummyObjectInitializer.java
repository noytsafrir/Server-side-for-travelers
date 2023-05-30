package superapp.logic.init;

import java.util.List;

import jakarta.annotation.PostConstruct;
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
import superapp.boundaries.user.UserId;
import superapp.logic.ObjectServiceWithPagination;

@Component
@Profile("MiniappDummyObjectInitializer")
public class MiniappDummyObjectInitializer implements CommandLineRunner {
	private ObjectServiceWithPagination objectService;
	private Log logger = LogFactory.getLog(MiniappDummyObjectInitializer.class);

	private String dummyObjectType;

	private String superAppName;
	private String email;

	private UserId userId = null;

	@Autowired
	public MiniappDummyObjectInitializer(ObjectServiceWithPagination objectService) {
		super();
		this.objectService = objectService;
	}

	@Value("${miniapp.command.targetObject.type}")
	public void setDummyObjectType(String dummyObjectType) {
		this.dummyObjectType = dummyObjectType;
	}

	@Value("${spring.application.name}")
	public void setSuperappName(String superAppName) {
		this.superAppName = superAppName;
	}

	@Value("${osm.user.userEmail}")
	public void setEmail(String email) {
		this.email = email;
	}

	@PostConstruct
	public void init() {
		this.userId = new UserId(this.superAppName, this.email);
	}

	@Override
	public void run(String... args) throws Exception {
		getObjectOrCreate();
	}

	public void getObjectOrCreate() {
		List<ObjectBoundary> dummyObjects =  objectService.getObjectsByType(this.superAppName, this.email, this.dummyObjectType, 1, 0);
		if (dummyObjects.size() == 0) {
			createDummyObject();
		}
		else {
			this.logger.trace("Dummy object already exist: " + dummyObjects.get(0).toString());
		}
}

	private void createDummyObject() {
		ObjectBoundary object = new ObjectBoundary();
		object.setType(this.dummyObjectType);
		object.setCreatedBy(new CreatedBy(this.userId));
		object.setAlias("dummyObject");
		object.setLocation(new Location(0.0, 0.0));
		object.setActive(true);

		object = objectService.createObject(object);
		this.logger.trace("Dummy object created: " + object.toString());
	}

}