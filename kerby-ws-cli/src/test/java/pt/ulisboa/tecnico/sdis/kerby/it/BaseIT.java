package pt.ulisboa.tecnico.sdis.kerby.it;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import sirs.kerby.cli.KerbyClient;

import java.io.IOException;
import java.util.Properties;

public class BaseIT {

	private static final String TEST_PROP_FILE = "/test.properties";
	protected static Properties testProps;

	protected static KerbyClient client;

	@BeforeClass
	public static void oneTimeSetup() throws Exception {
		testProps = new Properties();
		try {
			testProps.load(BaseIT.class.getResourceAsStream(TEST_PROP_FILE));
			System.out.println("Loaded test properties:");
			System.out.println(testProps);
		} catch (IOException e) {
			final String msg = String.format("Could not load properties file {}", TEST_PROP_FILE);
			System.out.println(msg);
			throw e;
		}

		String wsURL = testProps.getProperty("ws.url");
		client = new KerbyClient();
		// CLIENT.setVerbose(true);
	}

	@AfterClass
	public static void cleanup() {
	}

}
