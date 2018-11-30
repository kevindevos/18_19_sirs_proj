package sirs.webinterface.handlers;

import handlers.KerbistServerHandler;
import sirs.webinterface.domain.WebInterfaceManager;

public class KerbistWebIntServerHandler extends KerbistServerHandler {
    @Override
    protected void initHandlerVariables(){
        serverPassword = WebInterfaceManager.privatePassword;
        serverName = WebInterfaceManager.WEB_SERVER_NAME;
    }
}
