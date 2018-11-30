package handlers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pt.ulisboa.tecnico.sdis.kerby.*;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClientException;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 *  This SOAP handler intecepts the remote calls done by binas-ws-cli for authentication,
 *  and creates a KerbyClient to authenticate with the kerby server in RNL
 */
public abstract class KerbistServerHandler implements SOAPHandler<SOAPMessageContext> {
    protected static final String KERBY_WS_URL = "http://localhost:8888/kerby";
    private static final String TICKET_ELEMENT_NAME = "ticket";
    protected static final String AUTH_ELEMENT_NAME = "auth";

    protected String serverName;
    protected String serverPassword;
    protected Key serverKey;
    protected Key sessionKey;

    private CipheredView cipheredTicketView;
    private CipheredView cipheredAuthView;

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

    /**
     * The handleMessage method is invoked for normal processing of inbound and
     * outbound messages.
     */
    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        initHandlerVariables();

        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if(serverPassword == null){
            serverPassword = generateSharedPassword();
        }

        serverKey = getServerKey();

        if(outbound)
            return handleOutboundMessage(smc);
        else
            return handleInboundMessage(smc);

    }

    /** Handles outbound messages */
    private boolean handleOutboundMessage(SOAPMessageContext smc){
        return true;
    }

    /** Handles inbound messages */
    private boolean handleInboundMessage(SOAPMessageContext smc) {
        retrieveTicketAndAuthFromMessageHeaders(smc);

        try{
            // get the ticket and validate it
            Ticket ticket = Ticket.makeTicketFromCipheredView(cipheredTicketView, serverKey);
            ticket.validate();
            // TODO      AuthorizationHandler

            sessionKey = ticket.getKeyXY();
            // TODO MACHandler.sessionKey = (SecretKey) BinasPortImpl.kcsSessionKey;

            // get the auth and validate it
            Auth auth = Auth.makeAuthFromCipheredView(cipheredAuthView, sessionKey);
            auth.validate();

        } catch(KerbyException e){
            // Ticket is invalid! send back to client an exception
            throw new RuntimeException("InvalidTicket");
        }

        // at this point both ticket and auth were valid, so we return true
        return true;
    }

    /** Get ticket and auth from the soap message headers */
    private boolean retrieveTicketAndAuthFromMessageHeaders(SOAPMessageContext smc){
        // get first header element
        StringWriter sw = new StringWriter();
        CipherClerk clerk = new CipherClerk();

        try{
            // get SOAP envelope header
            SOAPMessage msg = smc.getMessage();
            SOAPPart sp = msg.getSOAPPart();
            SOAPEnvelope se = sp.getEnvelope();
            SOAPHeader sh = se.getHeader();

            // check header
            if (sh == null) {
                System.out.println("Header not found.");
                return true;
            }

            Name ticketName = se.createName(TICKET_ELEMENT_NAME);

            Iterator it = sh.getChildElements();
            // check header element
            if (!it.hasNext()) {
                System.out.printf("Header element %s not found.%n", TICKET_ELEMENT_NAME);
                return true;
            }


            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            // ----------------- TICKET ----------------------

            SOAPElement ticketSOAPElement = (SOAPElement) it.next();
            Document ticketDocument = builder.parse(new InputSource(new StringReader(ticketSOAPElement.getValue())));

            DOMSource ticketDOMSource = new DOMSource(ticketDocument);
            Node ticketNode = ticketDOMSource.getNode();

            cipheredTicketView = clerk.cipherFromXMLNode(ticketNode);

            // -----------------  AUTH  ----------------------

            SOAPElement authSOAPElement = (SOAPElement) it.next();
            Document authDocument = builder.parse(new InputSource(new StringReader(authSOAPElement.getValue())));

            DOMSource authDOMSource = new DOMSource(authDocument);
            Node authNode = authDOMSource.getNode();

            cipheredAuthView = clerk.cipherFromXMLNode(authNode);

        } catch(SOAPException e){
            e.printStackTrace();
        } catch(ParserConfigurationException e){
            e.printStackTrace();
        } catch(SAXException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(JAXBException e){
            e.printStackTrace();
        }
        return false;
    }

    private Key getServerKey(){
        try{
            return SecurityHelper.generateKeyFromPassword(serverPassword);
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch(InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }

    /** The handleFault method is invoked for fault message processing. */
    @Override
    public boolean handleFault(SOAPMessageContext smc) {
        System.out.println("Ignoring fault message...");
        return true;
    }

    /**
     * Called at the conclusion of a message exchange pattern just prior to the
     * JAX-WS runtime dispatching a message, fault or exception.
     */
    @Override
    public void close(MessageContext messageContext) {
    }

    /**
     * Perform a Diffie-Helmann exchange with the kerby server, resulting in a shared password secretly generated
     * This password can then be used to generate a key with SecurityHelper.generateKeyFromPassword
     * @return password string
     */
    private String generateSharedPassword(){
        Random rand = new Random();
        // generate public ints to be shared, base g, and modulus p
        int g = rand.nextInt(1000);
        int p = rand.nextInt(100);

        // generate our secret value
        int webPower = rand.nextInt(100);
        int webValueToShare = ((int) Math.pow(g, webPower)) % p;

        KerbyClient kerbyClient = null;
        try{
            kerbyClient = new KerbyClient(KERBY_WS_URL);
            int finalValue = kerbyClient.generateDHPassword(serverName, webValueToShare, g, p);

            // actual password in a string format
            return Integer.toString(finalValue);
        } catch(KerbyClientException e){
            e.printStackTrace();
        }
        return null;

    }


}