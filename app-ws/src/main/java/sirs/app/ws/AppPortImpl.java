package sirs.app.ws;


import pt.ulisboa.tecnico.sdis.kerby.SecurityHelper;
import sirs.app.domain.Note;
import sirs.app.domain.NotesManager;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;



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
    private static final String VALID_SERVER_PASSWORD = "nhdchdps";
    public static Key kcsSessionKey;
    public static Key serverKey;

    public AppPortImpl(){
        serverKey = generateServerKeyFromPassword();
    }

    // end point manager
    private AppEndpointManager endpointManager;

    public AppPortImpl(AppEndpointManager endpointManager) {
        serverKey = generateServerKeyFromPassword();
        this.endpointManager = endpointManager;
    }

    private Key generateServerKeyFromPassword(){
        try{
            return SecurityHelper.generateKeyFromPassword(VALID_SERVER_PASSWORD);
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch(InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String testPing(String inputMessage) {
        System.out.println("Server received message: " + inputMessage);
        return "app-ws says hi!";
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

    /**
     * Add if not exist, update if exists and if has permissions, or throw exception if not allowed
     * @param noteView data object of the note to update
     * @throws NotAllowed_Exception
     */
    @Override
    public void updateNote(NoteView noteView) throws NotAllowed_Exception{
        System.out.println("updateNote");
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


}
