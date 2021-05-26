package com.openclassrooms.realestatemanager.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private long Uid;
    private String name;
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    //FOR TEST
    @Ignore
    public User(int id, String name, String password) {
        this.Uid = id;
        this.name = name;
        this.password = password;
    }

    public long getUid() {
        return Uid;
    }

    public void setUid(long uid) {
        this.Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
