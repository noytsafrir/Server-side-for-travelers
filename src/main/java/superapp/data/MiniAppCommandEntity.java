package superapp.data;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import superapp.boundaries.command.MiniAppCommandID;


// at this part we want to change the attributes for the DB data type
//e.g. obj to 3 strings like commandID that we replace
// with the strings : superapp , miniapp & internalCommandId

@Document(collection = "MiniAppCommands")
public class MiniAppCommandEntity {
	@Id
	private MiniAppCommandPrimaryKeyId commandID;
	
	private String command;

	private String internalObjectId;
	private Date invocationTimestamp;
	private String email;
	
	private String commandAttributes;


	public MiniAppCommandEntity() {}



	public MiniAppCommandEntity(MiniAppCommandPrimaryKeyId commandID, String command, String internalObjectId,
			Date invocationTimestamp, String email, String commandAttributes) {
		super();
		this.commandID = commandID;
		this.command = command;
		this.internalObjectId = internalObjectId;
		this.invocationTimestamp = invocationTimestamp;
		this.email = email;
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

	public String getInternalObjectId() {
		return internalObjectId;
	}

	public void setInternalObjectId(String internalObjectId) {
		this.internalObjectId = internalObjectId;
	}

	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}

	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCommandAttributes() {
		return commandAttributes;
	}

	public void setCommandAttributes(String commandAttributes) {
		this.commandAttributes = commandAttributes;
	}


	@Override
	public String toString() {
		return "MiniAppCommandEntity [commandID=" + commandID + ", command=" + command + ", internalObjectId="
				+ internalObjectId + ", invocationTimestamp=" + invocationTimestamp + ", email=" + email
				+ ", commandAttributes=" + commandAttributes + "]";
	}

	

}
