package pt.ulisboa.tecnico.sdis.kerby;

import org.w3c.dom.Node;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.security.Key;
import java.util.Arrays;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static pt.ulisboa.tecnico.sdis.kerby.SecurityHelper.recodeKey;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.*;

/**
 * Class that represents a Kerberos Session Key and can use different data formats.
 * 
 * @author Miguel Pardal
 *
 */
public class SessionKey {

	/** SessionKey data container. After creation, cannot be null. */
	private SessionKeyView view;

	// SessionKey creation -------------------------------------------------------

	/** Create SessionKey from arguments. */
	public SessionKey(Key keyXY,long nounce) {
		view = new SessionKeyView();
		view.setEncodedKeyXY(keyXY.getEncoded());
		view.setNounce(nounce);
	}

	// TODO create constructor without key (one is generated)

	/** Create SessionKey from data view. */
	public SessionKey(SessionKeyView view) {
		setView(view);
	}

	/** Create SessionKey from an XML Node.
	 * @param node An XML Node containing a SessionKey
	 * */
	public static SessionKey makeSessionKeyFromXMLNode(Node node) throws JAXBException {
		SessionKeyView view = fromXMLNode(node);
		return new SessionKey(view);
	}
	
	/** Create SessionKey from a Ciphered SessionKeyView.
	 * @param view A Ciphered SessionKeyView.
	 * @param key The Key used to decipher the View.
	 * */
	public static SessionKey makeSessionKeyFromCipheredView(CipheredView cipheredView, Key key) throws KerbyException {
		SessionKeyView view = decipher(cipheredView, key);
		return new SessionKey(view);
	}
	
	/** Create SessionKey from a XML Bytes.
	 * @param node A byte array representing a SessionKey
	 * */
	public static SessionKey makeSessionKeyFromXMLBytes(byte[] bytes) throws JAXBException {
		SessionKeyView view = fromXMLBytes(bytes);
		return new SessionKey(view);
	}

	/** Create SessionKey from a Base64 String.
	 * @param node A Base64 String representing a SessionKey
	 * */
	public static SessionKey makeSessionKeyFromBase64String(String base64String) throws JAXBException {
		SessionKeyView view = fromBase64String(base64String);
		return new SessionKey(view);
	}
	
	// After construction, view can never be null, and can never be set to null.
	// This invariant is assumed to be true in the remaining code.

	// accessors -------------------------------------------------------------

	protected SessionKeyView getView() {
		return view;
	}

	protected void setView(SessionKeyView view) {
		if (view == null)
			throw new IllegalArgumentException("View cannot be null!");
		this.view = view;
	}

	public long getNounce() {
		return view.getNounce();
	}

	public void setNounce(long nounce) {
		view.setNounce(nounce);
	}

	public Key getKeyXY() {
		byte[] encodedKeyXY = view.getEncodedKeyXY();
		Key key = recodeKey(encodedKeyXY);
		return key;
	}

	public void setKeyXY(Key keyXY) {
		byte[] encodedKey = keyXY.getEncoded();
		view.setEncodedKeyXY(encodedKey);
	}

	// object methods --------------------------------------------------------

	/** Create a textual representation of the SessionKeyView. */
	public String toString() {
		if (view == null)
			return "null";

		StringBuilder builder = new StringBuilder();
		builder.append("SessionKeyView [nounce=");
		builder.append(view.getNounce());
		builder.append(", encodedKeyXY=");
		builder.append(Arrays.toString(view.getEncodedKeyXY()));
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(view.getEncodedKeyXY());
		result = prime * result + (int) (view.getNounce() ^ (view.getNounce() >>> 32));
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
		SessionKey other = (SessionKey) obj;
		SessionKeyView otherView = other.getView();

		return SessionKeyViewEquals(view, otherView);
	}

	/** Compare contents of two SessionKey views. */
	protected boolean SessionKeyViewEquals(SessionKeyView view1, SessionKeyView view2) {
		if (view1 == view2)
			return true;
		if (view2 == null)
			return false;
		if (view1.getClass() != view2.getClass())
			return false;
		if (view1.getEncodedKeyXY() == null) {
			if (view2.getEncodedKeyXY() != null)
				return false;
		} else if (view2.getEncodedKeyXY() == null || !Arrays.equals(view1.getEncodedKeyXY(), view2.getEncodedKeyXY()))
			// compare keys in encoded form
			// uses Arrays.equals to compare array contents
			return false;
		if (view1.getNounce() != (view2.getNounce()))
			return false;
		return true;
	}

	// SessionKey validation -----------------------------------------------------

	/** Validate contents of SessionKeyView. */
	public void validate() throws KerbyException {

		// check nulls and empty strings
		if (view == null)
			throw new KerbyException("Null SessionKey!");

		final byte[] encodedKeyXY = view.getEncodedKeyXY();
		if (encodedKeyXY == null)
			throw new KerbyException("Encoded key cannot be empty!");

		// does not check if key encoding is correct to avoid having one conversion here
		// at
		// validation, and one later at the getter when key is needed for use
	}

	// XML serialization -----------------------------------------------------

	/**
	 * Marshal SessionKey to XML document.
	 */
	public Node toXMLNode(String sessionKeyTagName) throws JAXBException {
		return viewToXML(SessionKeyView.class, view, new QName(sessionKeyTagName));
	}

	/** Marshal SessionKey to XML bytes. */
	public byte[] toXMLBytes(String sessionKeyTagName) throws JAXBException {
		return viewToXMLBytes(SessionKeyView.class, view, new QName(sessionKeyTagName));
	}
	
	/** Marshal SessionKey to Base64 String. */
	public String toBase64String(String sessionKeyTagName) throws JAXBException {
		byte[] xmlBytes = viewToXMLBytes(SessionKeyView.class, view, new QName(sessionKeyTagName));
		return printBase64Binary(xmlBytes);
	}

	/**
	 * Unmarshal SessionKey from XML document.
	 */
	private static SessionKeyView fromXMLNode(Node xml) throws JAXBException {
		SessionKeyView view = xmlNodeToView(SessionKeyView.class, xml);
		return view;
	}

	/** Unmarshal byte array to a view object. */
	private static SessionKeyView fromXMLBytes(byte[] bytes) throws JAXBException {
		SessionKeyView view = xmlBytesToView(SessionKeyView.class, bytes);
		return view;
	}
	
	/** Unmarshal sessionKey from Base64 String. */
	private static SessionKeyView fromBase64String(String base64String) throws JAXBException {
		byte[] bytes = parseBase64Binary(base64String);
		return fromXMLBytes(bytes); 
	}

	// ciphering ---------------------------------------------------------------
	
	public CipheredView cipher(Key key) throws KerbyException {
		return SecurityHelper.cipher(SessionKeyView.class, view, key);
	}

	private static SessionKeyView decipher(CipheredView cipheredView, Key key) throws KerbyException {
		SessionKeyView view = SecurityHelper.decipher(SessionKeyView.class, cipheredView, key);
		return view;
	}

}
