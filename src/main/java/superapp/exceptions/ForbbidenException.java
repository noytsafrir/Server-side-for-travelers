package superapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import superapp.utils.ResourceIdentifier;


@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbbidenException extends RuntimeException {
	private static final long serialVersionUID = 4293997442861105632L;

	public ForbbidenException() {
	}

	public ForbbidenException(String message) {
		super(message);
	}

	public ForbbidenException(Throwable cause) {
		super(cause);
	}

	public ForbbidenException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ForbbidenException(ResourceIdentifier userId, String operation) {
		super("Could not " + operation + " because " + userId + " does not have permissions");
	}

}
