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

    public NoteView toView(){
        NoteView noteView = new NoteView();

        noteView.setName(name);
        noteView.setContent(content);
        noteView.setOwner(owner);

        return noteView;
    }


}
