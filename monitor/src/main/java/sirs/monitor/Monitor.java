package sirs.monitor;

import pt.ulisboa.tecnico.sdis.kerby.TicketCollection;
import sirs.app.ws.cli.AppClient;
import sirs.app.ws.cli.AppClientConnectionManager;
import sirs.ws.cli.WebClient;

import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Monitor {
    private List<AppClient> appServerConnections; // connections to application servers
    // private backupServerConnection; // connection to backup server

    // webinterface's web service URL for monitoring
    private static String WEB_WS_URL = "http://localhost:8185/web-ws/endpoint";

    // List of valid kerby tickets
    public static TicketCollection ticketCollection;

    public static final String MONITOR_NAME = "MONITOR";
    public static String privatePassword;

    // Store all session keys for each target server <targetServerName, sessionKey>
    public static Map<String, Key> sessionKeyMap;

    // web service client to communicate with web interface with soap messages
    // instead of using the web page url's, due to security issues
    private WebClient webClient;

    public Monitor(){
        appServerConnections = AppClientConnectionManager.getInstance().connectWithAllAppServers();
        ticketCollection = new TicketCollection();
        sessionKeyMap = new HashMap<>();
        webClient = new WebClient(WEB_WS_URL);
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

        String response = webClient.testPing("Monitor Ping");
        if(response == null) return false;


        return true;


    }

}
