package com.openclassrooms.realestatemanager.Repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.Database.DAO.UserDAO;
import com.openclassrooms.realestatemanager.Model.User;

public class UserRepository {

    private UserDAO mUserDAO;

    //CONSTRUCTOR
    public UserRepository(UserDAO userDAO) {
        this.mUserDAO = userDAO;
    }

    /**
     * Get an User by is name and password to connect it.
     * @param name
     * @param password
     * @return
     */
    public LiveData<User> getUser(String name,String password){
        return mUserDAO.connectUser(name,password);
    }

    /**
     * Add User in database
     * @param user
     * @return
     */
    public long createUser(User user){
        return mUserDAO.createUser(user);
    }

    /**
     * Get a User by is id
     * @param id
     * @return
     */
    public LiveData<User> getUser(long id) {
        return mUserDAO.getUserById(id);
    }
}
