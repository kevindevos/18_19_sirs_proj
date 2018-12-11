package sirs.app.ws.cli;

import common.sirs.ws.NoteView;
import org.junit.Assert;
import org.junit.Test;
import sirs.app.ws.NotAllowed_Exception;
import sirs.app.ws.NoteNotFound_Exception;

import static junit.framework.TestCase.fail;

public class NoteAccessTest extends BaseTest {
    @Test
    public void getNoteSuccess(){
        try{
            NoteView note = appClient.getNoteByName(testNoteName);

            Assert.assertTrue(note != null);
            Assert.assertTrue(note.getName().equals(testNoteName));
            Assert.assertTrue(note.getOwner().equals(testNoteOwner));
        } catch(NoteNotFound_Exception e){
            fail();
        }
    }

    @Test
    public void addNoteSuccess(){
        NoteView newNote = new NoteView();
        String newNoteName = "newNoteName";

        newNote.setName(newNoteName);
        newNote.setContent("newNoteContent");
        newNote.setOwner("newNoteOwner");

        try{
            appClient.updateNote(newNote);
            NoteView retrievedNote = appClient.getNoteByName(newNoteName);

            Assert.assertTrue(retrievedNote.getName().equals(newNoteName));
            Assert.assertTrue(retrievedNote.getOwner().equals(newNote.getOwner()));
        }  catch(NoteNotFound_Exception e){
            fail();
        }
    }

    @Test
    public void updateExistingNoteSuccess(){
        try{
            NoteView note = appClient.getNoteByName(testNoteName);
            note.setContent(testNoteContent);
            appClient.updateNote(note);
            NoteView retrievedNote = appClient.getNoteByName(testNoteName);

            Assert.assertTrue(retrievedNote.getContent().equals(testNoteContent));
        } catch(NoteNotFound_Exception e){
            fail();
        }

    }

    @Test(expected = NoteNotFound_Exception.class)
    public void getNoteNotFound() throws NoteNotFound_Exception{
        appClient.getNoteByName("foo");
    }
}
