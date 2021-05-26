package com.openclassrooms.realestatemanager.Utils.Converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ListConverterToGson{

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<String> stringToFacilitiesList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String facilitiesListToString(List<String> facilitiesList) {
        return gson.toJson(facilitiesList);
    }
}
