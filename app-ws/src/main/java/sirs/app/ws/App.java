package sirs.app.ws;


import sirs.app.ws.AppEndpointManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class App {

    public static void main(String[] args) throws Exception {
        // Check arguments
        if (args.length == 0 || args.length == 2) {
            System.err.println("Argument(s) missing!");
            System.err.println("Usage: java " + App.class.getName() + " wsURL");
            return;
        }
        String wsName = null;
        String wsURL = null;

        // Create server implementation object, according to options
        AppEndpointManager endpoint = null;
        if (args.length == 1) {
            wsURL = args[0];
            endpoint = new AppEndpointManager(wsURL);
        }

        // load App properties
        try {
            InputStream inputStream = App.class.getResourceAsStream("/app.properties");

            Properties properties = new Properties();
            properties.load(inputStream);

            System.out.println("Loaded properties:");
            System.out.println(properties);


        } catch (IOException e) {
            System.out.printf("Failed to load configuration: %s%n", e);
        }


        try {
            endpoint.start();
            endpoint.awaitConnections();
        } finally {
            endpoint.stop();
        }

    }

}