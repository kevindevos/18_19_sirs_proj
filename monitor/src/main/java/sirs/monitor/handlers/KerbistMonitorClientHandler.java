package sirs.monitor.handlers;

import handlers.KerbistClientHandler;
import sirs.monitor.Monitor;

public class KerbistMonitorClientHandler extends KerbistClientHandler {

    @Override
    protected void initHandlerVariables(){
        kerbistName = Monitor.MONITOR_NAME;
        kerbistClientPassword = Monitor.privatePassword;
        ticketCollection = Monitor.ticketCollection;
        sessionKeyMap = Monitor.sessionKeyMap;

    }

}
