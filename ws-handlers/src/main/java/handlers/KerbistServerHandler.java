package handlers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pt.ulisboa.tecnico.sdis.kerby.*;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;

/**
 *  This SOAP handler intecepts the remote calls done by binas-ws-cli for authentication,
 *  and creates a KerbyClient to authenticate with the kerby server in RNL
 */
public abstract class KerbistServerHandler extends KerbistHandler  {
    private static final String TICKET_ELEMENT_NAME = "ticket";
    protected static final String AUTH_ELEMENT_NAME = "auth";

    protected static String serverPassword;
    protected static Key serverKey;
    protected static Key sessionKey;

    private CipheredView cipheredTicketView;
    private CipheredView cipheredAuthView;


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
       // cipherSoapBodyWithSessionKey(smc, sessionKey);
        return true;
    }

    /** Handles inbound messages */
    private boolean handleInboundMessage(SOAPMessageContext smc) {
        System.out.println("received a message");
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

        // decipher the body
       // decipherSoapBodyWithSessionKey(smc, sessionKey);
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





}