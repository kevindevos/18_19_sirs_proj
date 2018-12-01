package sirs.webinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.ulisboa.tecnico.sdis.kerby.TicketCollection;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClientException;
import sirs.webinterface.domain.WebInterfaceManager;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
        // generate a password to use with kerby on boot
        KerbyClient kerbyClient = new KerbyClient(WebInterfaceManager.KERBY_WS_URL);
        WebInterfaceManager.getInstance().privatePassword = kerbyClient.generateDHPassword(WebInterfaceManager.getInstance().WEB_SERVER_NAME);

        // run springboot app
		SpringApplication.run(Application.class, args);
	}

}
