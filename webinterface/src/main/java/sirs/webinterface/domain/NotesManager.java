package sirs.webinterface.domain;

import sirs.app.ws.cli.AppClient;

/**
 * Manages user notes by querying AppServers that store these notes
 */
public class NotesManager {
    private NotesManager(){
    }

    private static class SingletonHolder {
        private static final NotesManager INSTANCE = new NotesManager();
    }

    public static synchronized NotesManager getInstance(){
        return NotesManager.SingletonHolder.INSTANCE;
    }
}
