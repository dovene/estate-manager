package com.openclassrooms.realestatemanager.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.Model.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.UI.Fragment.DetailsProperty.DetailsPropertyFragment;
import com.openclassrooms.realestatemanager.UI.Fragment.ListProperties.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.UI.Fragment.LoanSimulator.LoanSimulatorFragment;
import com.openclassrooms.realestatemanager.UI.Fragment.Map.MapFragment;
import com.openclassrooms.realestatemanager.UI.Fragment.Profile.ProfileFragment;
import com.openclassrooms.realestatemanager.UI.Fragment.Search.SearchFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.realestatemanager.UI.Activities.DetailsPropertyActivity.PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ListPropertiesFragment.sendPropertyIdToMainActivityOnClickListener,
    SearchFragment.GetPropertiesListFromSearchListeners {

    @BindView(R.id.activity_main_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_host_frame_layout) FrameLayout mHostFrameLayout;
    @BindView(R.id.activity_main_navigation_view) NavigationView mNavigationView;

    private ListPropertiesFragment mListPropertiesFragment;
    private DetailsPropertyFragment mDetailsPropertyFragment;
    private SearchFragment mSearchFragment;
    private Integer mPropertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configureToolbar();
        configureDrawer();
        checkIfTwoPanes();
        configureAndShowHostFragment();
        configureAndShowDetailsFragment();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Configure toolbar and manage the hamburger icon for navigation drawer
     */
    private void configureToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * If the frame layout for details Activity is display, the twoPane mode is active.
     */
    private void checkIfTwoPanes() {
        if (findViewById(R.id.details_activity_frame_layout) != null) twoPanes = true;
    }

    /**
     * Configure navigationDrawer and mange click on hamburger icon
     */
    private void configureDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_layout_open,R.string.drawer_layout_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.primaryTextColor));
        toggle.syncState();
    }

    /**
     * Manage host layout to show ListPropertyFragment
     * If fragment already created update TwoPanes.
     */
    private void configureAndShowHostFragment() {
        Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.activity_main_host_frame_layout);
        if (fragmentInFrame == null) {
            mListPropertiesFragment = new ListPropertiesFragment(twoPanes, this);
            startTransactionFragment(mListPropertiesFragment);
        }else if (fragmentInFrame instanceof ListPropertiesFragment){
            mListPropertiesFragment = (ListPropertiesFragment) fragmentInFrame;
            mListPropertiesFragment.updateTwoPanesAndListenerToFragment(twoPanes,this);
            showDetailsFrameLayout(true);
        }else if (fragmentInFrame instanceof ProfileFragment || fragmentInFrame instanceof LoanSimulatorFragment
        || fragmentInFrame instanceof MapFragment){
            showDetailsFrameLayout(false);
        }else if (fragmentInFrame instanceof SearchFragment){
            mSearchFragment = (SearchFragment) fragmentInFrame;
            mSearchFragment.updateListener(this);
            showDetailsFrameLayout(false);
        }
    }

    /**
     * Display the DetailsPropertyFragment on Two panes mode.
     */
    private void configureAndShowDetailsFragment() {
        mDetailsPropertyFragment = (DetailsPropertyFragment) getSupportFragmentManager().findFragmentById(R.id.details_activity_frame_layout);
        if (mDetailsPropertyFragment == null && findViewById(R.id.details_activity_frame_layout) != null){
            mDetailsPropertyFragment = new DetailsPropertyFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_activity_frame_layout,mDetailsPropertyFragment)
                    .commit();
        }
    }

    /**
     * Manage click on navigationView Items
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.activity_main_drawer_list:
                    if (mListPropertiesFragment == null) mListPropertiesFragment = new ListPropertiesFragment(twoPanes,this);
                    this.startTransactionFragment(mListPropertiesFragment);
                    showDetailsFrameLayout(true);
                break;
            case R.id.activity_main_drawer_map:
                    MapFragment mapFragment = new MapFragment();
                    this.startTransactionFragment(mapFragment);
                    showDetailsFrameLayout(false);
                break;
            case R.id.activity_main_drawer_profile:
                ProfileFragment profileFragment = new ProfileFragment();
                this.startTransactionFragment(profileFragment);
                showDetailsFrameLayout(false);
                break;
            case R.id.activity_main_drawer_loan_simulator:
                LoanSimulatorFragment loanSimulatorFragment = new LoanSimulatorFragment();
                this.startTransactionFragment(loanSimulatorFragment);
                showDetailsFrameLayout(false);
                break;
            default:
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Manage click back for navigationDrawer
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    /**
     * Replace the host fragment by the parameter fragment
     * @param fragment
     */
    public void startTransactionFragment(Fragment fragment){
        if(!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_host_frame_layout,fragment)
                    .commit();
        }
    }

    /**
     * Manage click on Toolbar Item selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_add_item:
                    mPropertyId = null;
                    startPropertyManagerActivity();
                break;
            case R.id.app_bar_edit_item:
                    if (mPropertyId != null){
                        startPropertyManagerActivity();
                    }else{
                        Snackbar.make(findViewById(android.R.id.content),"You have to select an item to update it!",Snackbar.LENGTH_SHORT).show();
                    }
                break;
            case R.id.app_bar_search_item:
                SearchFragment searchFragment = new SearchFragment(this);
                this.startTransactionFragment(searchFragment);
                showDetailsFrameLayout(false);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Start PropertyManagerActivity
     */
    public void startPropertyManagerActivity(){
        Intent intent = new Intent(this,PropertyManagerActivity.class);
        if (mPropertyId != null) {
            intent.putExtra(PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER, mPropertyId);
        }
        startActivity(intent);
    }

    /**
     * Display Different menu on this activity if the twoPanes mode is active.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (twoPanes) {
            getMenuInflater().inflate(R.menu.activity_main_menu_two_panes_toolbar, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_activity_menu_toolbar, menu);
        }
        return true;
    }

    /**
     * Get propertyId from ListPropertyFragment Listener
     * To give right propertyId in intent when start ManagePropertyActivity
     * @param propertyId
     */
    @Override
    public void callbackPropertyId(int propertyId) {
        this.mPropertyId = propertyId;
    }

    /**
     * Showing details frame layout or not depends on screen rotation and item clicked
     * @param b
     */
    private void showDetailsFrameLayout(boolean b) {
        if (b) {
            if (findViewById(R.id.details_activity_frame_layout) != null) {
                FrameLayout detailsLayout = findViewById(R.id.details_activity_frame_layout);
                detailsLayout.setVisibility(View.VISIBLE);
            }
        }else {
            if (findViewById(R.id.details_activity_frame_layout) != null) {
                FrameLayout detailsLayout = findViewById(R.id.details_activity_frame_layout);
                detailsLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void getPropertiesListFromSearch(List<Property> propertyList) {
        mListPropertiesFragment = new ListPropertiesFragment(propertyList,true,twoPanes,this);
        this.startTransactionFragment(mListPropertiesFragment);
        showDetailsFrameLayout(true);
    }
}
