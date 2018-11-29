package pt.ulisboa.tecnico.sdis.kerby;

/**
 * Thrown to indicate that a bad ticket request has been passed.
 * 
 * @author Miguel Pardal
 *
 */
public class BadTicketRequest extends Exception {

	private static final long serialVersionUID = 1L;

	public BadTicketRequest() {
	}

	public BadTicketRequest(String message) {
		super(message);
	}

	public BadTicketRequest(Throwable cause) {
		super(cause);
	}

	public BadTicketRequest(String message, Throwable cause) {
		super(message, cause);
	}

	public BadTicketRequest(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
