package pt.ulisboa.tecnico.sdis.kerby;

/**
 * Kerby Web Service application.
 * 
 * @author Miguel Pardal
 *
 */
public class KerbyApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length != 1) {
			System.err.println("Invalid Args!");
			System.err.println("Usage: java " + KerbyApp.class.getName() + " wsURL");
			return;
		}

		String wsName = null;
        String wsURL = args[0];

        // Create server implementation object, according to options
		KerbyEndpointManager endpoint = null;
        endpoint = new KerbyEndpointManager(wsURL);

		try {
			endpoint.start();
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
		}

	}

}