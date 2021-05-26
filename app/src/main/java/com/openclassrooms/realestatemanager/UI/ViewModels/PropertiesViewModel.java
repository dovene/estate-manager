package com.openclassrooms.realestatemanager.UI.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.Model.Property;
import com.openclassrooms.realestatemanager.Repository.PropertiesRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class PropertiesViewModel extends ViewModel {

    private PropertiesRepository mPropertiesRepository;
    private Executor mExecutor;

    private LiveData<List<Property>> propertiesList;

    public PropertiesViewModel(PropertiesRepository propertiesRepository, Executor executor) {
        mPropertiesRepository = propertiesRepository;
        mExecutor = executor;
    }

    public LiveData<List<Property>> getAllProperties(){
        if (propertiesList == null) {
            propertiesList = mPropertiesRepository.getAllProperties();
        }
        return propertiesList;
    }

    public void refreshProperties(){
        propertiesList = mPropertiesRepository.getAllProperties();
    }

    public LiveData<Property> getPropertyById(int id) { return mPropertiesRepository.getPropertyById(id); }

    public void createProperty(Property property){
        mExecutor.execute(()-> { mPropertiesRepository.createProperty(property); });
    }

    public void updateProperty(Property property) {
        mExecutor.execute(()-> mPropertiesRepository.updateProperty(property));
    }
}
