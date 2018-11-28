package sirs.webinterface.domain;


import sirs.app.ws.cli.AppClient;

import java.util.List;

/**
 * Manages user notes by querying AppServers that store these notes
 */
public class NotesManager {
    private NotesManager(){
    }

    public Note findNoteByName(String noteName){
        List<AppClient> connections = ConnectionManager.getInstance().createConnectionsWithAllAppServers();

        for(AppClient appClient : connections){
            // appClient.getNoteByName(noteName)
        }

        return null;
    }

    private static class SingletonHolder {
        private static final NotesManager INSTANCE = new NotesManager();
    }

    public static synchronized NotesManager getInstance(){
        return NotesManager.SingletonHolder.INSTANCE;
    }
}
