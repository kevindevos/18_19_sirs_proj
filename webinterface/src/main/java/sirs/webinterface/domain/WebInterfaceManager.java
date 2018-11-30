package sirs.webinterface.domain;

import pt.ulisboa.tecnico.sdis.kerby.TicketCollection;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class WebInterfaceManager {
    // List of valid kerby tickets
    public static TicketCollection ticketCollection;

    public static final String WEB_SERVER_NAME = "WEB_SERVER";
    public static String PASSWORD;

    // Store all session keys for each target server <targetServerName, sessionKey>
    private Map<String, Key> sessionKeyMap;


    private WebInterfaceManager(){
        ticketCollection = new TicketCollection();
        sessionKeyMap = new HashMap<>();
    }

    private static class SingletonHolder {
        private static final WebInterfaceManager INSTANCE = new WebInterfaceManager();
    }

    public static synchronized WebInterfaceManager getInstance(){
        return WebInterfaceManager.SingletonHolder.INSTANCE;
    }

    public Key getSessionKey(String targetServerWsURL){
        return sessionKeyMap.get(targetServerWsURL);
    }

    public void addSessionKey(String targetServerWsURL, Key sessionKey){
        sessionKeyMap.put(targetServerWsURL, sessionKey);
    }

    public boolean sessionKeyExists(String targetServerWsURL){
        return sessionKeyMap.get(targetServerWsURL) != null;
    }

}
