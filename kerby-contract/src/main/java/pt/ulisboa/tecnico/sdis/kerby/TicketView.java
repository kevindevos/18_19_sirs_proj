package pt.ulisboa.tecnico.sdis.kerby;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "x", "y", "time1", "time2", "encodedKeyXY" })
public class TicketView implements Serializable {

	private static final long serialVersionUID = 1L;

	private String x;
	private String y;
	private Date time1;
	private Date time2;
	private byte[] encodedKeyXY;

	/**
	 * @return the x
	 */
	public String getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(String x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public String getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(String y) {
		this.y = y;
	}

	/**
	 * @return the time1
	 */
	public Date getTime1() {
		return time1;
	}

	/**
	 * @param time1
	 *            the time1 to set
	 */
	public void setTime1(Date time1) {
		this.time1 = time1;
	}

	/**
	 * @return the time2
	 */
	public Date getTime2() {
		return time2;
	}

	/**
	 * @param time2
	 *            the time2 to set
	 */
	public void setTime2(Date time2) {
		this.time2 = time2;
	}

	/**
	 * @return the encodedKeyXY
	 */
	public byte[] getEncodedKeyXY() {
		return encodedKeyXY;
	}

	/**
	 * @param encodedKeyXY
	 *            the encodedKeyXY to set
	 */
	public void setEncodedKeyXY(byte[] encodedKeyXY) {
		this.encodedKeyXY = encodedKeyXY;
	}

}
