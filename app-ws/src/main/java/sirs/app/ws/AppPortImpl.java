package sirs.app.ws;


import common.sirs.ws.NoteDigestView;
import common.sirs.ws.NoteView;
import sirs.Security;
import sirs.app.domain.Note;
import sirs.app.domain.NotesManager;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@HandlerChain(file = "/app-ws_handler-chain.xml")
@WebService(
        endpointInterface = "sirs.app.ws.AppPortType",
        wsdlLocation = "app.wsdl",
        name ="AppWebService",
        portName = "AppPort",
        targetNamespace="http://ws.app.sirs/",
        serviceName = "AppService"
)
public class AppPortImpl implements AppPortType {
    public static String privatePassword;

    public AppPortImpl(){}

    // end point manager
    private AppEndpointManager endpointManager;

    public AppPortImpl(AppEndpointManager endpointManager) {
        this.endpointManager = endpointManager;
    }

    @Override
    public NoteView getNoteByName(String noteName) throws NoteNotFound_Exception{
        System.out.println("getNoteByName");
        Note note = NotesManager.getInstance().getNoteByName(noteName);
        if(note != null){
            return note.toView();
        }else{
            throw new NoteNotFound_Exception("", null);
        }
    }

    @Override
    public List<NoteView> getNotesByUser(String username){
        List<Note> notes = NotesManager.getInstance().getNotesByUser(username);
        List<NoteView> noteViews = new ArrayList<>();

        for(Note note: notes){
            noteViews.add(note.toView());
        }

        return noteViews;
    }

    @Override
    public List<NoteDigestView> getAllNoteDigests(String username){
        List<Note> notes =  NotesManager.getInstance().getAllNotes();
        List<NoteDigestView> digestViews = new ArrayList<>();

        for(int i = 0; i < notes.size(); i++){
            NoteDigestView noteDigestView = new NoteDigestView();

            noteDigestView.setNoteView(notes.get(i).toView());
            noteDigestView.setDigest(Security.buildDigestFrom(notes.get(i).getContent()));

            digestViews.add(noteDigestView);
        }

        return digestViews;
    }


    /**
     * Add if not exist, update if exists and if has permissions, or throw exception if not allowed
     * @param noteView data object of the note to update
     * @throws NotAllowed_Exception
     */
    @Override
    public void updateNote(NoteView noteView) throws NotAllowed_Exception{
        Note note = NotesManager.getInstance().getNoteByName(noteView.getName());
        if(note != null ){
            // update contents if same owner
            if(note.getOwner().equals(noteView.getOwner())){
                note.setContent(noteView.getContent());
            }else{
                throw new NotAllowed_Exception("", null);
            }
        }else{
            Note newNote = new Note(noteView.getName(), noteView.getContent(), noteView.getOwner());
            NotesManager.getInstance().addNote(newNote);
        }
    }

    /**
     * Clear server data for testing
     */
    @Override
    public void testClear(){
        NotesManager.getInstance().clearNotes();
    }

    /**
     * Setup the server for testing
     */
    @Override
    public void testInit(){
        // Add a simple note for testing purposes
        Note testNote = new Note("TEST_NOTE", "TEST_NOTE_CONTENT", "TEST_USER");
        NotesManager.getInstance().addNote(testNote);
    }

    @Override
    public String testPing(String inputMessage) {
        System.out.println("Server received message: " + inputMessage);
        return "hello";
    }


}
