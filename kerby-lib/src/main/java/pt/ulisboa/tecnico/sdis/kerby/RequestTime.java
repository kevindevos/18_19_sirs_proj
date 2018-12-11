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
 * Class that represents a Kerberos RequestTime and can use different data formats.
 * 
 * @author Miguel Amaral
 *
 */
public class RequestTime {

	/** RequestTime data container. After creation, cannot be null. */
	private RequestTimeView view;

	// RequestTimeView creation -------------------------------------------------------

	/** Create RequestTimeView from arguments. */
	public RequestTimeView requestTimeBuild(Date timeRequest) {
		RequestTimeView requestTimeView = new RequestTimeView();
		requestTimeView.setTimeRequest(dateToXML(timeRequest));
		return requestTimeView;
	}
	
	/** Create a default view */
	public RequestTime() {
		RequestTimeView view = new RequestTimeView();
		view.setTimeRequest(null);
		setView(view);
	}
	
	/** Create RequestTime from data view. */
	public RequestTime(RequestTimeView view) {
		setView(view);
	}
	
	public RequestTime(Date timeRequest) {
		setView(requestTimeBuild(timeRequest));
	}
	
	/** Create RequestTime from an XML Node.
	 * @param node An XML Node containing a RequestTime
	 * */
	public static RequestTime makeRequestTimeFromXMLNode(Node node) throws JAXBException {
		RequestTimeView view = fromXMLNode(node);
		return new RequestTime(view);
	}
	
	/** Create RequestTime from a Ciphered RequestTimeView.
	 * @param view A Ciphered RequestTimeView.
	 * @param key The Key used to decipher the View.
	 * */
	public static RequestTime makeRequestTimeFromCipheredView(CipheredView cipheredView, Key key) throws KerbyException {
		RequestTimeView view = decipher(cipheredView, key);
		return new RequestTime(view);
	}
	
	/** Create RequestTime from a XML Bytes.
	 * @param node A byte array representing an RequestTime
	 * */
	public static RequestTime makeRequestTimeFromXMLBytes(byte[] bytes) throws JAXBException {
		RequestTimeView view = fromXMLBytes(bytes);
		return new RequestTime(view);
	}

	/** Create RequestTime from a Base64 String.
	 * @param node A Base64 String representing an RequestTime
	 * */
	public static RequestTime makeRequestTimeFromBase64String(String base64String) throws JAXBException {
		RequestTimeView view = fromBase64String(base64String);
		return new RequestTime(view);
	}

	
	// After construction, view can never be null, and can never be set to null.
	// This invariant is assumed to be true in the remaining code.

	// accessors -------------------------------------------------------------

	protected RequestTimeView getView() {
		return view;
	}

	protected void setView(RequestTimeView view) {
		if (view == null)
			throw new IllegalArgumentException("View cannot be null!");
		this.view = view;
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

	/** Create a textual representation of the RequestTimeView. */
	public String requestTimeToString() {
		if (view == null) {
			return "null";
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("RequestTimeView [timeRequest=");
		builder.append(view.getTimeRequest());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((view.getTimeRequest() == null) ? 0 : view.getTimeRequest().hashCode());
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
		RequestTime other = (RequestTime) obj;
		RequestTimeView otherView = other.getView();

		return RequestTimeViewEquals(view, otherView);
	}

	/** Compare contents of two RequestTime views. */
	protected boolean RequestTimeViewEquals(RequestTimeView view1, RequestTimeView view2) {
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
		return true;
	}

	// RequestTime validation -----------------------------------------------------

	/** Validate contents of RequestTimeView. */
	public void validate() throws KerbyException {

		// check nulls and empty strings
		if (view == null)
			throw new KerbyException("Null RequestTime!");

		final XMLGregorianCalendar xgc1 = view.getTimeRequest();
		if (xgc1 == null)
			throw new KerbyException("TimeRequest cannot be empty!");
	}

	// XML serialization -----------------------------------------------------

	/**
	 * Marshal RequestTime to XML document.
	 */
	public Node toXMLNode(String requestTimeTagName) throws JAXBException {
		return viewToXML(RequestTimeView.class, view, new QName(requestTimeTagName));
	}

	/** Marshal requestTime to XML bytes. */
	public byte[] toXMLBytes(String requestTimeTagName) throws JAXBException {
		return viewToXMLBytes(RequestTimeView.class, view, new QName(requestTimeTagName));
	}
	
	/** Marshal requestTime to Base64 String. */
	public String toBase64String(String requestTimeTagName) throws JAXBException {
		byte[] xmlBytes = viewToXMLBytes(RequestTimeView.class, view, new QName(requestTimeTagName));
		return printBase64Binary(xmlBytes);
	}

	/**
	 * Unmarshal requestTime from XML document.
	 */
	private static RequestTimeView fromXMLNode(Node xml) throws JAXBException {
		RequestTimeView view= xmlNodeToView(RequestTimeView.class, xml);
		return view;
	}

	/** Unmarshal byte array to a view object. */
	private static RequestTimeView fromXMLBytes(byte[] bytes) throws JAXBException {
		RequestTimeView view = xmlBytesToView(RequestTimeView.class, bytes);
		return view;
	}
	
	/** Unmarshal requestTime from Base64 String. */
	private static RequestTimeView fromBase64String(String base64String) throws JAXBException {
		byte[] bytes = parseBase64Binary(base64String);
		return fromXMLBytes(bytes); 
	}

	// ciphering ---------------------------------------------------------------

	public CipheredView cipher(Key key) throws KerbyException {
		return SecurityHelper.cipher(RequestTimeView.class, view, key);
	}

	/**
	 * Decipher is private because it should only be called by constructor when
	 * receiving a CipheredView of the RequestTime.
	 */
	private static RequestTimeView decipher(CipheredView cipheredView, Key key) throws KerbyException {
		RequestTimeView view = SecurityHelper.decipher(RequestTimeView.class, cipheredView, key);
		return view;
	}

}