package com.openclassrooms.realestatemanager.UI.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.Model.User;
import com.openclassrooms.realestatemanager.Repository.UserRepository;

import java.util.concurrent.Executor;

public class UserViewModel extends ViewModel {

    private final UserRepository mUserRepository;
    private final Executor mExecutor;

    public UserViewModel(UserRepository userRepository, Executor executor) {
        mUserRepository = userRepository;
        mExecutor = executor;
    }

    public LiveData<User> getUser(String name, String password){
         return mUserRepository.getUser(name,password);
    }

    public LiveData<User> getUserById(long id){
        return mUserRepository.getUser(id);
    }

    public MutableLiveData<Integer> createUser(User user){
        MutableLiveData<Integer> idCreatedUser = new MutableLiveData<>();
        mExecutor.execute(()-> {
            long idReturn = mUserRepository.createUser(user);
            idCreatedUser.postValue((int) idReturn);
        });
        return idCreatedUser;
    }
}
