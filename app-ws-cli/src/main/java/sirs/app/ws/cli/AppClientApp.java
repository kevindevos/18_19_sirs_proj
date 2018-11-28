package sirs.app.ws.cli;

import sirs.app.ws.NoteView;

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
		NoteView noteView = new NoteView();
		noteView.setName("helloName");
		client.updateNote(noteView);
		//System.out.print("Result: ");
		//7System.out.println(result);
	}

}
