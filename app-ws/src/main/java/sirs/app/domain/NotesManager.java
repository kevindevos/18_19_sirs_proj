package sirs.app.domain;


import java.util.ArrayList;
import java.util.List;

/**
 *  Query the servers for specific notes
 */
public class NotesManager {
    private List<Note> notes = new ArrayList<>();

    private NotesManager(){
    }

    public Note getNoteByName(String noteName){
        for(Note note : notes){
            if(note.getName().equals(noteName)){
                return note;
            }
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
