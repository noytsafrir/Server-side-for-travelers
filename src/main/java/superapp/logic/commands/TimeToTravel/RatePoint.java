package superapp.logic.commands.TimeToTravel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.converters.ObjectConvertor;
import superapp.dal.ObjectCrud;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;
import superapp.exceptions.InvalidInputException;
import superapp.exceptions.ResourceNotFoundException;
import superapp.logic.commands.MiniappInterface;

@Component("timeToTravel_ratePoint")
public class RatePoint implements MiniappInterface{
	private ObjectCrud objectCrud;
	private ObjectConvertor converter;
	private final Log logger = LogFactory.getLog(RatePoint.class);

	
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
		String command = miniappCommandBoundary.getCommand();
		
		if(command.equals("timeToTravel_ratePoint")) {
			this.logger.trace("The command to invoke is : " + command);
			ObjectPrimaryKeyId pkid = converter.idToEntity(miniappCommandBoundary.getTargetObject().getObjectId());
			ObjectEntity entity = this.objectCrud.findById(pkid)
					.orElseThrow(() -> new ResourceNotFoundException(pkid, "find object"));
			this.logger.warn("**********"+ miniappCommandBoundary.getCommandAttributes());
			this.logger.warn("##########"+ entity.getObjectDetails());

			Integer count = (Integer)entity.getObjectDetails().get("ratingCount") + 1;
			entity.getObjectDetails().put("ratingCount",count);
			Double rateFromUser = (Double) miniappCommandBoundary.getCommandAttributes().get("rate");
			Double totalRating = ((Double)entity.getObjectDetails().get("totalRating") + rateFromUser);
			Double newRate = totalRating / count;
			entity.getObjectDetails().put("rating",newRate);
			entity.getObjectDetails().put("totalRating",totalRating);
			objectCrud.save(entity);
			return converter.toBoundary(entity);
		}
		else {
			this.logger.warn("The command " + command + " is not found");
			throw new InvalidInputException("command ", command);
		}
	}
}
