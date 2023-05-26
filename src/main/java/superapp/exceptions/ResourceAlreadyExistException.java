package superapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.CONFLICT)
public class ResourceAlreadyExistException extends RuntimeException {
	private static final long serialVersionUID = 6385170380829121646L;

	public ResourceAlreadyExistException() {
	}

	public ResourceAlreadyExistException(String message) {
		super(message);
	}

	public ResourceAlreadyExistException(Throwable cause) {
		super(cause);
	}

	public ResourceAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ResourceAlreadyExistException(Object id, String operation) {
		super("Could not " + operation + " because " + id + " already exist");
	}
	public ResourceAlreadyExistException(String resourceType, Object id) {
		super(resourceType + " " + id + " already exist");
	}
	
	public ResourceAlreadyExistException(Object id, String resourceType, String operation) {
		super("Could not " + operation + " because " + resourceType + " " + id + " already exist");
	}

}
