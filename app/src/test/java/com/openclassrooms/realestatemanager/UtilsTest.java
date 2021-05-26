package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.Utils.Utils;

import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class UtilsTest {

    int mDollars = 100;
    int mEuros = 81;

    @Test
    public void convertDollarToEuro() {
        int euros = Utils.convertDollarToEuro(mDollars);
        assertEquals(euros,81);
    }

    @Test
    public void convertEuroToDollar() {
        int dollars = Utils.convertEuroToDollar(mEuros);
        assertEquals(dollars,100);
    }

    @Test
    public void getTodayDate() {
        String date = Utils.getTodayDate();
        assertTrue(isValidDate(date));
    }

    public boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.parse(date);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
