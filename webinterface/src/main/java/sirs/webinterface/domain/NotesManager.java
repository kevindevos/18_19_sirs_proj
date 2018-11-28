package sirs.webinterface.domain;


import sirs.app.ws.NoteNotFound_Exception;
import sirs.app.ws.NoteView;
import sirs.app.ws.cli.AppClient;
import sirs.webinterface.exception.NoteNotFoundException;

import java.util.List;

/**
 *  Query the servers for specific notes
 */
public class NotesManager {
    private NotesManager(){
    }

    public NoteView askForNoteByName(String noteName, String username) throws NoteNotFoundException{
        List<AppClient> connections = ConnectionManager.getInstance().createConnectionsWithAllAppServers();

        for(AppClient appClient : connections){
            try{
                NoteView noteView = appClient.getNoteByName(noteName);
                if(noteView.getOwner().equals(username)){
                    return noteView;
                }
            } catch(NoteNotFound_Exception e){
                continue;
            }
        }

        throw new NoteNotFoundException("");
    }

    private static class SingletonHolder {
        private static final NotesManager INSTANCE = new NotesManager();
    }

    public static synchronized NotesManager getInstance(){
        return NotesManager.SingletonHolder.INSTANCE;
    }
}
