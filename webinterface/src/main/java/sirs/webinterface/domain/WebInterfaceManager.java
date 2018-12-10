package sirs.webinterface.domain;

public class WebInterfaceManager {
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
