package sirs.ws.cli;

/**
 * Client application. 
 * 
 * Looks for Apps using UDDI and arguments provided in pom.xml
 */
public class WebClientApp {

	public static void main(String[] args) throws Exception {
		// Check arguments.
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + WebClientApp.class.getName() + " wsURL");
			return;
		}
		String wsName = null;
		String wsURL = null;
		wsURL = args[0];

		// Create client.
		WebClient client = null;
		System.out.printf("Creating client for server at %s%n", wsURL);
		client = new WebClient(wsURL);

		System.out.println("Invoke ping()...");
		String result = client.testPing("Ping message");
		System.out.print("Result: ");
		System.out.println(result);
	}

}
