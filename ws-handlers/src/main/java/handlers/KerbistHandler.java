package handlers;

import pt.ulisboa.tecnico.sdis.kerby.*;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClientException;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public abstract class KerbistHandler implements SOAPHandler<SOAPMessageContext> {
    protected static final String KERBY_WS_URL = "http://localhost:8888/kerby";
    protected static SecureRandom randomGenerator = new SecureRandom();
    protected static final int VALID_DURATION = 30;

    protected static final String TICKET_ELEMENT_NAME = "ticket";
    protected static final String AUTH_ELEMENT_NAME = "auth";

    protected String kerbistClientPassword;
    protected Key kerbistClientKey;

    protected TicketCollection ticketCollection;
    protected Map<String, Key> sessionKeyMap;

    protected String targetWsURL;

    protected static String kerbistName;

    /**
     * Perform a Diffie-Helmann exchange with the kerby server, resulting in a shared password secretly generated
     * This password can then be used to generate a key with SecurityHelper.generateKeyFromPassword
     * @return password string
     */
    protected String generateSharedPassword(){
        Random rand = new Random();
        // generate public ints to be shared, base g, and modulus p
        int g = rand.nextInt(10000) + 100;
        int p = rand.nextInt(10000) + 10;

        // generate our secret value
        int myPower = rand.nextInt(10000);
        int valueToShare = ((int) Math.pow(g, myPower)) % p;

        KerbyClient kerbyClient = null;
        try{
            kerbyClient = new KerbyClient(KERBY_WS_URL);
            int serverValue = kerbyClient.generateDHPassword(kerbistName, valueToShare, g, p);

            int finalValue = ((int) Math.pow(g, myPower)) % p;
            System.err.println(kerbistName + " : Generated DH number: " + finalValue);
            // actual password in a string format
            return Integer.toString(finalValue);
        } catch(KerbyClientException e){
            e.printStackTrace();
        }
        return null;

    }




    /**
     * Sets up variables required for the handler to work
     */
    protected abstract void initHandlerVariables();


    /**
     * Gets the header blocks that can be processed by this Handler instance. If
     * null, processes all.
     */
    @Override
    public Set<QName> getHeaders() {
        return null;
    }


    @Override
    public void close(MessageContext context){

    }
}
