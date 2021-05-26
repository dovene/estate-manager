package com.openclassrooms.realestatemanager.UI.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.UI.Fragment.DetailsProperty.DetailsPropertyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsPropertyActivity extends BaseActivity {

    @BindView(R.id.activity_main_toolbar) Toolbar mToolbar;

    public static String PROPERTY_ID_EXTRA = "PROPERTY_ID_EXTRA";
    public static String PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER = "PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER";

    private DetailsPropertyFragment mDetailsPropertyFragment;
    private Integer mPropertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_property);
        ButterKnife.bind(this);
        configureToolbar();
        checkIfIntentAsExtra();
        configureAndShowDetailsFragment();
    }

    /**
     * configure toolbar to manage click back from this activity.
     */
    private void configureToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * if intent as Extra update the propertyId with value of the intent.
     */
    private void checkIfIntentAsExtra() {
        if (getIntent().hasExtra(PROPERTY_ID_EXTRA)){
            mPropertyId = getIntent().getIntExtra(PROPERTY_ID_EXTRA,0);
        }
    }

    /**
     * Manage to show the Details Fragment in the frameLayout of this activity
     * Pass the propertyId in parameter to get the property in fragment
     */
    private void configureAndShowDetailsFragment() {
        mDetailsPropertyFragment = (DetailsPropertyFragment) getSupportFragmentManager().findFragmentById(R.id.details_activity_frame_layout);
        if (mDetailsPropertyFragment == null){
            mDetailsPropertyFragment = new DetailsPropertyFragment(mPropertyId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_activity_frame_layout,mDetailsPropertyFragment)
                    .commit();
        }
    }

    /**
     * Display Different menu on this activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.details_activity_menu_toolbar, menu);
        return true;
    }

    /**
     * Manage the click on item in the menu of this activity.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //OnClick of edit item put the propertyId in Extra of the intent then start PropertyManagerActivity.
            case R.id.app_bar_edit_item:
                Intent intent = new Intent(this,PropertyManagerActivity.class);
                intent.putExtra(PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER,mPropertyId);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
