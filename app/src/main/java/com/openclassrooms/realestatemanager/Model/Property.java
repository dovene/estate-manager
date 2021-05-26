package com.openclassrooms.realestatemanager.Model;

import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.openclassrooms.realestatemanager.Utils.Converter.AddressConverter;
import com.openclassrooms.realestatemanager.Utils.Converter.DateConverter;
import com.openclassrooms.realestatemanager.Utils.Converter.ListConverterToGson;
import com.openclassrooms.realestatemanager.Utils.Converter.ListPictureConverterToGson;

import java.util.Date;
import java.util.List;

@Entity
public class Property {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private int price;
    private int surface;
    private int nbrOfRooms;
    private String description;
    private Boolean sold;
    @TypeConverters(ListConverterToGson.class)
    private List<String> facilities;
    @TypeConverters(DateConverter.class)
    private Date addedDate;
    @TypeConverters(DateConverter.class)
    private Date dateOfSale;
    @TypeConverters(AddressConverter.class)
    private Address mAddress;
    @TypeConverters(ListPictureConverterToGson.class)
    private List<Picture> mPictureList;
    private long userId;

    public Property(String type, int price, int surface, int nbrOfRooms, String description, Boolean sold, List<String> facilities, Date addedDate, Date dateOfSale, Address address, List<Picture> pictureList, long userId) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.nbrOfRooms = nbrOfRooms;
        this.description = description;
        this.sold = sold;
        this.facilities = facilities;
        this.addedDate = addedDate;
        this.dateOfSale = dateOfSale;
        this.mAddress = address;
        this.mPictureList = pictureList;
        this.userId = userId;
    }

    @Ignore
    public Property(){}

    //For Test
    @Ignore
    public Property(int id, String type, int price, int surface, int nbrOfRooms, String description, Boolean sold, List<String> facilities, Date addedDate, Date dateOfSale, Address address, List<Picture> pictureList, long userId) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.nbrOfRooms = nbrOfRooms;
        this.description = description;
        this.sold = sold;
        this.facilities = facilities;
        this.addedDate = addedDate;
        this.dateOfSale = dateOfSale;
        mAddress = address;
        mPictureList = pictureList;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public int getNbrOfRooms() {
        return nbrOfRooms;
    }

    public void setNbrOfRooms(int nbrOfRooms) {
        this.nbrOfRooms = nbrOfRooms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Date getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address address) {
        mAddress = address;
    }

    public List<Picture> getPictureList() {
        return mPictureList;
    }

    public void setPictureList(List<Picture> pictureList) {
        mPictureList = pictureList;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static Property fromContentValue(ContentValues values){
        final Property property = new Property();
        if (values.containsKey("id")) property.setId(values.getAsInteger("id"));
        if (values.containsKey("type")) property.setType(values.getAsString("type"));
        if (values.containsKey("price")) property.setPrice(values.getAsInteger("price"));
        if (values.containsKey("surface")) property.setSurface(values.getAsInteger("surface"));
        if (values.containsKey("nbrOfRooms")) property.setNbrOfRooms(values.getAsInteger("nbrOfRooms"));
        if (values.containsKey("description")) property.setDescription(values.getAsString("description"));
        if (values.containsKey("sold")) property.setSold(values.getAsBoolean("sold"));
        if (values.containsKey("facilities")){
            List<String> facilities = ListConverterToGson.stringToFacilitiesList(values.getAsString("facilities"));
            property.setFacilities(facilities);
        }
        if (values.containsKey("mAddress")) {
            Address address = AddressConverter.stringToAddress(values.getAsString("mAddress"));
            property.setAddress(address);
        }
        if (values.containsKey("mPictureList")) {
            List<Picture> pictureList = ListPictureConverterToGson.stringToPictureList(values.getAsString("mPictureList"));
            property.setPictureList(pictureList);
        }
        if (values.containsKey("addedDate")) property.setAddedDate(DateConverter.toDate(values.getAsLong("addedDate")));
        if (values.containsKey("dateOfSale")) property.setDateOfSale(DateConverter.toDate(values.getAsLong("dateOfSale")));
        if (values.containsKey("userId")) property.setUserId(values.getAsLong("userId"));
        return property;
    }
}
