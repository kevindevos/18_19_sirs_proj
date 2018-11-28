package sirs.webinterface.domain;

/**
 * A note that a user can create, and it's owner can access
 */
public class Note {
    private String name;
    private String content;
    private User owner;

    public Note(String name, String content, User owner){
        this.name = name;
        this.content = content;
        this.owner = owner;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String aContent){
        content = aContent;
    }

    public User getOwner(){
        return owner;
    }

    public void setOwner(User aOwner){
        owner = aOwner;
    }

    public String getName(){
        return name;
    }

    public void setName(String aName){
        name = aName;
    }
}
