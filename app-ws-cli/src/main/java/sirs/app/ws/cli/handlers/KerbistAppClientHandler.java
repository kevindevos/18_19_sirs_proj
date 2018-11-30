package sirs.app.ws.cli.handlers;

import handlers.KerbistClientHandler;
import sirs.app.ws.cli.AppClient;

public class KerbistAppClientHandler extends KerbistClientHandler {
    @Override
    protected void initHandlerVariables(){
        kerbistClientName = AppClient.wsURL;
        kerbistClientPassword = AppClient.privatePassword;
        ticketCollection = AppClient.ticketCollection;
        sessionKeyMap = AppClient.sessionKeyMap;
    }
}