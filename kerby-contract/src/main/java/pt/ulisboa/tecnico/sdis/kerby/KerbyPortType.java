package pt.ulisboa.tecnico.sdis.kerby;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Kerby Web Service Port Type (equivalent to a Java interface).
 * 
 * @author Miguel Pardal
 *
 */
@WebService
public interface KerbyPortType {

	/**
	 * Step 1 of the simplified Kerberos protocol: the client sends its id, the
	 * server id and a nounce. And receives sealed session key and ticket.
	 */
	public SessionKeyAndTicketView requestTicket(@WebParam(name = "client") String client,
			@WebParam(name = "server") String server, @WebParam(name = "nounce") long nounce,
			@WebParam(name = "ticketDuration") int ticketDuration) throws BadTicketRequest;

	/** dummy operation to force schema generation of all views. */
	public String dummy(AuthView av, RequestTimeView rtv, CipheredView sv, SessionKeyView skv, TicketView tv);

}
