package pt.ulisboa.tecnico.sdis.kerby;

import javax.jws.WebService;

/**
 * Kerby Web Service implementation class.
 * 
 * @author Miguel Pardal
 *
 */
@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.kerby.KerbyPortType")
public class Kerby implements KerbyPortType {

	@Override
	public SessionKeyAndTicketView requestTicket(String client, String server, long nounce, int ticketDuration) throws BadTicketRequest {
		return null;
	}

	@Override
	public String dummy(AuthView av, RequestTimeView rtv, CipheredView sv, SessionKeyView skv, TicketView tv) {
		return Kerby.class.getSimpleName();
	}

}
