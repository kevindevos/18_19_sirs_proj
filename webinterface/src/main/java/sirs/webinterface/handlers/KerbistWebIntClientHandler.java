package sirs.webinterface.handlers;

import handlers.KerbistClientHandler;
import sirs.webinterface.domain.WebInterfaceManager;

public class KerbistWebIntClientHandler extends KerbistClientHandler {

    @Override
    protected void initHandlerVariables(){
        kerbistName = WebInterfaceManager.WEB_SERVER_NAME;
        kerbistClientPassword = WebInterfaceManager.privatePassword;
        ticketCollection = WebInterfaceManager.ticketCollection;
        sessionKeyMap = WebInterfaceManager.sessionKeyMap;
    }

}
