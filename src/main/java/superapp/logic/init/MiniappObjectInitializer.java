package superapp.logic.init;

import java.util.Date;
import java.util.HashMap;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import superapp.boundaries.object.CreatedBy;
import superapp.boundaries.object.Location;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserId;
import superapp.logic.ObjectServiceWithPagination;

@Component
@Profile("afekaManualTests")
public class MiniappObjectInitializer implements CommandLineRunner{
	private ObjectServiceWithPagination objectService;
	private Log logger = LogFactory.getLog(MiniappObjectInitializer.class);

	@Autowired
	public MiniappObjectInitializer(ObjectServiceWithPagination objectService) {
		super();
		this.objectService = objectService;
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void run(String... args) throws Exception {
//		ObjectBoundary obj = new ObjectBoundary();
//		SuperAppObjectIdBoundary objId = new SuperAppObjectIdBoundary();
//		UserId userId = new UserId(	);
//		HashMap<String, Object> details = new HashMap<String, Object>();
////		details.put("attr1", "testAttr1");
////		details.put("attr2", true);
////		details.put("attr3", 1);
////		details.put("attr4", 0.5);
//		obj.setObjectId(objId);
//		obj.setType("miniappDummyObject");
//		obj.setAlias("miniappDummyObject");
//		obj.setActive(true);
//		obj.setCreationTimestamp(new Date());
//		obj.setLocation(new Location(0.0, 1.1));
//		obj.setCreatedBy(new CreatedBy(userId));
//		obj.setObjectDetails(details);
//		return obj;
//	}
}













