package pt.ulisboa.tecnico.sdis.kerby;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlType;

/**
 * Class that represents a Kerberos authenticator.
 * 
 * @author Miguel Pardal
 *
 */
@XmlType(propOrder = { "x", "timeRequest" })
public class AuthView implements Serializable {

	private static final long serialVersionUID = 1L;

	private String x;
	private Date timeRequest;

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public Date getTimeRequest() {
		return timeRequest;
	}

	public void setTimeRequest(Date timeRequest) {
		this.timeRequest = timeRequest;
	}

}
