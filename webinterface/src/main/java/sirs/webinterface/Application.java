package sirs.webinterface;

import com.sun.xml.ws.client.ClientTransportException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.ulisboa.tecnico.sdis.kerby.TicketCollection;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClientException;
import sirs.webinterface.domain.WebInterfaceManager;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
        // run springboot app
		SpringApplication.run(Application.class, args);

        setupKerbyConnection();
    }

    private static void setupKerbyConnection(){
        while(true){
            try{
                // generate a password to use with kerby
                KerbyClient kerbyClient = new KerbyClient(WebInterfaceManager.KERBY_WS_URL);
                WebInterfaceManager.getInstance().privatePassword = kerbyClient.generateDHPassword(WebInterfaceManager.getInstance().WEB_SERVER_NAME);

                return;
            }catch(ClientTransportException cte){
                System.err.println("Unable to contact Kerbist, retrying...");

                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }




}
