package pt.ulisboa.tecnico.sdis.kerby;

import javax.jws.WebService;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

/**
 * Kerby Web Service implementation class.
 * 
 * @author Miguel Pardal
 *
 */
@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.kerby.KerbyPortType",
wsdlLocation = "KerbyService_SIRS.wsdl",
name ="KerbyService",
portName = "KerbyPort",
targetNamespace="http://kerby.sdis.tecnico.ulisboa.pt/",
serviceName = "KerbyService"
)
public class KerbyPortImpl implements KerbyPortType {

    public KerbyPortImpl(){}

	// end point manager
	private KerbyEndpointManager endpointManager;

	public KerbyPortImpl(KerbyEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	@Override
	public SessionKeyAndTicketView requestTicket(String client, String server, long nounce, int ticketDuration)
            throws BadTicketRequest_Exception{
		SessionKeyAndTicketView result = new SessionKeyAndTicketView();
		KerbyManager manager = KerbyManager.getInstance();
		
		try {
			result = manager.requestTicket(client, server, nounce, ticketDuration);
		} catch (BadTicketRequestException e) {
			throwBadTicketRequest(e.getMessage());
		}
		
		return result;
	}

    @Override
    public void revokeKey(String keyOwner){
        KerbyManager.getInstance().revokeKey(keyOwner);
    }



    @Override
    public int generateDHPassword(String client, Integer value, int g, int p){
        Random rand = new Random();
        int kerbyPower = rand.nextInt(100);

        int finalValue = ((int) Math.pow(value, kerbyPower)) % p; // final int value
        String finalPassword = Integer.toString(finalValue);

        try{
            KerbyManager.getInstance().addKnownClientKey(client, SecurityHelper.generateKeyFromPassword(finalPassword));
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch(InvalidKeySpecException e){
            e.printStackTrace();
        }

        return ((int) Math.pow(g, kerbyPower)) % p; // to send back
    }


    // Exception helper -----------------------------------------------------
	
	private void throwBadTicketRequest(final String message) throws BadTicketRequest_Exception {
		BadTicketRequest faultInfo = new BadTicketRequest();
		faultInfo.setMessage(message);
		throw new BadTicketRequest_Exception(message, faultInfo);
	}

}
