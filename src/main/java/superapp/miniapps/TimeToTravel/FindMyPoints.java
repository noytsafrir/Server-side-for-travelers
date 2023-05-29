package superapp.miniapps.TimeToTravel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.user.UserId;
import superapp.converters.ObjectConvertor;
import superapp.dal.ObjectCrud;

import superapp.miniapps.MiniappInterface;

@Component("timeToTravel_findMyPoints")
public class FindMyPoints implements MiniappInterface{
	private ObjectCrud objectCrud;
	private ObjectConvertor converter;
	

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
		//TODO: check if it is a miniapp user
		//TODO: save the command in the DB

		return objectCrud
				.findAllByCreatedBy(userId,PageRequest.of(0, 10, Direction.DESC, "createdTimestamp", "objectId"))
				.stream()
				.map(this.converter::toBoundary)
				.toList();
	}
}
