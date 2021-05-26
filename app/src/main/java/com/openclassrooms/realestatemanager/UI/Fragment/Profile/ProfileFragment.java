package com.openclassrooms.realestatemanager.UI.Fragment.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.Model.User;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.UI.Fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;
import static com.openclassrooms.realestatemanager.UI.Activities.BaseActivity.PREFERENCES_NAME;

public class ProfileFragment extends BaseFragment{

    @BindView(R.id.picture_user_iv) ImageView mUserImageView;
    @BindView(R.id.user_name_tv) TextView mUserTextView;
    @BindView(R.id.logout_btn) Button logOutBtn;

    public static String CURRENT_USER_ID = "CURRENT_USER_ID";

    private User mCurrentUser;
    private SharedPreferences preferences;

    //CONSTRUCTOR
    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,view);
        preferences = getActivity().getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
        configureViewModels(getContext());
        getUserIdFromPreferences();
        return view;
    }

    /**
     * get user_id saved in SharedPreferences
     * then get user with viewModel
     */
    public void getUserIdFromPreferences(){
        long userId = preferences.getLong(CURRENT_USER_ID,0);
        mUserViewModel.getUserById(userId).observe(getActivity(),user -> {
            mCurrentUser = user;
            updateView();
        });
    }

    /**
     * Update view with userInformation
     */
    private void updateView() {
        if (mCurrentUser != null) {
            mUserTextView.setText(mCurrentUser.getName());
        }
    }

    /**
     * Manage on click SignOut button
     * Clear SharedPrefrences
     */
    @OnClick(R.id.logout_btn)
    public void logout(){
        preferences.edit().clear().apply();
        getActivity().finish();
        Toast.makeText(getContext(),"You've been disconnected.",Toast.LENGTH_SHORT).show();
    }
}
