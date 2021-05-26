package com.openclassrooms.realestatemanager.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.Model.Address;
import com.openclassrooms.realestatemanager.Model.User;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM User WHERE name = :name AND password = :password")
    LiveData<User> connectUser(String name, String password);

    @Insert
    long createUser(User user);

    @Query("SELECT * FROM User WHERE Uid = :id")
    LiveData<User> getUserById(long id);
}
