package sirs.app.domain;


import sirs.app.ws.NoteView;

import java.util.ArrayList;
import java.util.List;

/**
 *  Query the servers for specific notes
 */
public class NotesManager {
    private List<Note> notes = new ArrayList<>();

    private NotesManager(){
    }

    /**
     * Get a note searching by name
     * @param noteName note's name
     * @return Note obj
     */
    public Note getNoteByName(String noteName){
        for(Note note : notes){
            if(note.getName().equals(noteName)){
                return note;
            }
        }
        return null;
    }

    public void removeNote(Note note){
        notes.remove(note);
    }

    public void clearNotes(){
        notes.clear();
    }

    public void addNote(Note note){
        notes.add(note);
    }

    private static class SingletonHolder {
        private static final NotesManager INSTANCE = new NotesManager();
    }

    public static synchronized NotesManager getInstance(){
        return NotesManager.SingletonHolder.INSTANCE;
    }


}
