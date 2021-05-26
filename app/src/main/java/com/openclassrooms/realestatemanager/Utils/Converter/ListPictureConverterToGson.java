package com.openclassrooms.realestatemanager.Utils.Converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Model.Picture;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ListPictureConverterToGson {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Picture> stringToPictureList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Picture>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String picturesListToString(List<Picture> pictureList) {
        return gson.toJson(pictureList);
    }
}
