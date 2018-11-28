package sirs.webinterface.domain;

import sirs.app.ws.cli.AppClient;
import sirs.webinterface.exception.ConnectionException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class ConnectionManager {
    private List<String> appServerWsUrls;

    private ConnectionManager(){
        appServerWsUrls = new ArrayList<>();
    }

    // default list of servers ( bootstrapping )
    private void initAppServerWsUrls(){
        String defaultHost = "localhost";
        List<String> defaultPorts = new ArrayList<String>(Arrays.asList("8081","8082","8083"));

        for(String port : defaultPorts){
            appServerWsUrls.add(buildWsUrl(defaultHost, port));
        }
    }

    /**
     * Create a connection with a random server in appServerWsUrls
     * @return AppClient instance
     */
    public AppClient createConnectionWithRandomAppServer(){
        int rand = ThreadLocalRandom.current().nextInt(0, appServerWsUrls.size());
        return new AppClient(appServerWsUrls.get(rand));
    }

    /**
     * Create a list of connections with all App servers
     * @return List<AppClient>
     */
    public List<AppClient> createConnectionsWithAllAppServers(){
        List<AppClient> connections = new ArrayList<>();

        for(String wsUrl : appServerWsUrls){
            connections.add(new AppClient(wsUrl));
        }

        return connections;
    }

    /**
     * Create a connection with a specific WsUrl, the url must be in appServerWsUrls
     * @return AppClient instance
     */
    public AppClient createConnectionToAppServer(String wsUrl) throws ConnectionException{
        if(appServerExists(wsUrl)){
            return new AppClient(wsUrl);
        } else {
            throw new ConnectionException("Server not found.");
        }
    }

    public boolean appServerExists(String wsUrl){
        return appServerWsUrls.contains(wsUrl);
    }

    private static class SingletonHolder {
        private static final ConnectionManager INSTANCE = new ConnectionManager();
    }

    public static synchronized ConnectionManager getInstance(){
        return ConnectionManager.SingletonHolder.INSTANCE;
    }

    public String buildWsUrl(String host, String port){
        return "http://"+host+":"+port+"/app-ws/endpoint";
    }
}
