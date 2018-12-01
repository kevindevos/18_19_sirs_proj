package handlers;

import pt.ulisboa.tecnico.sdis.kerby.*;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Map;
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

    protected String kerbistName;

    /**
     * Perform a Diffie-Helmann exchange with the kerby server, resulting in a shared password secretly generated
     * This password can then be used to generate a key with SecurityHelper.generateKeyFromPassword
     * @return password string
     */
    protected String generateSharedPassword(){
        KerbyClient kerbyClient = new KerbyClient(KERBY_WS_URL);

        return kerbyClient.generateDHPassword(kerbistName);
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
