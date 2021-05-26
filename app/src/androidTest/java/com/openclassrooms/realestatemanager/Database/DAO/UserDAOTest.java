package com.openclassrooms.realestatemanager.Database.DAO;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.Database.RealEstateManagerDataBase;
import com.openclassrooms.realestatemanager.Model.User;
import com.openclassrooms.realestatemanager.Utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.openclassrooms.realestatemanager.Dummy.Dummy.sUser;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UserDAOTest {

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
    public void createUserAndGetUser() throws InterruptedException {
        this.mDataBase.mUserDAO().createUser(sUser);
        User user = LiveDataTestUtil.getValue(mDataBase.mUserDAO().getUserById(1));
        assertTrue(user.getName().equals(sUser.getName()) && user.getUid() == sUser.getUid());
    }

    @Test
    public void InsertAndConnectUser() throws InterruptedException {
        this.mDataBase.mUserDAO().createUser(sUser);
        User connectedUser = LiveDataTestUtil.getValue(mDataBase.mUserDAO().connectUser(sUser.getName(),sUser.getPassword()));
        assertTrue(connectedUser.getName().equals(sUser.getName()) && connectedUser.getUid() == sUser.getUid());
    }
}
