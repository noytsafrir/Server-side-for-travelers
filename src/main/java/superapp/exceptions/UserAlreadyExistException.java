package superapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 6385170380829121646L;

	public UserAlreadyExistException() {
	}

	public UserAlreadyExistException(String message) {
		super(message);
	}

	public UserAlreadyExistException(Throwable cause) {
		super(cause);
	}

	public UserAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

}
