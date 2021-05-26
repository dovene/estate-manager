package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.Model.Property;

import org.junit.Test;

import static com.openclassrooms.realestatemanager.Dummy.Dummy.facilities;
import static com.openclassrooms.realestatemanager.Dummy.Dummy.sPropertyList;
import static com.openclassrooms.realestatemanager.Utils.SearchUtils.checkIfPropertyHasSearchParameter;
import static org.junit.Assert.*;

public class SearchUtilsTest {

    /**
     * Check if the first property in Dummy class has the condition of search :
     * Minimum price 10 000 000$ and maximum 50 000 000$ with surface min of 100m2
     * No condition for nbr of room contains facilities list and in district of new york.
     * Should return true;
     */
    @Test
    public void checkIfDummyPropertyHasSearchParameter() {
        Property property = sPropertyList.get(0);
        Boolean isPropertySearchValid = checkIfPropertyHasSearchParameter(property,10000000,50000000,100,
            null,0,null,null,null,facilities,"NewYork");
        assertTrue(isPropertySearchValid);
    }
}
