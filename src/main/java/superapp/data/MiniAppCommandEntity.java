package superapp.data;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import superapp.boundaries.object.ObjectId;
import superapp.boundaries.user.UserId;


// at this part we want to change the attributes for the DB data type
//e.g. obj to 3 strings like commandID that we replace
// with the strings : superapp , miniapp & internalCommandId

@Document(collection = "MiniAppCommands")
public class MiniAppCommandEntity {
	@Id
	private MiniAppCommandPrimaryKeyId commandID;
	
	private String command;
	private ObjectId targetObject;
	private Date invocationTimestamp;
	
	private UserId invokedBy;
		
	private Map<String,Object> commandAttributes;


	public MiniAppCommandEntity() {}

	public MiniAppCommandEntity(MiniAppCommandPrimaryKeyId commandID, String command, ObjectId targetObject,
			Date invocationTimestamp, UserId invokedBy, Map<String, Object> commandAttributes) {
		super();
		this.commandID = commandID;
		this.command = command;
		this.targetObject = targetObject;
		this.invocationTimestamp = invocationTimestamp;
		this.invokedBy = invokedBy;
		this.commandAttributes = commandAttributes;
	}


	public MiniAppCommandPrimaryKeyId getCommandID() {
		return commandID;
	}

	public void setCommandID(MiniAppCommandPrimaryKeyId commandID) {
		this.commandID = commandID;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}



	public ObjectId getTargetObject() {
		return targetObject;
	}


	public void setTargetObject(ObjectId targetObject) {
		this.targetObject = targetObject;
	}


	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}

	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}



	public UserId getInvokedBy() {
		return invokedBy;
	}



	public void setInvokedBy(UserId invokedBy) {
		this.invokedBy = invokedBy;
	}


	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}

	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}

	@Override
	public String toString() {
		return "MiniAppCommandEntity [commandID=" + commandID + ", command=" + command + ", targetObject="
				+ targetObject + ", invocationTimestamp=" + invocationTimestamp + ", invokedBy=" + invokedBy
				+ ", commandAttributes=" + commandAttributes + "]";
	}


}
