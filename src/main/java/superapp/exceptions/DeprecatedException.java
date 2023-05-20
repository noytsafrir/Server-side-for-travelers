package superapp.exceptions;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.GONE)
public class DeprecatedException extends RuntimeException {
	private static final long serialVersionUID = 5513112287852203407L;

	public DeprecatedException() {
	}

	public DeprecatedException(String message) {
		super(message);
	}

	public DeprecatedException(Throwable cause) {
		super(cause);
	}

	public DeprecatedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DeprecatedException(LocalDate date) {
		super("Do not use this operation, it's depracated since " + date.toString());
	}

}
