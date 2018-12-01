package pt.ulisboa.tecnico.sdis.kerby.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;
import java.util.Random;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.kerby.*;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

/**
 * Client port wrapper.
 *
 * Adds easier end point address configuration to the Port generated by
 * wsimport.
 */
public class KerbyClient{

	/** WS service */
	KerbyService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	KerbyPortType port = null;

	/** UDDI server URL */
	private String uddiURL = null;

	/** WS name */
	private String wsName = null;

	/** WS end point address */
	private String wsURL = null; // default value is defined inside WSDL

	public String getWsURL() {
		return wsURL;
	}

	/** output option **/
	private boolean verbose = false;

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/** constructor with provided web service URL */
	public KerbyClient(String wsURL) throws KerbyClientException {
		this.wsURL = wsURL;
		createStub();
	}

	/** constructor with provided UDDI location and name */
	public KerbyClient(String uddiURL, String wsName) throws KerbyClientException {
		this.uddiURL = uddiURL;
		this.wsName = wsName;
		uddiLookup();
		createStub();
	}

	/** UDDI lookup */
	private void uddiLookup() throws KerbyClientException {
		try {
			if (verbose)
				System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);

			if (verbose)
				System.out.printf("Looking for '%s'%n", wsName);
			wsURL = uddiNaming.lookup(wsName);

		} catch (Exception e) {
			String msg = String.format("Client failed lookup on UDDI at %s!", uddiURL);
			throw new KerbyClientException(msg, e);
		}

		if (wsURL == null) {
			String msg = String.format("Service with name %s not found on UDDI at %s", wsName, uddiURL);
			throw new KerbyClientException(msg);
		}
	}

	/** Stub creation and configuration */
	private void createStub() {
		if (verbose)
			System.out.println("Creating stub ...");
		service = new KerbyService();
		port = service.getKerbyPort();

		if (wsURL != null) {
			if (verbose)
				System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, wsURL);
		}
	}

	// remote invocation methods ----------------------------------------------

	public SessionKeyAndTicketView requestTicket(String client, String server, long nounce, int ticketDuration)
			throws BadTicketRequest_Exception{
		return port.requestTicket(client, server, nounce, ticketDuration);
	}

    public void revokeKey(String keyOwner){
        port.revokeKey(keyOwner);
    }

    public String generateDHPassword(String client){
        Random rand = new Random();
        // generate public ints to be shared, base g, and modulus p
        int g = rand.nextInt(10000) + 100;
        int p = rand.nextInt(10000) + 10;

        // generate our secret value
        int myPower = rand.nextInt(10000);
        int valueToShare = ((int) Math.pow(g, myPower)) % p;

        int serverValue = port.generateDHPassword(client, valueToShare, g, p);
        int finalValue = ((int) Math.pow(g, myPower)) % p;
        System.err.println(client + " : Generated DH number: " + finalValue);

        return Integer.toString(finalValue);
    }

}
