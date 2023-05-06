package superapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import superapp.utils.ResourceIdentifier;


@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -386098799657411035L;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ResourceNotFoundException(ResourceIdentifier id) {
		super(id + " does not exist");
	}
	
	public ResourceNotFoundException(ResourceIdentifier id, String operation) {
		super("Could not " + operation + " because " + id + " does not exist");
	}
	
	public ResourceNotFoundException(String resourceType, ResourceIdentifier id) {
		super(resourceType + " " + id + " does not exist");
	}
	
	public ResourceNotFoundException(ResourceIdentifier id, String resourceType, String operation) {
		super("Could not " + operation + " because " + resourceType + " " + id + " does not exist");
	}
	
}
