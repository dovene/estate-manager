package com.openclassrooms.realestatemanager.Database.DAO;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.Model.Property;

import java.util.List;

@Dao
public interface PropertyDAO {

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAllProperty();

    @Query("SELECT * FROM Property WHERE id = :id")
    LiveData<Property> getPropertyById(Integer id);

    @Insert
    void insertProperty(Property property);

    @Update
    void updateProperty(Property property);

    //For ContentProvider
    @Query("SELECT * FROM property")
    Cursor getPropertiesWithCursor();
}
