package sirs.webinterface.handlers;

import handlers.KerbistClientHandler;
import sirs.webinterface.domain.WebInterfaceManager;

public class KerbistWebIntClientHandler extends KerbistClientHandler {

    @Override
    protected void initHandlerVariables(){
        kerbistClientName = WebInterfaceManager.WEB_SERVER_NAME;
        ticketCollection = WebInterfaceManager.ticketCollection;
        sessionKeyMap = WebInterfaceManager.sessionKeyMap;
    }

}
