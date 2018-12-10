package sirs.app.domain;

import common.sirs.ws.NoteView;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A note that a user can create, and it's owner can access
 */
public class Note {
    private String name;
    private String content;
    private String owner; // owner username
    private byte[] digest;

    public Note(String name, String content, String owner){
        this.name = name;
        setContent(content);
        this.owner = owner;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String aContent){
        content = aContent;
        digest = buildDigestFromContent();
    }

    private byte[] buildDigestFromContent(){
        MessageDigest digest = null;
        try{
            digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(content.getBytes(StandardCharsets.UTF_8));
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getOwner(){
        return owner;
    }

    public void setOwner(String aOwner){
        owner = aOwner;
    }

    public String getName(){
        return name;
    }

    public void setName(String aName){
        name = aName;
    }

    public byte[] getDigest(){
        return digest;
    }

    public NoteView toView(){
        NoteView noteView = new NoteView();

        noteView.setName(name);
        noteView.setContent(content);
        noteView.setOwner(owner);
        noteView.setDigest(noteView.getDigest());

        return noteView;
    }


}
