package pt.ulisboa.tecnico.sdis.kerby;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class KerbyManager {
	
	private static final int MIN_TICKET_DURATION = 10;
	private static final int MAX_TICKET_DURATION = 300;
	private static final boolean VALIDATE_NONCE = false;
	private static Set<UserNoncePair> previousNonces = Collections.synchronizedSet(new HashSet<UserNoncePair>());
	private static ConcurrentHashMap<String, Key> knownKeys = new ConcurrentHashMap<String, Key>();
	private static String salt;

	private String passwordFilename;
	
	// Singleton -------------------------------------------------------------
	private KerbyManager() {
	}


    public void addKnownClientKey(String client, Key key){
	    if(knownKeys.get(client) != null){
	        knownKeys.remove(client);
        }

	    knownKeys.put(client, key);
    }

    /**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final KerbyManager INSTANCE = new KerbyManager();
	}

	public static synchronized KerbyManager getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public SessionKeyAndTicketView requestTicket(String client, String server, long nonce, int ticketDuration) 
			throws BadTicketRequestException {
		
		/* Validate parameters */
		if(client == null || client.trim().isEmpty())
			throw new BadTicketRequestException("Null Client.");
		if(knownKeys.get(client) == null)
			throw new BadTicketRequestException("Unknown Client.");
		if(server == null || server.trim().isEmpty())
			throw new BadTicketRequestException("Null Server.");
		if(knownKeys.get(server) == null)
			throw new BadTicketRequestException("Unknown Server.");
		if(ticketDuration < MIN_TICKET_DURATION || ticketDuration > MAX_TICKET_DURATION)
			throw new BadTicketRequestException("Invalid Ticked Duration.");
		
		if(VALIDATE_NONCE) {
			UserNoncePair userNonce = new UserNoncePair(client, nonce);
			if(!previousNonces.add(userNonce))
				throw new BadTicketRequestException("Repeated Nonce, possible Replay Attack.");
		}
		
		try {
			/* Get Previously Generated Client and Server Keys */
			Key clientKey = knownKeys.get(client);
			Key serverKey = knownKeys.get(server);
			
			/* Generate a new key for Client-Server communication */
			Key clientServerKey = SecurityHelper.generateKey();
			
			/* Create and Cipher the Ticket */
			Ticket ticket = createTicket(client, server, ticketDuration, clientServerKey);
			CipheredView cipheredTicket = ticket.cipher(serverKey);
			
			/* Create and Cipher the Session Key */
			SessionKey sessionKey = new SessionKey(clientServerKey, nonce);
			CipheredView cipheredSessionKey = sessionKey.cipher(clientKey);
			
			/* Create SessionKeyAndTicketView */
			SessionKeyAndTicketView response = new SessionKeyAndTicketView();
			response.setTicket(cipheredTicket);
			response.setSessionKey(cipheredSessionKey);
			
			return response;
			
		} catch (NoSuchAlgorithmException e) {
			throw new BadTicketRequestException("Error generating shared key.");
		} catch (KerbyException e) {
			throw new BadTicketRequestException("Error while ciphering.");
		}
	}
	private Ticket createTicket(String client, String server, int ticketDuration, Key clientServerKey) {
		final Calendar calendar = Calendar.getInstance();
		final Date t1 = calendar.getTime();
		calendar.add(Calendar.SECOND, ticketDuration);
		final Date t2 = calendar.getTime();
		Ticket ticket = new Ticket(client, server, t1, t2, clientServerKey);
		return ticket;
	}

	public void revokeKey(String keyOwner){
	    knownKeys.remove(keyOwner);
    }


}
