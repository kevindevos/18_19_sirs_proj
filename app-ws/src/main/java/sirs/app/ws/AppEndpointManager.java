package sirs.app.ws;

import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;

import javax.xml.ws.Endpoint;
import java.io.IOException;

/** End point manager */
public class AppEndpointManager {
    /** Web Service name */
    private String wsName = null;

    /** Get Web Service UDDI publication name */
    public String getWsName() {
        return wsName;
    }

    /** Web Service location to publish */
    public static String wsURL = null;

    /** Port implementation */
    private AppPortImpl portImpl = new AppPortImpl(this);

    /** Obtain Port implementation */
    public AppPortType getPort() {
        return portImpl;
    }

    /** Web Service endpoint */
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
    public AppEndpointManager(String wsURL) {
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

        return;
    }

    private void setupKerbyConnection(){
        // generate a password to use with kerby
        KerbyClient kerbyClient = new KerbyClient();
        AppPortImpl.privatePassword = kerbyClient.generateDHPassword(wsURL);
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