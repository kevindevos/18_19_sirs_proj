package sirs.app.handlers;

import handlers.KerbistServerHandler;
import sirs.app.ws.AppEndpointManager;
import sirs.app.ws.AppPortImpl;

public class KerbistAppServerHandler extends KerbistServerHandler {
    @Override
    protected void initHandlerVariables(){
        serverPassword = AppPortImpl.privatePassword;
        kerbistName = AppEndpointManager.wsURL;
    }
}
