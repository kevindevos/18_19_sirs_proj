package sirs.monitor;

import pt.ulisboa.tecnico.sdis.kerby.TicketCollection;
import sirs.app.ws.cli.AppClient;
import sirs.app.ws.cli.AppClientConnectionManager;

import java.security.Key;
import java.util.List;
import java.util.Map;

public class Monitor {
    private List<AppClient> appServerConnections; // connections to application servers
    // private backupServerConnection; // connection to backup server

    // List of valid kerby tickets
    public static TicketCollection ticketCollection;

    public static final String MONITOR_NAME = "MONITOR";
    public static String privatePassword;

    // Store all session keys for each target server <targetServerName, sessionKey>
    public static Map<String, Key> sessionKeyMap;

    public Monitor(){
        appServerConnections = AppClientConnectionManager.getInstance().connectWithAllAppServers();
    }

    /**
     * Ping every server and check if they respond.
     * @return true if all servers responded, false otherwise
     */
    public boolean areServersUp(){
        for(AppClient appClient : appServerConnections){
            String response = appClient.testPing("Monitor Ping");
            if(response == null){
                return false;
            }
        }

        // TODO all servers


        return true;
    }
}
