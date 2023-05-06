package superapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import superapp.utils.BoundaryObject;


@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidInputException extends RuntimeException {
	private static final long serialVersionUID = -2310460623973054315L;

	public InvalidInputException() {
	}

	public InvalidInputException(String message) {
		super(message);
	}

	public InvalidInputException(Throwable cause) {
		super(cause);
	}

	public InvalidInputException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidInputException(BoundaryObject obj, String operation) {
		super("Could not " + operation + " because invalid input: " + obj);
	}
	public InvalidInputException(String resourceType, BoundaryObject obj) {
		super(resourceType + "input is invalid: " + obj);
	}
	
	public InvalidInputException(BoundaryObject obj, String resourceType, String operation) {
		super("Could not " + operation + " because " + resourceType + " input is invalid: " + obj);
	}

}
