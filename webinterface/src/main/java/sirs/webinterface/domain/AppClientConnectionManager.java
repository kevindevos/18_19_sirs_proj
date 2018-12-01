package sirs.webinterface.domain;

import sirs.app.ws.cli.AppClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AppClientConnectionManager {
    private List<String> appServerWsUrls;
    private List<String> defaultPorts;
    private String defaultHost;

    private List<AppClient> connections;

    public AppClientConnectionManager(){
        appServerWsUrls = new ArrayList<>();
        defaultPorts = new ArrayList<>(Arrays.asList("8081", "8082", "8083"));
        defaultHost = "localhost";

        initAppServerWsUrls();
        connections = createConnections();
    }

    private void initAppServerWsUrls(){
        for(String port : defaultPorts){
            appServerWsUrls.add(buildWsUrl(defaultHost, port));
        }
    }

    private List<AppClient> createConnections(){
        List<AppClient> connections = new ArrayList<>();

        for(String wsUrl : appServerWsUrls){
            connections.add(new AppClient(wsUrl));
        }

        return connections;
    }

    public AppClient getRandomConnection(){
        Random random = new Random();
        int rand = random.nextInt(connections.size());

        return connections.get(rand);
    }

    public List<AppClient> getAllConnections(){
        return connections;
    }

    public String buildWsUrl(String host, String port){
        return "http://"+host+":"+port+"/app-ws/endpoint";
    }
}
