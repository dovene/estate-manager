package com.openclassrooms.realestatemanager.UI.Fragment.DetailsProperty;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.Model.Picture;
import com.openclassrooms.realestatemanager.Model.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.UI.Fragment.BaseFragment;
import com.openclassrooms.realestatemanager.UI.Fragment.PropertyManager.PicturesRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.UI.Fragment.PropertyManager.PicturesViewHolder;
import com.openclassrooms.realestatemanager.Utils.Dialog.DialogShowImage;
import com.openclassrooms.realestatemanager.Utils.StorageUtils;
import com.openclassrooms.realestatemanager.Utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.realestatemanager.UI.Fragment.PropertyManager.PropertyManagerFragment.FOLDERNAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsPropertyFragment extends BaseFragment implements OnMapReadyCallback , PicturesViewHolder.ListenerPictureClick {

    private static final String INSTANCE_STATE = "INSTANCE_STATE";
    private static final String BUNDLE_INSTANCE_STATE = "BUNDLE_INSTANCE_STATE";

    @BindView(R.id.recycler_view_pictures) RecyclerView mPicturesRecyclerView;
    @BindView(R.id.no_property_selected_details_fragment) LinearLayout mNoSelectedProperty;
    @BindView(R.id.scroll_view_details_property) ScrollView mScrollViewDetailsProperty;
    @BindView(R.id.details_description_text_view) TextView mDescription;
    @BindView(R.id.details_surface_value_text_view) TextView mSurface;
    @BindView(R.id.details_nbr_of_rooms_value_text_view) TextView mNbrOfRooms;

    @BindView(R.id.details_property_type_value_text_view) TextView mPropertyType;
    @BindView(R.id.details_price_value_text_view) TextView mPrice;
    @BindView(R.id.details_entry_date_value_text_view) TextView mDateOfEntry;
    @BindView(R.id.details_sold_date_value_text_view) TextView mDateOfSold;
    @BindView(R.id.details_agent_in_charge_value_text_view) TextView mAgentInCharge;
    @BindView(R.id.details_property_status_value_text_view) TextView mPropertyStatus;

    @BindView(R.id.linear_Layout_sold_date) LinearLayout mSoldDateLayout;

    @BindView(R.id.facilities_info_tv) TextView mFacilitiesList;
    @BindView(R.id.linear_layout_park) LinearLayout mFacilitiesPark;
    @BindView(R.id.linear_layout_school) LinearLayout mFacilitiesSchool;
    @BindView(R.id.linear_layout_station) LinearLayout mFacilitiesStation;
    @BindView(R.id.linear_layout_store) LinearLayout mFacilitiesStore;
    @BindView(R.id.details_address_street_text_view) TextView mAddressStreet;
    @BindView(R.id.details_address_complement_street_text_view) TextView mAddressStreetComplement;
    @BindView(R.id.details_address_district_text_view) TextView mAddressDistrict;
    @BindView(R.id.details_address_state_and_postcode_text_view) TextView mAddressStateAndPostCode;
    @BindView(R.id.details_address_country_text_view) TextView mAddressCountry;
    @BindView(R.id.mapView) MapView mMapView;

    private GoogleMap mGoogleMap;
    private Integer mPropertyId;
    private PicturesRecyclerViewAdapter mAdapter;
    private Property property;

    //CONSTRUCTOR
    public DetailsPropertyFragment(){}

    /**
     * Constructor with parameter to get propertyId
     * @param propertyId
     */
    public DetailsPropertyFragment(int propertyId) {
        this.mPropertyId = propertyId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_property, container, false);
        ButterKnife.bind(this,view);
        configureViewModels(getContext());
        checkIfInstanceState(savedInstanceState);
        configureRecyclerView();
        checkIfPropertyId();
        mMapView.onCreate(savedInstanceState);
        return view;
    }

    /**
     * check if savedInstanceState exist then get the propertyId saved
     * @param savedInstanceState
     */
    private void checkIfInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            if (savedInstanceState.getBundle(BUNDLE_INSTANCE_STATE) != null) {
                mPropertyId = savedInstanceState.getBundle(BUNDLE_INSTANCE_STATE).getInt(INSTANCE_STATE);
            }
        }
    }

    /**
     * configure recyclerView horizontally to display pictures
     */
    private void configureRecyclerView() {
        mAdapter = new PicturesRecyclerViewAdapter(getContext(),this);
        mPicturesRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mPicturesRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * if propertyId exit manage Layout to show
     * then get Property by this id.
     */
    private void checkIfPropertyId() {
        if (mPropertyId != null) {
            mScrollViewDetailsProperty.setVisibility(View.VISIBLE);
            mNoSelectedProperty.setVisibility(View.GONE);
            mPropertiesViewModel.getPropertyById(mPropertyId).observe(getActivity(),this::updateProperty);
        }else{
            mScrollViewDetailsProperty.setVisibility(View.GONE);
            mNoSelectedProperty.setVisibility(View.VISIBLE);
        }
    }

    /**
     * On received property from viewModel
     * display information in the fragment.
     * @param property
     */
    private void updateProperty(Property property) {
        this.property = property;
        if (property != null){
            mUserViewModel.getUserById(property.getUserId()).observe(this,user -> {
                if (user!=null) {
                    mAgentInCharge.setText(user.getName());
                }
            });
            //Update pictures of this property in recyclerView
            mAdapter.updateListPictures(property.getPictureList());
            //setUp information
            mDateOfEntry.setText(Utils.formatDate(property.getAddedDate()));
            if (property.getSold()){
                mPropertyStatus.setText("Sold Out");
                mPropertyStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
                mSoldDateLayout.setVisibility(View.VISIBLE);
                mDateOfSold.setText(Utils.formatDate(property.getDateOfSale()));
            }else {
                mPropertyStatus.setText("Available");
                mSoldDateLayout.setVisibility(View.GONE);
                mPropertyStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.green));
            }
            mPrice.setText("$"+ String.format("%,d", property.getPrice()));
            mPropertyType.setText(property.getType());
            mDescription.setText(property.getDescription());
            mSurface.setText(String.valueOf(property.getSurface()));
            mNbrOfRooms.setText(String.valueOf(property.getNbrOfRooms()));
            mAddressStreet.setText(property.getAddress().getStreet());
            if (property.getAddress().getComplement_street() != null) {
                mAddressStreetComplement.setText(property.getAddress().getComplement_street());
            }else {
                mAddressStreetComplement.setVisibility(View.GONE);
            }
            mAddressDistrict.setText(property.getAddress().getDistrict());
            mAddressStateAndPostCode.setText(property.getAddress().getState()+" "+ property.getAddress().getPostCode());
            mAddressCountry.setText(property.getAddress().getCountry());
            mMapView.getMapAsync(this);
        }

        List<String> facilities = property.getFacilities();
        showingListOfFacilities(facilities);
    }

    /**
     * Display facilities on view
     * @param facilities
     */
    private void showingListOfFacilities(List<String> facilities) {
        if (!facilities.isEmpty()){
            mFacilitiesList.setVisibility(View.VISIBLE);
            for (String facility: facilities) {
                switch (facility){
                    case "School":
                        mFacilitiesSchool.setVisibility(View.VISIBLE);
                        break;
                    case "Store":
                        mFacilitiesStore.setVisibility(View.VISIBLE);
                        break;
                    case "Station":
                        mFacilitiesStation.setVisibility(View.VISIBLE);
                        break;
                    case "Park":
                        mFacilitiesPark.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    /**
     * hiding property on view to display correctly on get property.
     */
    @Override
    public void onResume() {
        super.onResume();
        mFacilitiesList.setVisibility(View.GONE);
        mFacilitiesSchool.setVisibility(View.GONE);
        mFacilitiesStore.setVisibility(View.GONE);
        mFacilitiesStation.setVisibility(View.GONE);
        mFacilitiesPark.setVisibility(View.GONE);
    }

    /**
     * When map is ready got the address of property to show it with marker.
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        String formattedAddress = property.getAddress().getFormattedAddress();
        LatLng position = Utils.getLocationFromAddress(getContext(), formattedAddress);
        if (position != null){
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(position)
            );
        }
    }

    /**
     * Save the propertyId to get it back on configurationChanged
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mPropertyId != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(BUNDLE_INSTANCE_STATE,mPropertyId);
            outState.putBundle(INSTANCE_STATE, bundle);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * On click of a picture get it in storage, then display in Dialog box.
     * @param picture
     */
    @Override
    public void onClickPicture(Picture picture) {
        Bitmap bitmap = StorageUtils.getBitmapFromStorage(getActivity().getFilesDir(),getContext(),picture.getFile(),FOLDERNAME);
        DialogShowImage dialog = new DialogShowImage(bitmap);
        dialog.setTargetFragment(this,1);
        dialog.show(getParentFragmentManager(),"DialogImagePreview");
    }
}
