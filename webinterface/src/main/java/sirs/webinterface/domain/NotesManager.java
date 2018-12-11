package sirs.webinterface.domain;


import common.sirs.ws.NoteView;
import sirs.app.ws.NotAllowed_Exception;
import sirs.app.ws.NoteNotFound_Exception;
import sirs.app.ws.cli.AppClient;
import sirs.app.ws.cli.AppClientConnectionManager;
import sirs.webinterface.exception.NoteNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  Query the servers for specific notes
 */
public class NotesManager {
    private AppClientConnectionManager connectionManager;

    // note name, noteview
    private HashMap<String, NoteView> recentlyChangedNotes;

    private NotesManager(){
        connectionManager = AppClientConnectionManager.getInstance();
        recentlyChangedNotes = new HashMap<>();
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
            updateRecentlyChangedNote(noteView);
        } catch(NotAllowed_Exception e){
            e.printStackTrace();
        }
    }

    private void updateRecentlyChangedNote(NoteView noteView){
        NoteView existingNoteView = recentlyChangedNotes.get(noteView.getName());
        if(existingNoteView != null){
            recentlyChangedNotes.replace(noteView.getName(), noteView);
        }else{
            recentlyChangedNotes.put(noteView.getName(), noteView);
        }
    }

    public List<NoteView> takeRecentlyChangedNotes(){
        ArrayList<NoteView> notes = new ArrayList<>(recentlyChangedNotes.values());
        recentlyChangedNotes.clear();

        return notes;
    }

    private static class SingletonHolder {
        private static final NotesManager INSTANCE = new NotesManager();
    }

    public static synchronized NotesManager getInstance(){
        return NotesManager.SingletonHolder.INSTANCE;
    }
}
