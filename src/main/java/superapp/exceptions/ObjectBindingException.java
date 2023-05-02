package superapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ObjectBindingException extends RuntimeException {
	private static final long serialVersionUID = 8242664202862712681L;

	public ObjectBindingException() {
	}

	public ObjectBindingException(String message) {
		super(message);
	}

	public ObjectBindingException(Throwable cause) {
		super(cause);
	}

	public ObjectBindingException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
