package sirs.webinterface.ws;

import sirs.kerby.cli.KerbyClient;
import sirs.web.ws.WebPortType;

import javax.xml.ws.Endpoint;
import java.io.IOException;


/** End point manager */
public class WebEndpointManager {
    /** Web Service name */
	private String wsName = null;

	/** Get Web Service UDDI publication name */
	public String getWsName() {
		return wsName;
	}

	/** Web Service location to publish */
	private String wsURL = null;

	/** Port implementation */
	private WebPortImpl portImpl = new WebPortImpl(this);

	/** Obtain Port implementation */
	public WebPortType getPort() {
		return portImpl;
	}

	/** Web Service end point */
	private Endpoint endpoint = null;

	/** output option **/
	private boolean verbose = true;

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/** constructor with provided web service URL */
	public WebEndpointManager(String wsURL) {
		if (wsURL == null)
			throw new NullPointerException("Web Service URL cannot be null!");
		this.wsURL = wsURL;
	}

	/* end point management */

	public void start() throws Exception {
		try {
			// publish end point
			endpoint = Endpoint.create(this.portImpl);
			if (verbose) {
				System.out.printf("Starting %s%n", wsURL);
			}
			endpoint.publish(wsURL);
		} catch (Exception e) {
			endpoint = null;
			if (verbose) {
				System.out.printf("Caught exception when starting: %s%n", e);
				e.printStackTrace();
			}
			throw e;
		}

		setupKerbyConnection();
	}

	public void awaitConnections() {
		if (verbose) {
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
		}
		try {
			System.in.read();
		} catch (IOException e) {
			if (verbose) {
				System.out.printf("Caught i/o exception when awaiting requests: %s%n", e);
			}
		}
	}

    private void setupKerbyConnection(){
        // generate a password to use with kerby
        KerbyClient kerbyClient = new KerbyClient();
        WebPortImpl.privatePassword = kerbyClient.generateDHPassword(wsURL);
    }

	public void stop() throws Exception {
		try {
			if (endpoint != null) {
				// stop end point
				endpoint.stop();
				if (verbose) {
					System.out.printf("Stopped %s%n", wsURL);
				}
			}
		} catch (Exception e) {
			if (verbose) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
		}
		this.portImpl = null;
	}


}