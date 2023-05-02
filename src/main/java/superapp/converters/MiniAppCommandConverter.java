package superapp.converters;

import org.springframework.stereotype.Component;

import superapp.boundaries.command.InvocationUser;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandID;
import superapp.boundaries.command.TargetObject;
import superapp.data.MiniAppCommandEntity;
import superapp.data.MiniAppCommandPrimaryKeyId;

@Component // create an instance from this class

public class MiniAppCommandConverter {

	public MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity) {

		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();
		
		MiniAppCommandID miniappId = new MiniAppCommandID();
		miniappId.setSuperapp(entity.getCommandID().getSuperapp());
		miniappId.setMiniapp(entity.getCommandID().getMiniapp());
		miniappId.setInternalCommandId(entity.getCommandID().getInternalCommandId());
		boundary.setCommandId(miniappId);

		boundary.setCommand(entity.getCommand());
	
		boundary.setTargetObject(new TargetObject(entity.getTargetObject()));
		boundary.setInvokedBy(new InvocationUser(entity.getInvokedBy()));
		boundary.setCommandId(new MiniAppCommandID(entity.getCommandID().getSuperapp(), entity.getCommandID().getMiniapp(), entity.getCommandID().getInternalCommandId()));
		boundary.setInvocationTimestamp(entity.getInvocationTimestamp());
		
		boundary.setCommandAttributes(entity.getCommandAttributes());
		return boundary;

	}

	public MiniAppCommandEntity toEntity(MiniAppCommandBoundary boundary) {
		MiniAppCommandEntity entity = new MiniAppCommandEntity();
		
		MiniAppCommandPrimaryKeyId miniAppPrimaryKeyId= new MiniAppCommandPrimaryKeyId();
		miniAppPrimaryKeyId.setSuperapp(boundary.getCommandId().getSuperapp());
		miniAppPrimaryKeyId.setMiniapp(boundary.getCommandId().getMiniapp());
		miniAppPrimaryKeyId.setInternalCommandId(boundary.getCommandId().getInternalCommandId());
		entity.setCommandID(miniAppPrimaryKeyId);
		
		entity.setCommand(boundary.getCommand());
		entity.setTargetObject(boundary.getTargetObject().getObjectId());
		entity.setInvocationTimestamp(boundary.getInvocationTimestamp());
		entity.setInvokedBy(boundary.getInvokedBy().getUserId());
		
	    entity.setCommandAttributes(boundary.getCommandAttributes());
		return entity;
	}
}