package com.openclassrooms.realestatemanager.UI.Fragment.ListProperties;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.Model.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.UI.Activities.DetailsPropertyActivity;
import com.openclassrooms.realestatemanager.UI.Fragment.BaseFragment;
import com.openclassrooms.realestatemanager.UI.Fragment.DetailsProperty.DetailsPropertyFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.openclassrooms.realestatemanager.UI.Activities.DetailsPropertyActivity.PROPERTY_ID_EXTRA;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListPropertiesFragment extends BaseFragment implements PropertiesViewHolder.OnPropertyListener {

    @BindView(R.id.list_properties_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.no_property_found_ll) LinearLayout mLayoutNoPropertyFound;
    @BindView(R.id.revert_search_fab) FloatingActionButton mRevertButton;

    //Interface
    public interface sendPropertyIdToMainActivityOnClickListener{
        void callbackPropertyId(int propertyId);
    }

    private sendPropertyIdToMainActivityOnClickListener mListener;

    private PropertiesRecyclerViewAdapter mAdapter;
    private boolean twoPanes;
    private boolean mFilterOn;
    private List<Property> mPropertyList;

    //CONSTRUCTOR
    public ListPropertiesFragment(){}

    /**
     * Constructor with parameter for search
     * Called from SerachFragment
     * @param propertyList
     * @param filterOn
     */
    public ListPropertiesFragment(List<Property> propertyList, boolean filterOn, boolean twoPanes, sendPropertyIdToMainActivityOnClickListener listener) {
        this.mPropertyList = propertyList;
        this.mFilterOn = filterOn;
        this.twoPanes = twoPanes;
        this.mListener = listener;
    }

    /**
     * Constructor with parameters
     * @param twoPanes
     * @param listener
     */
    public ListPropertiesFragment(boolean twoPanes,sendPropertyIdToMainActivityOnClickListener listener) {
        this.twoPanes = twoPanes;
        this.mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);
        configureViewModels(getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFilterOn){
            mRevertButton.setVisibility(View.VISIBLE);
        }else {
            mRevertButton.setVisibility(View.GONE);
        }
        if (mPropertyList == null || !mFilterOn) {
            //get all property from viewModel
            mPropertiesViewModel.getAllProperties().observe(getViewLifecycleOwner(), this::configureRecyclerView);
        }else {
            checkIfPropertyListIsEmptyAndUpdateView(mPropertyList);
            configureRecyclerView(mPropertyList);
        }
    }

    @OnClick(R.id.revert_search_fab)
    public void refreshView(){
        mFilterOn = false;
        onResume();
    }

    /**
     * On received list from viewModel, pass it to recyclerView
     * @param properties
     */
    private void configureRecyclerView(List<Property> properties) {
        checkIfPropertyListIsEmptyAndUpdateView(properties);
        mAdapter = new PropertiesRecyclerViewAdapter(properties,this,getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    /**
     * Check if a list is empty and update view by showing/hiding layout
     * @param propertyList
     */
    public void checkIfPropertyListIsEmptyAndUpdateView(List<Property> propertyList){
        if (propertyList == null || propertyList.isEmpty()){
            mLayoutNoPropertyFound.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else {
            mLayoutNoPropertyFound.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Get onclick property interface from view holder, received propertyId
     * Pass this propertyId in DetailsFragment or DetailsActivity depends on twoPanes mode.
     * @param propertyId
     */
    @Override
    public void onPropertyClick(int propertyId) {
        if (twoPanes){
            mListener.callbackPropertyId(propertyId);
            FragmentManager fragmentManager =  getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.details_activity_frame_layout,new DetailsPropertyFragment(propertyId))
                    .commit();
        }else{
            Intent intent = new Intent(getContext(), DetailsPropertyActivity.class);
            intent.putExtra(PROPERTY_ID_EXTRA, propertyId);
            startActivity(intent);
        }
        mAdapter.updateBackgroundColor(propertyId);
    }

    /**
     * If configuration changed update the twoPanes mode
     * @param twoPanes
     * @param listener
     */
    public void updateTwoPanesAndListenerToFragment(boolean twoPanes, sendPropertyIdToMainActivityOnClickListener listener) {
        this.twoPanes = twoPanes;
        this.mListener = listener;
    }
}
