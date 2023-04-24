package superapp.converters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import superapp.boundaries.command.InvocationUser;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandID;
import superapp.boundaries.command.TargetObject;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.object.ObjectId;
import superapp.boundaries.user.UserId;
import superapp.data.MiniAppCommandEntity;
import superapp.data.MiniAppCommandPrimaryKeyId;

@Component // create an instance from this class

public class MiniAppCommandConverter {

	@SuppressWarnings("unchecked")
	public MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity) {

		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();
		
		MiniAppCommandID miniappId = new MiniAppCommandID();
		miniappId.setSuperapp(entity.getCommandID().getSuperapp());
		miniappId.setMiniapp(entity.getCommandID().getMiniapp());
		miniappId.setInternalCommandId(entity.getCommandID().getInternalCommandId());
		boundary.setCommandId(miniappId);

		boundary.setCommand(entity.getCommand());
	
		boundary.setTargetObject(new TargetObject(new ObjectId(entity.getCommandID().getSuperapp(), entity.getInternalObjectId())));
		boundary.setInvokedBy(new InvocationUser(new UserId(entity.getCommandID().getSuperapp(), entity.getEmail())));
		boundary.setCommandId(new MiniAppCommandID(entity.getCommandID().getSuperapp(), entity.getCommandID().getMiniapp(), entity.getCommandID().getInternalCommandId()));
		boundary.setInvocationTimestamp(entity.getInvocationTimestamp());
		
		 ObjectMapper mapper = new ObjectMapper();
		    HashMap<String, Object> commandAttributesMap = null;
		    try {
		    	commandAttributesMap = (HashMap<String, Object>) mapper.readValue(entity.getCommandAttributes(), Map.class);
		    } catch (JsonProcessingException e) {
		        e.printStackTrace();
		    }
		boundary.setCommandAttributes(commandAttributesMap);
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
		entity.setInternalObjectId(boundary.getTargetObject().getObjectId().getInternalObjectId());
		entity.setInvocationTimestamp(boundary.getInvocationTimestamp());
		entity.setEmail(boundary.getInvokedBy().getUserId().getEmail());
		
		   ObjectMapper mapper = new ObjectMapper();
		    String commandAttributesString = null;
		    try {
		    	commandAttributesString = mapper.writeValueAsString(boundary.getCommandAttributes());
		    } catch (JsonProcessingException e) {
		        e.printStackTrace();
		    }
		    entity.setCommandAttributes(commandAttributesString);
		return entity;
	}
}