package handlers;

import org.w3c.dom.Node;
import pt.ulisboa.tecnico.sdis.kerby.*;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClientException;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.StringWriter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 *  This SOAP handler intecerpts the remote calls done by binas-ws-cli for authentication,
 *  and creates a KerbyClient to authenticate with the kerby server in RNL
 */
public class KerbistClientHandler implements SOAPHandler<SOAPMessageContext> {
    protected static final String KERBY_WS_URL = "http://localhost:8888/kerby";
    protected static SecureRandom randomGenerator = new SecureRandom();
    protected static final int VALID_DURATION = 30;

    protected static final String TICKET_ELEMENT_NAME = "ticket";
    protected static final String AUTH_ELEMENT_NAME = "auth";

    protected static CipheredView ticket;
    protected static CipheredView auth;

    protected static String kerbistClientName;
    protected static String kerbistClientPassword;
    protected static String targetWsURL;
    protected static Key kerbistClientKey;
    protected static Key kerbistSessionKey;

    protected static TicketCollection ticketCollection;
    protected static Map<String, Key> sessionKeyMap;


    /**
     * The handleMessage method is invoked for normal processing of inbound and
     * outbound messages.
     */
    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        targetWsURL = (String) smc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);

        if(kerbistClientPassword == null){
            kerbistClientPassword = generateSharedPassword();
        }

        kerbistClientKey = getKerbistClientKey();
        kerbistSessionKey = getSessionKey();

        if(outbound){
            if(getTicket() == null){
                requestNewTicketAndSessionKey();
            }else{
                ticket = getTicket();
                auth = getAuth();
            }

            return handleOutboundMessage(smc);
        }else{
            return handleInboundMessage(smc);
        }
    }

    private boolean handleInboundMessage(SOAPMessageContext smc){
        return true;
    }

    private boolean handleOutboundMessage(SOAPMessageContext smc){
        addTicketToMessage(smc, ticket);
        addAuthToMessage(smc, auth);

        return true;
    }

    private Key getKerbistClientKey(){
        try{
            return SecurityHelper.generateKeyFromPassword(kerbistClientPassword);
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch(InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }

    private Key getSessionKey(){
        SessionKeyAndTicketView sessionKeyAndTicketView = ticketCollection.getTicket(targetWsURL);

        Key key = sessionKeyMap.get(targetWsURL);
        if(key == null){
            try{
                key = SessionKey.makeSessionKeyFromCipheredView(sessionKeyAndTicketView.getSessionKey(), kerbistSessionKey).getKeyXY();
            } catch(KerbyException e){
                e.printStackTrace();
            }
        }

        return key;
    }

    private CipheredView getTicket(){
        return ticketCollection.getTicket(targetWsURL).getTicket();
    }

    private CipheredView getAuth(){
        try{
            return new Auth(kerbistClientName, new Date()).cipher(getSessionKey());
        } catch(KerbyException e){
            e.printStackTrace();
        }

        return null;
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
            int finalValue = kerbyClient.generateDHPassword(kerbistClientName, webValueToShare, g, p);

            // actual password in a string format
            return Integer.toString(finalValue);
        } catch(KerbyClientException e){
            e.printStackTrace();
        }
        return null;

    }

    private void requestNewTicketAndSessionKey(){
        try{
            KerbyClient kerbyClient = new KerbyClient(KERBY_WS_URL);

            // nonce to prevent replay attacks
            long nonce = randomGenerator.nextLong();

            // 1. authenticate user and get ticket and session key by requesting a ticket to kerby
            SessionKeyAndTicketView sessionKeyAndTicketView = kerbyClient.requestTicket(kerbistClientName, targetWsURL, nonce, VALID_DURATION);

            // add to TicketCollection
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, VALID_DURATION);
            long finalValidTime = calendar.getTimeInMillis();
            ticketCollection.storeTicket(targetWsURL, sessionKeyAndTicketView, finalValidTime );

            // 2. generate a key from the webinterface's private password, to decipher and retrieve the session key
            Key webServerKey = SecurityHelper.generateKeyFromPassword(kerbistClientPassword);

            // NOTE: SessionKey : {Kc.s , n}Kc
            // to get the actual session key, we call getKeyXY
            Key sessionKey = SessionKey.makeSessionKeyFromCipheredView(sessionKeyAndTicketView.getSessionKey(), webServerKey).getKeyXY();
            sessionKeyMap.put(targetWsURL, sessionKey);
            // TODO MACHandler

            // 3. save ticket for server
            ticket = sessionKeyAndTicketView.getTicket();

            // 4. create authenticator (Auth)
            Auth authToBeCiphered = new Auth(kerbistClientName, new Date());
            // cipher the auth with the session key Kcs
            auth = authToBeCiphered.cipher(sessionKeyMap.get(targetWsURL));

            System.out.println("requested new ticket and session key");


        } catch(KerbyClientException e){
            e.printStackTrace();
        } catch(BadTicketRequest_Exception e){
            e.printStackTrace();
        } catch(KerbyException e){
            e.printStackTrace();
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch(InvalidKeySpecException e){
            e.printStackTrace();
        }
    }

    private void addTicketToMessage(SOAPMessageContext smc, CipheredView ticket){
        CipherClerk clerk = new CipherClerk();
        try{
            // get soap envelope
            SOAPMessage msg = smc.getMessage();
            SOAPPart sp = msg.getSOAPPart();
            SOAPEnvelope se = sp.getEnvelope();

            // add header if there is none ( se.getHeader() is null if the header doesn't exist )
            SOAPHeader sh = se.getHeader();
            if(sh == null){
                sh = se.addHeader();
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter sw = new StringWriter();

            // create xml node
            Node ticketNode = clerk.cipherToXMLNode(ticket, TICKET_ELEMENT_NAME);

            Name ticketName = se.createName(TICKET_ELEMENT_NAME, "ns1", "urn:ticket");
            SOAPHeaderElement element = sh.addHeaderElement(ticketName);

            // serialize and add to soap message
            transformer.transform(new DOMSource(ticketNode), new StreamResult(sw));
            element.addTextNode(sw.toString());

        } catch(SOAPException e){
            e.printStackTrace();
        } catch(TransformerConfigurationException e){
            e.printStackTrace();
        } catch(TransformerException e){
            e.printStackTrace();
        } catch(JAXBException e){
            e.printStackTrace();
        }
    }

    private void addAuthToMessage(SOAPMessageContext smc, CipheredView auth){
        CipherClerk clerk = new CipherClerk();
        try{
            // get soap envelope
            SOAPMessage msg = smc.getMessage();
            SOAPPart sp = msg.getSOAPPart();
            SOAPEnvelope se = sp.getEnvelope();


            // add header if there is none ( se.getHeader() is null if the header doesn't exist )
            SOAPHeader sh = se.getHeader();
            if(sh == null){
                sh = se.addHeader();
            }

            // create xml node
            Node authNode = clerk.cipherToXMLNode(auth, AUTH_ELEMENT_NAME);

            Name authName = se.createName(AUTH_ELEMENT_NAME, "ns1" ,"urn:auth");
            SOAPHeaderElement element2 = sh.addHeaderElement(authName);

            // serialize the auth node and add to soap message
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter sw = new StringWriter();

            transformer.transform(new DOMSource(authNode), new StreamResult(sw));
            element2.addTextNode(sw.toString());


        } catch(SOAPException e){
            e.printStackTrace();
        } catch(JAXBException e){
            e.printStackTrace();
        } catch(TransformerConfigurationException e){
            e.printStackTrace();
        } catch(TransformerException e){
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean handleFault(SOAPMessageContext context){
        return false;
    }

    @Override
    public void close(MessageContext context){

    }


    /**
     * Gets the header blocks that can be processed by this Handler instance. If
     * null, processes all.
     */
    @Override
    public Set<QName> getHeaders() {
        return null;
    }

}