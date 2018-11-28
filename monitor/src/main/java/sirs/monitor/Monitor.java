package sirs.monitor;

import sirs.app.ws.cli.AppClient;
import sirs.app.ws.cli.AppClientConnectionManager;

import java.util.List;

public class Monitor {
    private List<AppClient> appServerConnections; // connections to application servers
    // private backupServerConnection; // connection to backup server

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
            }else{
                System.out.println("server answered with: " + response);
            }
        }

        // TODO all servers


        return true;
    }
}
