package sirs.webinterface.handlers;

import handlers.KerbistServerHandler;
import sirs.webinterface.domain.WebInterfaceManager;

public class KerbistWebServerHandler extends KerbistServerHandler {
    @Override
    protected void initHandlerVariables(){
        serverPassword = WebInterfaceManager.getInstance().privatePassword;
        kerbistName = WebInterfaceManager.getInstance().WEB_SERVER_NAME;

    }
}
