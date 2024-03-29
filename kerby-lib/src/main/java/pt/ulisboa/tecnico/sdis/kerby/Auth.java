package pt.ulisboa.tecnico.sdis.kerby;

import org.w3c.dom.Node;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.security.Key;
import java.util.Date;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.*;
/**
 * Class that represents a Kerberos auth and can use different data formats.
 * 
 * @author Miguel Amaral
 *
 */
public class Auth {

	/** Auth data container. After creation, cannot be null. */
	private AuthView view;

	// Auth creation -------------------------------------------------------

	/** Create a default view */
	//public Auth() {
	//	AuthView view = new AuthView();
	//	view.setTimeRequest(dateToXML(new Date()));
	//	view.setX("X");
	//	setView(view);
	//}
	
	/** Create AuthView from arguments. 
	 * @param x A String with the Client Name.
	 * @param timeRequest The Date of the Request.
	 * @return AuthView created from arguments.
	 * */
	public AuthView authBuild(String x, Date timeRequest) {
		AuthView auth = new AuthView();
		auth.setTimeRequest(dateToXML(timeRequest));
		auth.setX(x);
		return auth;
	}
	
	/** Create auth from data view. 
	 * */
	public Auth(AuthView view) {
		setView(view);
	}
	
	/** Create Auth from arguments.
	 * @param x A String with the Client Name.
	 * @param timeRequest The Date of the Request.
	 * */
	public Auth(String x, Date timeRequest) {
		setView(authBuild(x, timeRequest));
	}
	
	
	/** Create Auth from an XML Node.
	 * @param node An XML Node containing an Auth
	 * */
	public static Auth makeAuthFromXMLNode(Node node) throws JAXBException {
		AuthView view = fromXMLNode(node);
		return new Auth(view);
	}
	
	/** Create Auth from a Ciphered AuthView.
	 * @param view A Ciphered AuthView.
	 * @param key The Key used to decipher the View.
	 * */
	public static Auth makeAuthFromCipheredView(CipheredView cipheredView, Key key) throws KerbyException {
		AuthView view = decipher(cipheredView, key);
		return new Auth(view);
	}
	
	/** Create Auth from a XML Bytes.
	 * @param node A byte array representing an Auth
	 * */
	public static Auth makeAuthFromXMLBytes(byte[] bytes) throws JAXBException {
		AuthView view = fromXMLBytes(bytes);
		return new Auth(view);
	}

	/** Create Auth from a Base64 String.
	 * @param node A Base64 String representing an Auth
	 * */
	public static Auth makeAuthFromBase64String(String base64String) throws JAXBException {
		AuthView view = fromBase64String(base64String);
		return new Auth(view);
	}
	
	// After construction, view can never be null, and can never be set to null.
	// This invariant is assumed to be true in the remaining code.

	// accessors -------------------------------------------------------------

	protected AuthView getView() {
		return view;
	}

	protected void setView(AuthView view) {
		if (view == null)
			throw new IllegalArgumentException("View cannot be null!");
		this.view = view;
	}

	public String getX() {
		return view.getX();
	}

	public void setX(String x) {
		view.setX(x);
	}

	public Date getTimeRequest() {
		Date time = xmlToDate(view.getTimeRequest());
		return time;
	}

	public void setTimeRequest(Date timeRequest) {
		XMLGregorianCalendar xgc = dateToXML(timeRequest);
		view.setTimeRequest(xgc);
	}

	// object methods --------------------------------------------------------

	/** Create a textual representation of the AuthView. 
	 * @return String representation of AuthView. */
	public String authToString() {
		if (view == null) {
			return "null";
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("AuthView [x=");
		builder.append(view.getX());
		builder.append(", timeRequest=");
		builder.append(view.getTimeRequest());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((view.getTimeRequest() == null) ? 0 : view.getTimeRequest().hashCode());
		result = prime * result + ((view.getX() == null) ? 0 : view.getX().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Auth other = (Auth) obj;
		AuthView otherView = other.getView();

		return authViewEquals(view, otherView);
	}

	/** Compare contents of two auth views. */
	protected boolean authViewEquals(AuthView view1, AuthView view2) {
		if (view1 == view2)
			return true;
		if (view2 == null)
			return false;
		if (view1.getClass() != view2.getClass())
			return false;
		if (view1.getTimeRequest() == null) {
			if (view2.getTimeRequest() != null)
				return false;
		} else if (!view1.getTimeRequest().equals(view2.getTimeRequest()))
			return false;
		if (view1.getX() == null) {
			if (view2.getX() != null)
				return false;
		} else if (!view1.getX().equals(view2.getX()))
			return false;
		return true;
	}

	// auth validation -----------------------------------------------------

	/** Validate contents of AuthView. 
	 * @throws KerbyException If the AuthView or its elements are null or empty. */
	public void validate() throws KerbyException {

		// check nulls and empty strings
		if (view == null)
			throw new KerbyException("Null auth!");

		final String x = view.getX();
		if (x == null || x.trim().length() == 0)
			throw new KerbyException("X cannot be empty!");

		final XMLGregorianCalendar xgc1 = view.getTimeRequest();
		if (xgc1 == null)
			throw new KerbyException("TimeRequest cannot be empty!");
	}

	// XML serialization -----------------------------------------------------

	/**
	 * Marshal auth to XML document.
	 */
	public Node toXMLNode(String authTagName) throws JAXBException {
		return viewToXML(AuthView.class, view, new QName(authTagName));
	}

	/** Marshal auth to XML bytes. */
	public byte[] toXMLBytes(String authTagName) throws JAXBException {
		return viewToXMLBytes(AuthView.class, view, new QName(authTagName));
	}
	
	/** Marshal auth to Base64 String. */
	public String toBase64String(String authTagName) throws JAXBException {
		byte[] xmlBytes = viewToXMLBytes(AuthView.class, view, new QName(authTagName));
		return printBase64Binary(xmlBytes);
	}

	/**
	 * Unmarshal auth from XML document.
	 */
	private static AuthView fromXMLNode(Node xml) throws JAXBException {
		AuthView view= xmlNodeToView(AuthView.class, xml);
		// set view should not allow null
		return view;
	}

	/** Unmarshal byte array to a view object. */
	private static AuthView fromXMLBytes(byte[] bytes) throws JAXBException {
		AuthView view = xmlBytesToView(AuthView.class, bytes);
		// set view should not allow null
		return view;
	}

	/** Unmarshal auth from Base64 String. */
	private static AuthView fromBase64String(String base64String) throws JAXBException {
		byte[] bytes = parseBase64Binary(base64String);
		return fromXMLBytes(bytes); 
	}
	
	// sealing ---------------------------------------------------------------
	/**
	 * Ciphers the Auth.
	 * @param key The Key used to cipher the Auth. 
	 * @return The Ciphered AuthView. */
	public CipheredView cipher(Key key) throws KerbyException {
		return SecurityHelper.cipher(AuthView.class, view, key);
	}

	/**
	 * Creates an Auth from a Ciphered Auth.
	 * */
	private static AuthView decipher(CipheredView cipheredView, Key key) throws KerbyException {
		AuthView view = SecurityHelper.decipher(AuthView.class, cipheredView, key);
		// set view should not allow null
		return view;
	}

}