package com.openclassrooms.realestatemanager.Provider;

import android.database.Cursor;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.Database.RealEstateManagerDataBase;
import com.openclassrooms.realestatemanager.Model.Address;
import com.openclassrooms.realestatemanager.Model.Property;
import com.openclassrooms.realestatemanager.Utils.Converter.AddressConverter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.openclassrooms.realestatemanager.Dummy.Dummy.sPropertyList;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PropertyContentProviderTest {

    private RealEstateManagerDataBase mDataBase;

    @Before
    public void setUp(){
         mDataBase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
            RealEstateManagerDataBase.class)
            .allowMainThreadQueries()
            .build();
    }

    @Test
    public void getPropertiesWithCursor(){
        for (Property property: sPropertyList) {
            mDataBase.mPropertyDAO().insertProperty(property);
        }
        final Cursor cursor = mDataBase.mPropertyDAO().getPropertiesWithCursor();
        assertEquals(sPropertyList.size(),cursor.getCount());
        if (cursor.moveToFirst()){ // data?
            assertTrue(sPropertyList.get(0).getId() == Integer.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
            assertTrue(sPropertyList.get(0).getPrice() == Integer.valueOf(cursor.getString(cursor.getColumnIndex("price"))));
            Address address = AddressConverter.stringToAddress(cursor.getString(cursor.getColumnIndex("mAddress")));
            assertTrue(sPropertyList.get(0).getAddress().getStreet().equals(address.getStreet()));
        }
        cursor.close();
    }
}
