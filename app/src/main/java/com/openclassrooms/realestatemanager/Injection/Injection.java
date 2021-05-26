package com.openclassrooms.realestatemanager.Injection;

import android.content.Context;

import com.openclassrooms.realestatemanager.Database.RealEstateManagerDataBase;
import com.openclassrooms.realestatemanager.Repository.PropertiesRepository;
import com.openclassrooms.realestatemanager.Repository.UserRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static PropertiesRepository providePropertyDataSource(Context context){
        RealEstateManagerDataBase dataBase = RealEstateManagerDataBase.getInstance(context);
        return new PropertiesRepository(dataBase.mPropertyDAO());
    }

    public static UserRepository provideUserDataSource(Context context){
        RealEstateManagerDataBase dataBase = RealEstateManagerDataBase.getInstance(context);
        return new UserRepository(dataBase.mUserDAO());
    }

    public static Executor provideExecutor(){
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context){
        PropertiesRepository propertiesRepository = providePropertyDataSource(context);
        UserRepository userRepository = provideUserDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(propertiesRepository,userRepository, executor);
    }
}
