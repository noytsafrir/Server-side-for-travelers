package superapp.logic.commands.TimeToTravel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.user.UserId;
import superapp.converters.ObjectConvertor;
import superapp.dal.ObjectCrud;
import superapp.logic.actualServices.MiniAppCommandServiceDB;
import superapp.logic.commands.MiniappInterface;

@Component("timeToTravel_findMyPoints")
public class FindMyPoints implements MiniappInterface{
	private ObjectCrud objectCrud;
	private ObjectConvertor converter;
	private Log logger = LogFactory.getLog(FindMyPoints.class);

	
	@Autowired
	public void setObjectCrud(ObjectCrud objectCrud) {
		this.objectCrud = objectCrud;
	}
	
	@Autowired
	public void setConverter(ObjectConvertor converter) {
		this.converter = converter;
	}


	@Override
	public Object activateCommand(MiniAppCommandBoundary miniappCommandBoundary) {
		UserId userId = miniappCommandBoundary.getInvokedBy().getUserId();
		String command = miniappCommandBoundary.getCommand();

		if(command.equals("timeToTravel_findMyPoints")) {
			this.logger.trace("The command to invoke is : " + command);
			return objectCrud
					.findAllByCreatedBy(userId,PageRequest.of(0, 10, Direction.DESC, "createdTimestamp", "objectId"))
					.stream()
					.map(this.converter::toBoundary)
					.toList();
		}
		else {
			this.logger.warn("The command " + command + " is not found");
			return null; //change to default
		}
		//TODO: check if it is a miniapp user
		//TODO: save the command in the DB
	}
}
