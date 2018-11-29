package handlers;

import pt.ulisboa.tecnico.sdis.kerby.*;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.security.SecureRandom;
import java.util.Set;

/**
 *  This SOAP handler intecerpts the remote calls done by binas-ws-cli for authentication,
 *  and creates a KerbyClient to authenticate with the kerby server in RNL
 */
public class KerbistClientHandler implements SOAPHandler<SOAPMessageContext> {
    public static final String KERBY_WS_URL = "http://localhost:8888/kerby";
    private static SecureRandom randomGenerator = new SecureRandom();
    private static final int VALID_DURATION = 30;

    private static final String TICKET_ELEMENT_NAME = "ticket";
    private static final String AUTH_ELEMENT_NAME = "auth";

    private static CipheredView ticket;
    private static CipheredView auth;


    /**
     * Gets the header blocks that can be processed by this Handler instance. If
     * null, processes all.
     */
    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    /**
     * The handleMessage method is invoked for normal processing of inbound and
     * outbound messages.
     */
    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context){
        return false;
    }

    @Override
    public void close(MessageContext context){

    }


}