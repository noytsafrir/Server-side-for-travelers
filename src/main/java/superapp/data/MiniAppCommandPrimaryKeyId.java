package superapp.data;

import java.util.Objects;

public class MiniAppCommandPrimaryKeyId {
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

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof MiniAppCommandPrimaryKeyId)) {
            return false;
        }
        
        MiniAppCommandPrimaryKeyId other = (MiniAppCommandPrimaryKeyId) o;
        return Objects.equals(this.superapp, other.getSuperapp()) &&
               Objects.equals(this.miniapp, other.getMiniapp()) &&
               Objects.equals(this.internalCommandId, other.getInternalCommandId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.superapp, this.miniapp, this.internalCommandId);
    }

}