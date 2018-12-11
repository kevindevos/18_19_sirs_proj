package sirs.kerby.ws;

/**
 * Kerby Web Service application.
 * 
 * @author Miguel Pardal
 *
 */
public class KerbyApp {
    private static final String KERBY_WS_URL = "http://localhost:8888/kerby";

    public static void main(String[] args) throws Exception {
        // Create server implementation object, according to options
		KerbyEndpointManager endpoint = null;
        endpoint = new KerbyEndpointManager(KERBY_WS_URL);

		try {
			endpoint.start();
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
		}

	}

}