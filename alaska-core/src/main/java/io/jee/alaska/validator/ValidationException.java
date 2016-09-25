package io.jee.alaska.validator;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 800988942007520393L;
	
	public ValidationException(String message) {
		super( message );
	}

	public ValidationException() {
		super();
	}

	public ValidationException(String message, Throwable cause) {
		super( message, cause );
	}

	public ValidationException(Throwable cause) {
		super( cause );
	}

}
