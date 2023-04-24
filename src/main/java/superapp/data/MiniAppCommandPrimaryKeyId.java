package superapp.data;

public class MiniAppCommandPrimaryKeyId{
	private String superapp;
	private String miniapp;
	private String internalCommandId;


	public MiniAppCommandPrimaryKeyId() {

	}

	public MiniAppCommandPrimaryKeyId(String superapp, String miniapp, String internalCommandId) {
		super();
		this.superapp = superapp;
		this.miniapp = miniapp;
		this.internalCommandId = internalCommandId;
	}

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

	@Override
	public String toString() {
		return "MiniAppCommandPrimaryKeyId [superapp=" + superapp + ", miniapp=" + miniapp + ", internalCommandId="
				+ internalCommandId + "]";
	}



}