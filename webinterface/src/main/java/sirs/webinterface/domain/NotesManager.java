package sirs.webinterface.domain;


import sirs.app.ws.NotAllowed_Exception;
import sirs.app.ws.NoteNotFound_Exception;
import sirs.app.ws.NoteView;
import sirs.app.ws.cli.AppClient;
import sirs.app.ws.cli.AppClientConnectionManager;
import sirs.webinterface.exception.NoteNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 *  Query the servers for specific notes
 */
public class NotesManager {
    AppClientConnectionManager connectionManager;

    private NotesManager(){
        connectionManager = AppClientConnectionManager.getInstance();
    }

    public NoteView askForNoteByName(String noteName, String username) throws NoteNotFoundException{
        for(AppClient appClient : connectionManager.getAllConnections()){
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

    public List<NoteView> askForAllUserNotes(String username){
        List<NoteView> notes = new ArrayList<>();

        for(AppClient appClient : connectionManager.getAllConnections()){
            notes.addAll(appClient.getNotesByUser(username));
        }

        return notes;
    }

    public void updateNote(NoteView noteView){
        AppClient appClient = connectionManager.getRandomConnection();

        try{
            appClient.updateNote(noteView);
        } catch(NotAllowed_Exception e){
            e.printStackTrace();
        }
    }

    private static class SingletonHolder {
        private static final NotesManager INSTANCE = new NotesManager();
    }

    public static synchronized NotesManager getInstance(){
        return NotesManager.SingletonHolder.INSTANCE;
    }
}
