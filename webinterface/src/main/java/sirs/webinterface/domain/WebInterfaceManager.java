package sirs.webinterface.domain;

public class WebInterfaceManager {
    // List of valid kerby tickets
    public static final String KERBY_WS_URL = "http://localhost:8888/kerby";

    public final String WEB_SERVER_NAME = "WEB_SERVER";
    public String privatePassword;


    private WebInterfaceManager(){
    }

    private static class SingletonHolder {
        private static final WebInterfaceManager INSTANCE = new WebInterfaceManager();
    }

    public static synchronized WebInterfaceManager getInstance(){
        return WebInterfaceManager.SingletonHolder.INSTANCE;
    }

}
