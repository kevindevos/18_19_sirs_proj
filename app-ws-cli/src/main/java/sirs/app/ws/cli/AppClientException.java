package sirs.app.ws.cli;

/** 
 * 
 * Exception to be thrown when something is wrong with the client. 
 * 
 */
public class AppClientException extends Exception {

	private static final long serialVersionUID = 1L;

	public AppClientException() {
		super();
	}

	public AppClientException(String message) {
		super(message);
	}

	public AppClientException(Throwable cause) {
		super(cause);
	}

	public AppClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
