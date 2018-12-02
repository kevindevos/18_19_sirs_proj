package sirs.app.ws.cli.handlers;

import handlers.KerbistClientHandler;
import pt.ulisboa.tecnico.sdis.kerby.TicketCollection;

import java.security.Key;
import java.util.Map;

public class KerbistAppClientHandler extends KerbistClientHandler {

    @Override
    protected void initHandlerVariables(){
    }

    public KerbistAppClientHandler(String clientName, String clientPassword, TicketCollection clientTicketCollection, Map<String, Key> clientSessionKeyMap){
        kerbistClientPassword = clientPassword;
        ticketCollection = clientTicketCollection;
        sessionKeyMap = clientSessionKeyMap;
        kerbistName = clientName;
    }

}
