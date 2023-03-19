package demo;

import java.util.Date;
import java.util.Map;

public class Message {
	private Long id;
	private String message;
	private NameBoundary name;
	private Boolean important;
	private Double version;
	private Date createdOn;
	private Map<String,Object> data;


	public Message() {}

	public Message(String message) {
		this.message = message;
	}

	public NameBoundary getName() {
		return name;
	}
	public void setName(NameBoundary name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Boolean getImportant() {
		return important;
	}
	public void setImportant(Boolean important) {
		this.important = important;
	}
	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	
	@Override
	public String toString() {
		return "Message [id=" + id + ", message=" + message + ", name=" + name + ", important=" + important
				+ ", version=" + version + ", createdOn=" + createdOn + ", data=" + data + "]";
	}
}

	
	
	

	
	
	
	