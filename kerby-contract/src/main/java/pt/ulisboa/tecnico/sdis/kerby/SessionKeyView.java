package pt.ulisboa.tecnico.sdis.kerby;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "encodedKeyXY", "nounce" })
public class SessionKeyView implements Serializable {

	private static final long serialVersionUID = 1L;

	private byte[] encodedKeyXY;
	private long nounce;

	public byte[] getEncodedKeyXY() {
		return encodedKeyXY;
	}

	public void setEncodedKeyXY(byte[] encodedKeyXY) {
		this.encodedKeyXY = encodedKeyXY;
	}

	public long getNounce() {
		return nounce;
	}

	public void setNounce(long nounce) {
		this.nounce = nounce;
	}

}
