package superapp.converters;

import org.springframework.stereotype.Component;

import superapp.boundaries.command.InvocationUser;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandID;
import superapp.boundaries.command.TargetObject;
import superapp.boundaries.object.ObjectId;
import superapp.boundaries.user.UserId;
import superapp.data.MiniAppCommandEntity;

@Component // create an instance from this class

public class MiniAppCommandConverter {

	public MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity) {

		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();
		boundary.setCommand(entity.getCommand());
		// boundary.setCommandAttributes(null); -- to do getting map atrributes
		boundary.setTargetObject(new TargetObject(new ObjectId(entity.getSuperapp(), entity.getInternalObjectId())));
		boundary.setInvokedBy(new InvocationUser(new UserId(entity.getSuperapp(), entity.getEmail())));
		boundary.setCommandID(new MiniAppCommandID(entity.getSuperapp(), entity.getMiniapp(), entity.getInternalCommandId()));
		boundary.setInvocationTimestamp(entity.getInvocationTimestamp());
	
		
		return boundary;

	}

	public MiniAppCommandEntity toEntity(MiniAppCommandBoundary boundary) {
		MiniAppCommandEntity entity = new MiniAppCommandEntity();
		entity.setSuperapp(boundary.getCommandID().getSuperapp());
		entity.setMiniapp(boundary.getCommandID().getMiniapp());
		entity.setInternalCommandId(boundary.getCommandID().getInternalCommandId());
		entity.setCommand(boundary.getCommand());
		entity.setInternalObjectId(boundary.getTargetObject().getObjectId().getInternalObjectId());
		entity.setInvocationTimestamp(boundary.getInvocationTimestamp());
		entity.setEmail(boundary.getInvokedBy().getUserId().getEmail());
		// entity.setCommandAttributes(boundary.getCommandAttributes());
		return entity;
	}
}