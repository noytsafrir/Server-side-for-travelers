package superapp.data;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

// at this part we want to change the attributes for the DB data type
//e.g. obj to 3 strings like commandID that we replace
// with the strings : superapp , miniapp & internalCommandId

@Entity
@Table(name = "MiniAppCommands")
@IdClass(MiniAppCommandPrimaryKeyId.class) // pk will be defind later
public class MiniAppCommandEntity {
	// private MiniAppCommandID commandID;
	@Id
	private String superapp;
	@Id
	private String miniapp;
	@Id
	private String internalCommandId;

	private String command;

	private String internalObjectId;
	private Date invocationTimestamp;
	private String email;
	@Lob // from map to string
	private String commandAttributes;

	// empty const'
	public MiniAppCommandEntity() {
	}

	// const'
	public MiniAppCommandEntity(String superapp, String miniapp, String internalCommandId, String command,
			String internalObjectId, Date invocationTimestamp, String email, String commandAttributes) {
		super();
		this.superapp = superapp;
		this.miniapp = miniapp;
		this.internalCommandId = internalCommandId;
		this.command = command;
		this.internalObjectId = internalObjectId;
		this.invocationTimestamp = invocationTimestamp;
		this.email = email;
		this.commandAttributes = commandAttributes;
	}

	// g & s
	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}

	public String getMiniapp() {
		return miniapp;
	}

	public void setMiniapp(String miniapp) {
		this.miniapp = miniapp;
	}

	public String getInternalCommandId() {
		return internalCommandId;
	}

	public void setInternalCommandId(String internalCommandId) {
		this.internalCommandId = internalCommandId;
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

}
