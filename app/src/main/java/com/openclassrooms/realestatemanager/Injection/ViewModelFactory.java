package com.openclassrooms.realestatemanager.Injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.Repository.PropertiesRepository;
import com.openclassrooms.realestatemanager.Repository.UserRepository;
import com.openclassrooms.realestatemanager.UI.ViewModels.PropertiesViewModel;
import com.openclassrooms.realestatemanager.UI.ViewModels.UserViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PropertiesRepository mPropertiesRepository;
    private final UserRepository mUserRepository;
    private final Executor mExecutor;

    public ViewModelFactory(PropertiesRepository propertiesRepository, UserRepository userRepository, Executor executor) {
        this.mPropertiesRepository = propertiesRepository;
        this.mUserRepository = userRepository;
        this.mExecutor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertiesViewModel.class)) {
            return (T) new PropertiesViewModel(mPropertiesRepository,mExecutor);
        }
        if (modelClass.isAssignableFrom(UserViewModel.class)){
            return (T) new UserViewModel(mUserRepository,mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
