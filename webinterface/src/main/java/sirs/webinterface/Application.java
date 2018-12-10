package sirs.webinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;
import sirs.webinterface.domain.WebInterfaceManager;
import sirs.webinterface.ws.WebEndpointManager;


@SpringBootApplication
public class Application {
    private static String WS_URL = "http://localhost:8185/web-ws/endpoint";

	public static void main(String[] args) {
        // run springboot app
		SpringApplication.run(Application.class, args);

        setupKerbyConnection();

        // setup web service endpoint
        Runnable runnable = Application::setupWebServiceEndpoint;
        Thread thread = new Thread(runnable);
        thread.start();

    }

    private static void setupKerbyConnection(){
        // generate a password to use with kerby
        KerbyClient kerbyClient = new KerbyClient();
        WebInterfaceManager.getInstance().privatePassword = kerbyClient.generateDHPassword(WebInterfaceManager.getInstance().WEB_SERVER_NAME);
    }

    private static void setupWebServiceEndpoint(){
        // Create server implementation object, according to options
        WebEndpointManager endpoint = new WebEndpointManager(WS_URL);

        try {
            endpoint.start();
            endpoint.awaitConnections();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                endpoint.stop();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }




}
