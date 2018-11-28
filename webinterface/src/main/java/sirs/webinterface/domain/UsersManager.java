package sirs.webinterface.domain;

/**
 * Class that manages the Registration and maintenance of Users
 *
 */
public class UsersManager {

    private UsersManager(){ }

    private static class SingletonHolder {
        private static final UsersManager INSTANCE = new UsersManager();
    }

    public static synchronized UsersManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

}