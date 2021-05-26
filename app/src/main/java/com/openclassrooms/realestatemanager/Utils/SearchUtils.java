package com.openclassrooms.realestatemanager.Utils;

import com.openclassrooms.realestatemanager.Model.Property;

import java.util.Date;
import java.util.List;

public class SearchUtils {

    public static boolean checkIfPropertyHasSearchParameter(Property property, Integer priceMin, Integer priceMax, Integer surfaceMin,
                                                            Integer surfaceMax, Integer nbrRoomMin, Integer nbrRoomMax, Date dateMinEntry,
                                                            Date dateMinSold, List<String> facilitiesList, String district){
        if (isBetweenPriceMinAndMax(property,priceMin,priceMax) && isBetweenSurfaceMinAndMax(property,surfaceMin,surfaceMax) &&
            isBetweenNbrOfRoomMinAndMax(property,nbrRoomMin,nbrRoomMax) && isAfterMinDateOfEntry(property,dateMinEntry) &&
            isAfterMinDateOfSold(property,dateMinSold) && isInDistrict(property,district) && isContainsFacilities(property,facilitiesList)){
            return true;
        }
        return false;
    }

    private static boolean isBetweenPriceMinAndMax(Property property,Integer priceMin,Integer priceMax){
        if (property.getPrice() >= priceMin && priceMax == null ||
            property.getPrice() >= priceMin && property.getPrice() <= priceMax) {
            return true;
        }
        return false;
    }

    private static boolean isBetweenSurfaceMinAndMax(Property property, Integer surfaceMin, Integer surfaceMax){
        if (property.getSurface() >= surfaceMin && surfaceMax == null ||
            property.getSurface() >= surfaceMin && property.getSurface() <= surfaceMax){
            return true;
        }
        return false;
    }

    private static boolean isBetweenNbrOfRoomMinAndMax(Property property, Integer nbrRoomMin, Integer nbrRoomMax){
        if (property.getNbrOfRooms() >= nbrRoomMin && nbrRoomMax == null ||
            property.getNbrOfRooms() >= nbrRoomMin && property.getNbrOfRooms() <= nbrRoomMax) {
            return true;
        }
        return false;
    }

    private static boolean isAfterMinDateOfEntry(Property property, Date dateMinEntry){
        if (dateMinEntry == null || property.getAddedDate().after(dateMinEntry)){
            return true;
        }
        return false;
    }

    private static boolean isAfterMinDateOfSold(Property property, Date dateMinSold){
        if (dateMinSold == null || property.getDateOfSale() != null && property.getDateOfSale().after(dateMinSold)) {
            return true;
        }
        return false;
    }

    private static boolean isInDistrict(Property property, String district){
        if (property.getAddress().getDistrict().equals(district) || district.equals("None")) {
            return true;
        }
        return false;
    }

    private static boolean isContainsFacilities(Property property, List<String> facilitiesList){
        boolean b = true;
        if (!facilitiesList.isEmpty()) {
            for (String facility : facilitiesList) {
                if (property.getFacilities().contains(facility)){
                    b = true;
                }else {
                    b = false;
                }
            }
        } else {
            b = true;
        }
        return b;
    }
}
