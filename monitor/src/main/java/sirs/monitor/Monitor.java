package sirs.monitor;

import common.sirs.ws.NoteDigestView;
import common.sirs.ws.NoteView;
import pt.ulisboa.tecnico.sdis.kerby.TicketCollection;
import sirs.app.ws.cli.AppClient;
import sirs.app.ws.cli.AppClientConnectionManager;
import sirs.web.ws.TakeRecentlyChangedNotes;
import sirs.ws.cli.WebClient;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Monitor {
    private static List<AppClient> appServerConnections; // connections to application servers
    // private backupServerConnection; // connection to backup server

    // webinterface's web service URL for monitoring
    private static String WEB_WS_URL = "http://localhost:8185/web-ws/endpoint";

    // time constants
    private static final int SERVER_UPTIME_PING_INTERVAL = 1000;
    private static final int NOTE_INTEGRITY_CHECK_INTERVAL = 10 * 1000;

    // List of valid kerby tickets
    public static TicketCollection ticketCollection;

    // Store all session keys for each target server <targetServerName, sessionKey>
    public static Map<String, Key> sessionKeyMap;

    // web service client to communicate with web interface with soap messages
    // instead of using the web page url's, due to security issues
    private WebClient webClient;

    public Monitor(){
        appServerConnections = AppClientConnectionManager.getInstance().getAllConnections();
        ticketCollection = new TicketCollection();
        sessionKeyMap = new HashMap<>();
        webClient = new WebClient(WEB_WS_URL);
    }

    public void run(){
        // check status of the servers every second
        Thread appServerUpThread = startAppServerUpCheckerThread();
        Thread webServerUpThread = startWebServerUpCheckerThread();
        Thread noteIntegrityCheckerThread = startNoteIntegrityCheckerThread();


        appServerUpThread.interrupt();
        webServerUpThread.interrupt();
        noteIntegrityCheckerThread.interrupt();
    }

    private Thread startNoteIntegrityCheckerThread(){
        Runnable runnable = () -> {
            List<NoteDigestView> recentlyChangedNotes = new ArrayList<>();
            while(true){
                for(int i = 0; i < appServerConnections.size(); i++){
                    recentlyChangedNotes.addAll(appServerConnections.get(i).getAllNoteDigests());
                }
                // TODO check integrity here

                recentlyChangedNotes.clear();
            }
        };
        Thread upThread = new Thread(runnable);
        upThread.start();

        return upThread;
    }

    private Thread startWebServerUpCheckerThread(){
        Runnable runnable = () -> {
            while(isWebServerUp()){
                try{
                    System.out.println("Monitor: Web server is up.");
                    Thread.sleep(SERVER_UPTIME_PING_INTERVAL);
                } catch(InterruptedException e){
                    System.exit(-1);
                }
            }
            // TODO code when web server is down
        };
        Thread upThread = new Thread(runnable);
        upThread.start();

        return upThread;
    }

    private Thread startAppServerUpCheckerThread(){
        Runnable runnable = () -> {
            while(areAppServersUp()){
                try{
                    System.out.println("Monitor: Servers are up.");
                    Thread.sleep(SERVER_UPTIME_PING_INTERVAL);
                } catch(InterruptedException e){
                    System.exit(-1);
                }
            }
            // TODO code when app server is down
        };
        Thread upThread = new Thread(runnable);
        upThread.start();

        return upThread;
    }


    /**
     * Ping every server and check if they respond.
     * @return true if all servers responded, false otherwise
     */
    public boolean areAppServersUp(){
        for(AppClient appClient : appServerConnections){
            String response = appClient.testPing("Monitor Ping");
            if(response == null){
                return false;
            }
        }
        return true;
    }

    public boolean isWebServerUp(){
        String response = webClient.testPing("Monitor Ping");

        return response != null;
    }

}
