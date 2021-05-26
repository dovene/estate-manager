package com.openclassrooms.realestatemanager.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.Model.User;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.UI.ViewModels.UserViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;
import static com.openclassrooms.realestatemanager.UI.Fragment.Profile.ProfileFragment.CURRENT_USER_ID;

public class AuthenticationActivity extends BaseActivity {

    @BindView(R.id.name_user_editText) EditText editTextUserName;
    @BindView(R.id.password_editText) EditText editTextPassword;

    private SharedPreferences preferences;
    private ViewModelFactory modelFactory;
    private UserViewModel mUserViewModel;
    private String userName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        preferences = this.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        checkIfConnectedUser();
        modelFactory = Injection.provideViewModelFactory(this);
        mUserViewModel = new ViewModelProvider(this,modelFactory).get(UserViewModel.class);
    }

    private void checkIfConnectedUser() {
        long userId = preferences.getLong(CURRENT_USER_ID,0);
        if (userId != 0){
            startMainActivity();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * Manage on click SignIn button
     */
    @OnClick(R.id.connection_btn)
    public void connection() { configureSignIn(); }

    /**
     * Manage on click SignOut button
     * Clear SharedPrefrences
     */
    @OnClick(R.id.register_btn)
    public void register(){ configureRegister(); }

    public void configureSignIn() {
        userName = editTextUserName.getText().toString();
        password = editTextPassword.getText().toString();
        mUserViewModel.getUser(userName, password).observe(this, this::connectUser);
    }

    /**
     * After connection edit preferences user_id
     * and start mainActivity
     * @param user
     */
    private void connectUser(User user) {
        if (user != null){
            Toast.makeText(this,"Successful authentication.", Toast.LENGTH_SHORT).show();
            preferences.edit().putLong(CURRENT_USER_ID, user.getUid()).apply();
            startMainActivity();
        }else{
            Snackbar.make(getCurrentFocus(),"Authentication failed, name or password incorrect !", LENGTH_SHORT).show();
        }
    }

    public void configureRegister() {
        userName = editTextUserName.getText().toString();
        password = editTextPassword.getText().toString();
        if (userName.isEmpty() || password.isEmpty()){
            Snackbar.make(getCurrentFocus(),"Registration failed, you didn't fill all fields.", LENGTH_SHORT).show();
        } else {
            User user = new User(userName, password);
            mUserViewModel.createUser(user).observe(this, id -> getNewUserId(id));
        }
    }

    /**
     * Get New User_id
     * @param id
     */
    private void getNewUserId(Integer id) {
        mUserViewModel.getUserById(id).observe(this,this::connectNewUser);
    }

    /**
     * After Registration, edit preferences user_id and update View
     * @param user
     */
    private void connectNewUser(User user) {
        if (user != null){
            Toast.makeText(this,"Successful registration.", Toast.LENGTH_SHORT).show();
            preferences.edit().putLong(CURRENT_USER_ID, user.getUid()).apply();
            startMainActivity();
        }
    }
}
