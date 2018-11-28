package sirs.webinterface.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages the Registration and maintenance of Users
 *
 */
public class UsersManager {
    private List<User> users;

    private UsersManager(){
        users = new ArrayList<>();
    }

    private static class SingletonHolder {
        private static final UsersManager INSTANCE = new UsersManager();
    }

    public static synchronized UsersManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void addUser(User user){
        users.add(user);
    }

    public User getUser(String username){
        for(User user: users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public boolean userExists(String username){
        for(User user : users){
            if(user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

}