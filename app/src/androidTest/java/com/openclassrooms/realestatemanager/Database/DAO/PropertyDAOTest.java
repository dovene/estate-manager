package com.openclassrooms.realestatemanager.Database.DAO;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.Database.RealEstateManagerDataBase;
import com.openclassrooms.realestatemanager.Model.Property;
import com.openclassrooms.realestatemanager.Utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.openclassrooms.realestatemanager.Dummy.Dummy.sPropertyList;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PropertyDAOTest {

    private RealEstateManagerDataBase mDataBase;

    //Force execute each test synchronously.
    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    //Create instance of Db. Called before each test.
    // inMemoryDatabaseBuilder can create an instance directly in memory (not in a file in the hardware).
    @Before
    public void initDb() throws Exception {
        this.mDataBase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
            RealEstateManagerDataBase.class)
            .allowMainThreadQueries()
            .build();
    }

    @After
    public void closeDb() throws Exception {
        mDataBase.close();
    }

    @Test
    public void returnEmptyListIfNoPropertyInDataBase() throws InterruptedException {
        List<Property> propertyList = LiveDataTestUtil.getValue(this.mDataBase.mPropertyDAO().getAllProperty());
        assertTrue(propertyList.isEmpty());
    }

    @Test
    public void insertAndGetProperty() throws InterruptedException {
        //Add property
        this.mDataBase.mPropertyDAO().insertProperty(sPropertyList.get(0));
        //TEST
        Property property = LiveDataTestUtil.getValue(this.mDataBase.mPropertyDAO().getPropertyById(1));
        assertTrue(property.getAddress().getStreet().equals(sPropertyList.get(0).getAddress().getStreet())
        && property.getId() == sPropertyList.get(0).getId());
    }

    @Test
    public void InsertAndGetAllProperty() throws InterruptedException {
        for (Property property: sPropertyList) {
            this.mDataBase.mPropertyDAO().insertProperty(property);
        }
        List<Property> properties = LiveDataTestUtil.getValue(this.mDataBase.mPropertyDAO().getAllProperty());
        assertTrue(properties.size() == sPropertyList.size());
    }


    @Test
    public void InsertAndUpdateProperty() throws InterruptedException {
        int price = 12000000;
        String complementStreet = "6F, 3rd floor";
        this.mDataBase.mPropertyDAO().insertProperty(sPropertyList.get(1));
        Property property = LiveDataTestUtil.getValue(this.mDataBase.mPropertyDAO().getPropertyById(2));
        property.setPrice(price);
        property.getAddress().setComplement_street(complementStreet);
        this.mDataBase.mPropertyDAO().updateProperty(property);
        Property propertyWithNewValues = LiveDataTestUtil.getValue(this.mDataBase.mPropertyDAO().getPropertyById(2));
        assertTrue(propertyWithNewValues.getPrice() == price && propertyWithNewValues.getAddress().getComplement_street().equals(complementStreet));
    }
}
