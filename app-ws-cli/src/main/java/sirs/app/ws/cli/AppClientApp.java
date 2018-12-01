package sirs.app.ws.cli;

import sirs.app.ws.NoteView;
import sirs.app.ws.cli.handlers.KerbistAppClientHandler;

import javax.xml.ws.handler.Handler;
import java.util.ArrayList;
import java.util.List;

/**
 * Client application. 
 * 
 * Looks for Apps using UDDI and arguments provided in pom.xml
 */
public class AppClientApp {

	public static void main(String[] args) throws Exception {
		// Check arguments.
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + AppClientApp.class.getName() + " wsURL");
			return;
		}
		String wsName = null;
		String wsURL = null;
		wsURL = args[0];

		// Create client.
		AppClient client = null;
		System.out.printf("Creating client for server at %s%n", wsURL);
		client = new AppClient(wsURL);

		System.out.println("Invoke ping()...");
		String result = client.testPing("Ping message");
		System.out.print("Result: ");
		System.out.println(result);
	}

}
