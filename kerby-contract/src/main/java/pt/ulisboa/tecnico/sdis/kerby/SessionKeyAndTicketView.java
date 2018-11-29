package pt.ulisboa.tecnico.sdis.kerby;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "sessionKey", "ticket" })
public class SessionKeyAndTicketView implements Serializable {

	private static final long serialVersionUID = 1L;

	private CipheredView sessionKey;
	private CipheredView ticket;

	public CipheredView getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(CipheredView sessionKey) {
		this.sessionKey = sessionKey;
	}

	public CipheredView getTicket() {
		return ticket;
	}

	public void setTicket(CipheredView ticket) {
		this.ticket = ticket;
	}

}
