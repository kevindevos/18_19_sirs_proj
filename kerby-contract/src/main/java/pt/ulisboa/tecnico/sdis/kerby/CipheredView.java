package pt.ulisboa.tecnico.sdis.kerby;

import java.io.Serializable;

public class CipheredView implements Serializable {

	private static final long serialVersionUID = 1L;

	private byte[] data;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] cipheredView) {
		this.data = cipheredView;
	}

}
