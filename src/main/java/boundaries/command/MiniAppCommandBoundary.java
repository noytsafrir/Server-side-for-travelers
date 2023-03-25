package boundaries.command;

import java.util.Date;
import java.util.Map;

import boundaries.user.UserAction;

public class MiniAppCommandBoundary {

	private MiniAppCommandID commandID;
	private String command;
	private TargetObject targetObject;
	private Date invocationTimestamp;
	private UserAction invokedBy;
    private Map<String, Object> commandAttributes;

	
	public MiniAppCommandBoundary() {}

	public MiniAppCommandBoundary(MiniAppCommandID commandID, String command, TargetObject targetObject,
			Date invocationTimestamp, UserAction invokedBy, Map<String, Object> commandAttributes) {
		this.commandID = commandID;
		this.command = command;
		this.targetObject = targetObject;
		this.invocationTimestamp = invocationTimestamp;
		this.invokedBy = invokedBy;
		this.commandAttributes = commandAttributes;
	}


	public MiniAppCommandID getCommandID() {
		return commandID;
	}


	public void setCommandID(MiniAppCommandID commandID) {
		this.commandID = commandID;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public TargetObject getTargetObject() {
		return targetObject;
	}


	public void setTargetObject(TargetObject targetObject) {
		this.targetObject = targetObject;
	}


	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}


	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}


	public UserAction getInvokedBy() {
		return invokedBy;
	}


	public void setInvokedBy(UserAction invokedBy) {
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
		return "MiniAppCommandBoundary [commandID=" + commandID + ", command=" + command + ", targetObject="
				+ targetObject + ", invocationTimestamp=" + invocationTimestamp + ", invokedBy=" + invokedBy
				+ ", commandAttributes=" + commandAttributes + "]";
	}
	
}