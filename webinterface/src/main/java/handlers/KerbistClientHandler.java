package handlers;

import pt.ulisboa.tecnico.sdis.kerby.*;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClientException;
import sirs.webinterface.domain.WebInterfaceManager;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
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
        String endpointAddress = (String) smc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);

        if(WebInterfaceManager.PASSWORD == null){
            WebInterfaceManager.PASSWORD = generateSharedPassword();
        }

        if(outbound){
            if(!isTicketValid(endpointAddress)){
                System.out.println("Requesting new ticket and session key from Kerbi");
                requestNewTicketAndSessionKey(endpointAddress);
            }

            initCipheredTicketAndAuth();


            return handleOutboundMessage(smc);
        }else{
            return handleInboundMessage(smc);
        }
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
            int finalValue = kerbyClient.generateDHPassword(WebInterfaceManager.WEB_SERVER_NAME, webValueToShare, g, p);

            // actual password in a string format
            return Integer.toString(finalValue);
        } catch(KerbyClientException e){
            e.printStackTrace();
        }

    }

    private void initCipheredTicketAndAuth(){
    }

    private void requestNewTicketAndSessionKey(String serverWsUrl){
        try{
            KerbyClient kerbyClient = new KerbyClient(KERBY_WS_URL);

            // nonce to prevent replay attacks
            long nonce = randomGenerator.nextLong();

            // 1. authenticate user and get ticket and session key by requesting a ticket to kerby
            SessionKeyAndTicketView sessionKeyAndTicketView = kerbyClient.requestTicket(WebInterfaceManager.WEB_SERVER_NAME, serverWsUrl, nonce, VALID_DURATION);

            // add to TicketCollection
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, VALID_DURATION);
            long finalValidTime = calendar.getTimeInMillis();
            WebInterfaceManager.ticketCollection.storeTicket(serverWsUrl, sessionKeyAndTicketView, finalValidTime );

            // 2. generate a key from the webinterface's private password, to decipher and retrieve the session key
            Key webServerKey = SecurityHelper.generateKeyFromPassword(WebInterfaceManager.PASSWORD);

            // NOTE: SessionKey : {Kc.s , n}Kc
            // to get the actual session key, we call getKeyXY
            Key sessionKey = SessionKey.makeSessionKeyFromCipheredView(sessionKeyAndTicketView.getSessionKey(), webServerKey).getKeyXY();
            WebInterfaceManager.getInstance().addSessionKey(serverWsUrl, sessionKey);
           // TODO MACHandler

            // 3. save ticket for server
            ticket = sessionKeyAndTicketView.getTicket();

            // 4. create authenticator (Auth)
            Auth authToBeCiphered = new Auth(WebInterfaceManager.WEB_SERVER_NAME, new Date());
            // cipher the auth with the session key Kcs
            auth = authToBeCiphered.cipher(WebInterfaceManager.getInstance().getSessionKey(serverWsUrl));

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

    /**
     * Check if ticket is valid, note, we use wsurl as the server name aswell
     * @param wsUrl
     * @return
     */
    private boolean isTicketValid(String wsUrl){
        return WebInterfaceManager.ticketCollection.getTicket(wsUrl) != null;
    }

    private boolean handleInboundMessage(SOAPMessageContext smc){
        return true;
    }

    private boolean handleOutboundMessage(SOAPMessageContext smc){
        addTicketAndAuthToMessage(smc);
        
        return true;
    }

    private void addTicketAndAuthToMessage(SOAPMessageContext smc){
    }

    @Override
    public boolean handleFault(SOAPMessageContext context){
        return false;
    }

    @Override
    public void close(MessageContext context){

    }


}