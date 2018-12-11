package sirs.monitor;

import common.sirs.ws.NoteDigestView;
import pt.ulisboa.tecnico.sdis.kerby.TicketCollection;
import sirs.app.ws.NotAllowed_Exception;
import sirs.app.ws.cli.AppClient;
import sirs.app.ws.cli.AppClientConnectionManager;
import sirs.ws.cli.WebClient;

import java.security.Key;
import java.util.*;

public class Monitor {
    private static List<AppClient> appServerConnections; // connections to application servers

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

    // monitor history of noteDigests seen so far from web interface
    private List<NoteDigestView> history;

    public Monitor(){
        appServerConnections = AppClientConnectionManager.getInstance().getAllConnections();
        ticketCollection = new TicketCollection();
        sessionKeyMap = new HashMap<>();
        webClient = new WebClient(WEB_WS_URL);


    }

    public void run(){
        // update history from app server notedigestviews once as a starting history
        updateMonitorNoteDigestHistory(getAllNoteDigestViewsAppServers());

        // check status of the servers every second
        Thread appServerUpThread = startAppServerUpCheckerThread();
        Thread webServerUpThread = startWebServerUpCheckerThread();
        Thread noteIntegrityCheckerThread = startNoteIntegrityCheckerThread();

        // TODO make main thread wait here, else it insta kills threads
        appServerUpThread.interrupt();
        webServerUpThread.interrupt();
        noteIntegrityCheckerThread.interrupt();
    }

    /**
     * Get all note digest views from web interface , update history in monitor
     * Get all note digest views from the app servers, if any digest is diferent, then recovery is made, and keys are revoked for the affected app server
     * @return thread running the periodic integrity checks
     */
    private Thread startNoteIntegrityCheckerThread(){
        Runnable runnable = () -> {
            List<NoteDigestView> noteDigestViewsAppSevers = null;
            List<NoteDigestView> notesToRecover = new ArrayList<>();

            while(true){
                noteDigestViewsAppSevers = getAllNoteDigestViewsAppServers();

                // update history from webinterface recently changed
                updateMonitorNoteDigestHistory(webClient.takeRecentlyChangedNoteDigests());

                // Compare note digests in the monitor's history with existing ones in app servers

                for(NoteDigestView appNoteDigestView : noteDigestViewsAppSevers){
                    for(NoteDigestView historyNoteDigestView : history){

                        if(historyNoteDigestView.getNoteView().getName().equals(appNoteDigestView.getNoteView().getName())){
                            if(historyNoteDigestView.getDigest() != appNoteDigestView.getDigest()){
                                notesToRecover.add(historyNoteDigestView);
                            }
                        }
                    }
                }

                // recover notes if necessary
                if(notesToRecover.size() > 0 ){
                    recoverNotesToAppServers(notesToRecover);
                    notesToRecover.clear();
                }

                // clear lists for next round
                noteDigestViewsAppSevers.clear();

                sleep(NOTE_INTEGRITY_CHECK_INTERVAL);
            }
        };
        Thread upThread = new Thread(runnable);
        upThread.start();

        return upThread;
    }

    private void updateMonitorNoteDigestHistory(List<NoteDigestView> noteDigestViews){
        for(NoteDigestView noteDigestView : noteDigestViews){
            for(NoteDigestView historyNoteDigestView : history){

                if(!noteDigestView.getNoteView().getName().equals(historyNoteDigestView.getNoteView().getName())){
                    history.add(noteDigestView);
                }else{
                    // update current digest view
                    history.remove(historyNoteDigestView);
                    history.add(historyNoteDigestView);
                }
            }

        }
    }

    private void recoverNotesToAppServers(List<NoteDigestView> notesToRecover){
        for(NoteDigestView noteDigestView : notesToRecover){
            int rand = (new Random(System.currentTimeMillis()).nextInt(appServerConnections.size()));
            AppClient appClient = appServerConnections.get(rand);

            try{
                appClient.updateNote(noteDigestView.getNoteView());
            } catch(NotAllowed_Exception e){
                e.printStackTrace();
            }
        }
    }

    private List<NoteDigestView> getAllNoteDigestViewsAppServers(){
        List<NoteDigestView> noteDigestViews = new ArrayList<>();

        for(int i = 0; i < appServerConnections.size(); i++){
            noteDigestViews.addAll(appServerConnections.get(i).getAllNoteDigests());
        }

        return noteDigestViews;
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

    public void sleep(int duration){
        try{
            System.out.println("Monitor: Web server is up.");
            Thread.sleep(duration);
        } catch(InterruptedException e){
            System.exit(-1);
        }
    }

}
