package java.sirs.app.ws.cli;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import sirs.app.ws.cli.AppClient;

public class BaseTest {
    protected AppClient appClient;
    protected String wsUrl = "http://localhost:8081/app-ws/endpoint";

    public BaseTest(){
        appClient = new AppClient(wsUrl);
    }

    @BeforeClass
    public static void oneTimeSetup() throws Exception{

    }

    @AfterClass
    public static void cleanup() {

    }

}
