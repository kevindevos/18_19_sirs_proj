package pt.ulisboa.tecnico.sdis.kerby.cli;

import pt.ulisboa.tecnico.sdis.kerby.SessionKeyAndTicketView;

import java.util.Random;

public class KerbyClientApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + KerbyClientApp.class.getName() + " wsURL OR uddiURL wsName");
			return;
		}
		String wsName = null;
		String wsURL = null;
		if (args.length == 1) {
			wsURL = args[0];
		}

		// Create client
		KerbyClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new KerbyClient();
		}
		// the following remote invocations are just basic examples
		// the actual tests are made using JUnit

		System.out.println("Invoke dummy()...");
		SessionKeyAndTicketView result = client.requestTicket("alice@CXX.binas.org", "binas@CXX.binas.org",
				new Random().nextLong(), 60 /* seconds */);
		System.out.print("Result: ");
		System.out.println(result);

	}

}
