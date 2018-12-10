package sirs.ws.cli.handlers;

import handlers.KerbistClientHandler;
import pt.ulisboa.tecnico.sdis.kerby.TicketCollection;

import java.security.Key;
import java.util.Map;

public class KerbistWebClientHandler extends KerbistClientHandler {

    @Override
    protected void initHandlerVariables(){
    }

    public KerbistWebClientHandler(String clientName, String clientPassword, TicketCollection clientTicketCollection, Map<String, Key> clientSessionKeyMap){
        kerbistClientPassword = clientPassword;
        ticketCollection = clientTicketCollection;
        sessionKeyMap = clientSessionKeyMap;
        kerbistName = clientName;
        targetName = "WEB_SERVER";
    }

}
