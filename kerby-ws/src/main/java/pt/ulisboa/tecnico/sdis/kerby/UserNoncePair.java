package pt.ulisboa.tecnico.sdis.kerby;

class UserNoncePair {
	private String username;
	private long nonce;
	
	public UserNoncePair(String name, long n) {
		username = name;
		nonce = n;
	}
	
	public String getUsername() {
		return username;
	}
	
	public long getNonce() {
		return nonce;
	}
	
	// object methods --------------------------------------------------------

	/** Create a textual representation of the UserNouncePair. */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserNouncePair [username=");
		builder.append(username);
		builder.append(", nounce=");
		builder.append(nonce);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (nonce ^ (nonce >>> 32));
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		UserNoncePair other = (UserNoncePair) obj;
		if (nonce != other.nonce)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}	

}
