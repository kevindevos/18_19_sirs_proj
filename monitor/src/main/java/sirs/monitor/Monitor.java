package sirs.monitor;

import common.sirs.ws.NoteDigestView;
import pt.ulisboa.tecnico.sdis.kerby.TicketCollection;
import sirs.app.ws.NotAllowed_Exception;
import sirs.app.ws.cli.AppClient;
import sirs.app.ws.cli.AppClientConnectionManager;
import sirs.web.ws.WebpageDigestView;
import sirs.ws.cli.WebClient;

import java.security.Key;
import java.util.*;

public class Monitor {
    private static List<AppClient> appServerConnections; // connections to application servers

    // webinterface's web service URL for monitoring
    private static String WEB_WS_URL = "http://localhost:8185/web-ws/endpoint";

    // time constants
    private static final int SERVER_UPTIME_PING_INTERVAL = 5 * 1000;
    private static final int INTEGRITY_CHECK_INTERVAL = 30 * 1000;

    // List of valid kerby tickets
    public static TicketCollection ticketCollection;

    // Store all session keys for each target server <targetServerName, sessionKey>
    public static Map<String, Key> sessionKeyMap;

    // web service client to communicate with web interface with soap messages
    // instead of using the web page url's, due to security issues
    private WebClient webClient;

    // monitor noteDigestHistory of noteDigests seen so far from web interface
    private List<NoteDigestView> noteDigestHistory;

    // monitor webpageDigestHistory of web pages seen from web interface
    private List<WebpageDigestView> webpageDigestHistory;

    public Monitor(){
        appServerConnections = AppClientConnectionManager.getInstance().getAllConnections();
        ticketCollection = new TicketCollection();
        sessionKeyMap = new HashMap<>();
        webClient = new WebClient(WEB_WS_URL);
        noteDigestHistory = new ArrayList<>();
        webpageDigestHistory =  new ArrayList<>();
    }

    public void run(){
        // update noteDigestHistory from app server notedigestviews once as a starting noteDigestHistory
        updateMonitorNoteDigestHistory(getAllNoteDigestViewsAppServers());

        // update noteDigestHistory of webpages once when starting
        updateMonitorWebpageDigestHistory(webClient.getWebpageDigests());

        // check status of the servers every second
        Thread appServerUpThread = startAppServerUpCheckerThread();
        Thread webServerUpThread = startWebServerUpCheckerThread();
        Thread webServerIntegrityCheckerThread = startWebServerIntegrityChecker();
        Thread noteIntegrityCheckerThread = startNoteIntegrityCheckerThread();



    }

    private Thread startWebServerIntegrityChecker(){
        // TODO review correctness
        Runnable runnable = () -> {
            List<WebpageDigestView> currentWebpageDigestViews;
            List<WebpageDigestView> pagesToRecover = new ArrayList<>();

            while(true){
                // get current webpages and their digests
                currentWebpageDigestViews = webClient.getWebpageDigests();

                // compare digests , if any dont match, recover the page
                for(WebpageDigestView historyDigestView : webpageDigestHistory){
                    for(WebpageDigestView currentDigestView : currentWebpageDigestViews){
                        if(historyDigestView.getPageName().equals(currentDigestView.getPageName())){
                            if(historyDigestView.getDigest() != currentDigestView.getDigest()){
                               pagesToRecover.add(historyDigestView);
                            }
                        }
                    }
                }

                if(pagesToRecover.size() > 0 ){
                    recoverWebPages(pagesToRecover);
                }

                sleep(INTEGRITY_CHECK_INTERVAL);
            }


        };
        Thread thread = new Thread(runnable);
        thread.start();

        return thread;
    }

    private void recoverWebPages(List<WebpageDigestView> pagesToRecover){
        webClient.recoverWebPages(pagesToRecover);
    }

    /**
     * update current history of web page states, only new ones can be added, to replace a existing page, the monitor needs to be restarted
     * @param webpageDigestViews
     */
    private void updateMonitorWebpageDigestHistory(List<WebpageDigestView> webpageDigestViews){
        // TODO
        // any page with same name but diferent content
        // any missing page from history
        // any new page not in history

        for(WebpageDigestView historyDigestView : webpageDigestHistory){
            for(WebpageDigestView webpageDigestView : webpageDigestViews){
                if(!historyDigestView.getPageName().equals(webpageDigestView.getPageName())){
                    webpageDigestHistory.add(webpageDigestView);
                }
            }
        }
    }

    /**
     * Get all note digest views from web interface , update noteDigestHistory in monitor
     * Get all note digest views from the app servers, if any digest is diferent, then recovery is made, and keys are revoked for the affected app server
     * @return thread running the periodic integrity checks
     */
    private Thread startNoteIntegrityCheckerThread(){
        // TODO review correctness
        Runnable runnable = () -> {
            List<NoteDigestView> noteDigestViewsAppSevers = null;
            List<NoteDigestView> notesToRecover = new ArrayList<>();

            while(true){
                noteDigestViewsAppSevers = getAllNoteDigestViewsAppServers();

                // update noteDigestHistory from webinterface recently changed
                updateMonitorNoteDigestHistory(webClient.takeRecentlyChangedNoteDigests());

                // Compare note digests in the monitor's noteDigestHistory with existing ones in app servers

                for(NoteDigestView appNoteDigestView : noteDigestViewsAppSevers){
                    for(NoteDigestView historyNoteDigestView : noteDigestHistory){

                        if(historyNoteDigestView.getNoteView().getName().equals(appNoteDigestView.getNoteView().getName())){
                            if(historyNoteDigestView.getDigest() != appNoteDigestView.getDigest()){
                                notesToRecover.add(historyNoteDigestView);
                                logErr("Compromised note : " + historyNoteDigestView.getNoteView().getName() + " with owner : " +
                                        historyNoteDigestView.getNoteView().getOwner());
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

                sleep(INTEGRITY_CHECK_INTERVAL);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        return thread;
    }

    private Thread startWebServerUpCheckerThread(){
        Runnable runnable = () -> {
            while(true){
                if(isWebServerUp()){
                    try{
                        log("Web Server is up.");
                        Thread.sleep(SERVER_UPTIME_PING_INTERVAL);
                    } catch(InterruptedException e){
                        System.exit(-1);
                    }
                }else{
                    logErr("Web Server is unresponsive.");
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        return thread;
    }

    private Thread startAppServerUpCheckerThread(){
        Runnable runnable = () -> {
            while(true){
                if(areAppServersUp()){
                    try{
                        log("Web Server is up.");
                        Thread.sleep(SERVER_UPTIME_PING_INTERVAL);
                    } catch(InterruptedException e){
                        System.exit(-1);
                    }
                }else{
                    List<AppClient> unresponsiveConnections = getListOfUnresponsiveAppServers();
                    for(AppClient appClient : unresponsiveConnections){
                        logErr("App Server at " + appClient.getWsURL() + " is unresponsive.");
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        return thread;
    }

    private void updateMonitorNoteDigestHistory(List<NoteDigestView> noteDigestViews){
        for(NoteDigestView noteDigestView : noteDigestViews){
            for(NoteDigestView historyNoteDigestView : noteDigestHistory){

                if(!noteDigestView.getNoteView().getName().equals(historyNoteDigestView.getNoteView().getName())){
                    noteDigestHistory.add(noteDigestView);
                }else{
                    // update current digest view
                    noteDigestHistory.remove(historyNoteDigestView);
                    noteDigestHistory.add(historyNoteDigestView);
                }
            }
        }
    }

    private void recoverNotesToAppServers(List<NoteDigestView> notesToRecover){
        for(NoteDigestView noteDigestView : notesToRecover){
            int rand = (new Random(System.currentTimeMillis()).nextInt(appServerConnections.size()));
            AppClient appClient = appServerConnections.get(rand);

           // try{
           //     appClient.updateNote(noteDigestView.getNoteView());
           // }
        }
    }

    private List<NoteDigestView> getAllNoteDigestViewsAppServers(){
        List<NoteDigestView> noteDigestViews = new ArrayList<>();

        for(int i = 0; i < appServerConnections.size(); i++){
            noteDigestViews.addAll(appServerConnections.get(i).getAllNoteDigests());
        }

        return noteDigestViews;
    }

    private List<AppClient> getListOfUnresponsiveAppServers(){
        List<AppClient> unresponsive = new ArrayList<>();
        for(AppClient appClient : appServerConnections){
            String response = appClient.testPing("Monitor Ping");
            if(response == null){
                unresponsive.add(appClient);
            }
        }

        return unresponsive;
    }

    /**
     * Ping every server and check if they respond.
     * @return true if all servers responded, false otherwise
     */
    private boolean areAppServersUp(){
        for(AppClient appClient : appServerConnections){
            String response = appClient.testPing("Monitor Ping");
            if(response == null){
                return false;
            }
        }
        return true;
    }

    private boolean isWebServerUp(){
        String response = webClient.testPing("Monitor Ping");

        return response != null;
    }

    private void sleep(int duration){
        try{
            Thread.sleep(duration);
        } catch(InterruptedException e){
            System.exit(-1);
        }
    }

    private void log(String message){
        System.out.println("[MONITOR]: " + message);
    }

    private void logErr(String errMessage){
        System.err.println("[MONITOR]: " + errMessage);
    }



}
