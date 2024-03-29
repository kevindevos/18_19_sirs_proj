package sirs.app.ws.cli;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BaseTest {
    protected static String wsUrl = "http://localhost:8081/app-ws/endpoint";
    protected static AppClient appClient = new AppClient(wsUrl);

    protected String testNoteName = "TEST_NOTE";
    protected String testNoteContent = "TEST_NOTE_CONTENT";
    protected String testNoteOwner = "TEST_USER";

    @BeforeClass
    public static void oneTimeSetup() throws Exception{
        appClient.testInit();
    }

    @AfterClass
    public static void cleanup() {
        appClient.testClear();
    }

}
