package com.openclassrooms.realestatemanager.Utils.Converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Model.Address;

import java.lang.reflect.Type;

public class AddressConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static Address stringToAddress(String data) {

        Type addressType = new TypeToken<Address>() {}.getType();
        return gson.fromJson(data, addressType);
    }

    @TypeConverter
    public static String addressToString(Address address) {
        return gson.toJson(address);
    }
}
