package handlers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pt.ulisboa.tecnico.sdis.kerby.*;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class KerbistHandler implements SOAPHandler<SOAPMessageContext> {
    protected static SecureRandom randomGenerator = new SecureRandom();
    protected static final int VALID_DURATION = 30;

    protected static final String TICKET_ELEMENT_NAME = "ticket";
    protected static final String AUTH_ELEMENT_NAME = "auth";
    public static final String CIPHERED_BODY_ELEMENT_NAME = "CipheredBody";

    protected String kerbistClientPassword;
    protected Key kerbistClientKey;

    protected TicketCollection ticketCollection;
    protected Map<String, Key> sessionKeyMap;

    protected String targetName;
    protected String kerbistName;

    /**
     * Perform a Diffie-Helmann exchange with the kerby server, resulting in a shared password secretly generated
     * This password can then be used to generate a key with SecurityHelper.generateKeyFromPassword
     * @return password string
     */
    protected String generateSharedPassword(){
        KerbyClient kerbyClient = new KerbyClient();

        return kerbyClient.generateDHPassword(kerbistName);
    }

    protected void decipherSoapBodyWithSessionKey(SOAPMessageContext smc, Key sessionKey){
        try{
            SOAPMessage msg = smc.getMessage();
            SOAPPart soapPart = msg.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Iterator it = soapBody.getChildElements();

            if(it.hasNext()){
                SOAPElement firstElement = (SOAPElement) it.next();

                Document cipheredElementDocument = builder.parse(new InputSource(new StringReader(firstElement.getValue())));
                DOMSource cipheredElementSource = new DOMSource(cipheredElementDocument);
                Node cipheredElementNode = cipheredElementSource.getNode();

                // create a CipheredView from the xml node
                CipherClerk clerk = new CipherClerk();
                CipheredView cipheredElementView = clerk.cipherFromXMLNode(cipheredElementNode);

                // decipher the CipheredView's data
                CipheredView decipheredElementView = SecurityHelper.decipher(CipheredView.class, cipheredElementView, sessionKey);
                Node decipheredElementNode = clerk.cipherToXMLNode(decipheredElementView, CIPHERED_BODY_ELEMENT_NAME);

                // add the deciphered body node back with the same name
                QName name = new QName(decipheredElementNode.getLocalName());
                SOAPBodyElement soapBodyElement = soapBody.addBodyElement(name);
                soapBodyElement.addTextNode(decipheredElementNode.getTextContent());

                return;
            }

        } catch(SOAPException e){
            e.printStackTrace();
        } catch(ParserConfigurationException e){
            e.printStackTrace();
        } catch(SAXException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(KerbyException e){
            e.printStackTrace();
        } catch(JAXBException e){
            e.printStackTrace();
        }

    }

    protected void cipherSoapBodyWithSessionKey(SOAPMessageContext smc, Key sessionKey){
        try{
            SOAPMessage msg = smc.getMessage();
            SOAPPart soapPart = msg.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();


            // cipher the body's first child
            Node firstBodyChild = soapBody.getFirstChild();
            soapBody.removeChild(soapBody.getFirstChild());

            CipherClerk cipherClerk = new CipherClerk();
            CipheredView bodyFirstChildView = cipherClerk.cipherFromXMLNode(firstBodyChild);
            CipheredView cipheredBodyFirstChild = SecurityHelper.cipher(CipheredView.class, bodyFirstChildView, sessionKey);

            // convert back to node
            Node cipheredNode = cipherClerk.cipherToXMLNode(cipheredBodyFirstChild, CIPHERED_BODY_ELEMENT_NAME);

            // instantiate util objs
            StringWriter sw = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            // add a body element to the soap message
            QName cipheredBodyName = new QName("http://ws.app.sirs/", CIPHERED_BODY_ELEMENT_NAME, "m");
            SOAPBodyElement bodyElement = soapBody.addBodyElement(cipheredBodyName);

            // serialize the node and add
            transformer.transform(new DOMSource(cipheredNode), new StreamResult(sw));
            bodyElement.addTextNode(sw.toString());
        } catch(SOAPException e){
            e.printStackTrace();
        } catch(KerbyException e){
            e.printStackTrace();
        } catch(JAXBException e){
            e.printStackTrace();
        } catch(TransformerConfigurationException e){
            e.printStackTrace();
        } catch(TransformerException e){
            e.printStackTrace();
        }

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
