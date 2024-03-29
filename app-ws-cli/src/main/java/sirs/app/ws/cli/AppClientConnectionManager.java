package sirs.app.ws.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AppClientConnectionManager {
    private List<String> appServerWsUrls;
    private List<String> defaultPorts;
    private String defaultHost;

    private List<AppClient> connections;

    private AppClientConnectionManager(){
        appServerWsUrls = new ArrayList<>();
        defaultPorts = new ArrayList<>(Arrays.asList("8081")); // "8082", "8083"));
        defaultHost = "localhost";

        initAppServerWsUrls();
        connections = createConnections();
    }

    private static class SingletonHolder {
        private static final AppClientConnectionManager INSTANCE = new AppClientConnectionManager();
    }

    public static synchronized AppClientConnectionManager getInstance(){
        return SingletonHolder.INSTANCE;
    }


    private void initAppServerWsUrls(){
        for(String port : defaultPorts){
            appServerWsUrls.add(buildWsUrl(defaultHost, port));
        }
    }

    private List<AppClient> createConnections(){
        List<AppClient> connections = new ArrayList<>();

        AppClient appClient;
        for(String wsUrl : appServerWsUrls){
            appClient = new AppClient(wsUrl);
            appClient.setMaxRetries(Integer.MAX_VALUE);
            connections.add(appClient);
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
