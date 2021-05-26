package com.openclassrooms.realestatemanager.UI.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.UI.Fragment.PropertyManager.PropertyManagerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.realestatemanager.UI.Activities.DetailsPropertyActivity.PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER;

public class PropertyManagerActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_toolbar) Toolbar mToolbar;

    private PropertyManagerFragment mPropertyManagerFragment;
    private Integer mPropertyId;
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_manager);
        ButterKnife.bind(this);
        configureToolbar();
        checkIfIntentAsExtra();
        configureAndShowPropertyManagerFragment();
    }

    /**
     * Configure toolbar to manage click back from this activity.
     */
    private void configureToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * check if intent as extra to add it in bundle.
     */
    private void checkIfIntentAsExtra() {
        if (getIntent().hasExtra(PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER)){
            mPropertyId = getIntent().getIntExtra(PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER,0);
            bundle.putInt(PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER,mPropertyId);
        }
    }

    /**
     * Manage to show the PropertyManagerFragment in the frameLayout of this activity.
     * Pass the bundle in argument to get it in fragment.
     */
    private void configureAndShowPropertyManagerFragment() {
        mPropertyManagerFragment = (PropertyManagerFragment) getSupportFragmentManager().findFragmentById(R.id.property_manager_activity_frame_layout);
        if (mPropertyManagerFragment == null){
            mPropertyManagerFragment = new PropertyManagerFragment();
            mPropertyManagerFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.property_manager_activity_frame_layout,mPropertyManagerFragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
