package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UtilsTest {

    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void checkIfInternetAvailable() {
        int responseCode = 0;
        if (Utils.isInternetAvailable(appContext)){
            try{
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(300);
                urlConnection.connect();
                responseCode = urlConnection.getResponseCode();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        assertTrue(responseCode == 200);
    }

}
