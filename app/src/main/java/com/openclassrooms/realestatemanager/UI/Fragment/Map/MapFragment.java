package com.openclassrooms.realestatemanager.UI.Fragment.Map;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.openclassrooms.realestatemanager.Model.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.UI.Activities.DetailsPropertyActivity;
import com.openclassrooms.realestatemanager.UI.Fragment.BaseFragment;
import com.openclassrooms.realestatemanager.Utils.Utils;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.openclassrooms.realestatemanager.UI.Activities.DetailsPropertyActivity.PROPERTY_ID_EXTRA;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, EasyPermissions.PermissionCallbacks {

    private static final String PERMS = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int RC_LOCATION_PERMS = 100;

    private View mView;
    private FusedLocationProviderClient fusedLocationClient;
    private Location mCurrentUserLocation;
    private GoogleMap mMap;
    private List<Property> mListProperties;

    //CONSTRUCTOR
    public MapFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        configureViewModels(getActivity());
        checkPermission();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPropertiesViewModel.getAllProperties().observe(getActivity(),this::getAllProperties);
    }

    /**
     * get list of all properties
     * @param fullProperties
     */
    private void getAllProperties(List<Property> fullProperties) {
        mListProperties = fullProperties;
    }

    /**
     * Check if permission to access location are granted
     * then get map fragment and move camera to user location
     */
    @AfterPermissionGranted(RC_LOCATION_PERMS)
    public void checkPermission() {
        if (!EasyPermissions.hasPermissions(getActivity(),PERMS)) {
            EasyPermissions.requestPermissions(this, "We need your permission to access to your location.", RC_LOCATION_PERMS, PERMS);
            return;
        }else {
            getLastLocationOfUser();
        }
    }

    private void getLastLocationOfUser() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mCurrentUserLocation = location;
                configureMapFragment();
            }
        });
    }

    /**
     * get Google map fragment
     */
    public void configureMapFragment(){
        if (mMap != null) {
            return;
        }else {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
        movePositionOnMap();
        putMarkersOnMap();
    }

    /**
     * Move map camera to user location
     */
    private void movePositionOnMap() {
        if (mCurrentUserLocation != null) {
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mCurrentUserLocation.getLatitude(),
                    mCurrentUserLocation.getLongitude()), 12));
        }
    }

    /**
     * Put markers on map foreach properties
     */
    private void putMarkersOnMap() {
        if (!mListProperties.isEmpty()){
            for (Property property: mListProperties) {
                LatLng latLng = Utils.getLocationFromAddress(getContext(), property.getAddress().getFormattedAddress());
                if (latLng != null) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                    marker.setTag(property.getId());
                }
            }
        }
    }

    /**
     * Manage click on marker to start Details Activity
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        int propertyId = (int) marker.getTag();
        Intent intent = new Intent(getContext(), DetailsPropertyActivity.class);
        intent.putExtra(PROPERTY_ID_EXTRA, propertyId);
        startActivity(intent);
        return false;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e("TAG", "onPermissionsGranted: "+requestCode );
        if (requestCode == RC_LOCATION_PERMS){
            getLastLocationOfUser();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    }

    /**
     * Get the result of permission with EasyPermissions dependency.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
}
