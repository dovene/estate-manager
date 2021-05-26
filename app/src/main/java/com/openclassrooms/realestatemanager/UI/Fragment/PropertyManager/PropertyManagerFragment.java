package com.openclassrooms.realestatemanager.UI.Fragment.PropertyManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.Dummy.Dummy;
import com.openclassrooms.realestatemanager.Model.Address;
import com.openclassrooms.realestatemanager.Model.Picture;
import com.openclassrooms.realestatemanager.Model.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.UI.Fragment.BaseFragment;
import com.openclassrooms.realestatemanager.Utils.Dialog.DialogDeleteImage;
import com.openclassrooms.realestatemanager.Utils.Dialog.DialogEntryDatePicker;
import com.openclassrooms.realestatemanager.Utils.Dialog.DialogImagePreview;
import com.openclassrooms.realestatemanager.Utils.Dialog.DialogSoldDatePiker;
import com.openclassrooms.realestatemanager.Utils.NotificationService;
import com.openclassrooms.realestatemanager.Utils.StorageUtils;
import com.openclassrooms.realestatemanager.Utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.openclassrooms.realestatemanager.UI.Activities.BaseActivity.PREFERENCES_NAME;
import static com.openclassrooms.realestatemanager.UI.Activities.DetailsPropertyActivity.PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER;
import static com.openclassrooms.realestatemanager.UI.Fragment.Profile.ProfileFragment.CURRENT_USER_ID;

//TODO check the update method.

/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyManagerFragment extends BaseFragment implements DialogImagePreview.DialogImagePreviewListener, PicturesViewHolder.ListenerPictureClick ,
    DialogDeleteImage.DialogDeleteListener , EasyPermissions.PermissionCallbacks, DialogEntryDatePicker.DialogEntryDatePickerListener,
    DialogSoldDatePiker.DialogSoldDatePickerListener {

    @BindView(R.id.recycler_view_pictures) RecyclerView mPicturesRecyclerView;
    @BindView(R.id.add_picture_fab) FloatingActionButton mAddPictureFAB;
    @BindView(R.id.take_picture_fab) FloatingActionButton mTakePictureFAB;
    @BindView(R.id.manager_layout_type_spinner) Spinner mPropertyTypeSpinner;
    @BindView(R.id.manager_layout_price_editText) EditText mPropertyPrice;
    @BindView(R.id.manager_layout_description_editText) EditText mPropertyDescription;
    @BindView(R.id.manager_layout_surface_editText) EditText mPropertySurface;
    @BindView(R.id.manager_layout_nbr_of_room_editText) EditText mPropertyNbrOfRooms;
    @BindView(R.id.manager_layout_address_nbr_of_street_editText) EditText mPropertyAddressStreetNumber;
    @BindView(R.id.manager_layout_address_street_editText) EditText mPropertyAddressStreet;
    @BindView(R.id.manager_layout_address_district_editText) EditText mPropertyAddressDistrict;
    @BindView(R.id.manager_layout_address_state_editText) EditText mPropertyAddressState;
    @BindView(R.id.manager_layout_address_post_code_editText) EditText mPropertyAddressPostCode;
    @BindView(R.id.manager_layout_address_country_editText) EditText mPropertyAddressCountry;
    @BindView(R.id.manager_layout_btn_add_property) Button mAddPropertyButton;
    @BindView(R.id.sold_date_selector_btn) Button mSoldDateSelector;
    @BindView(R.id.entry_date_text_view) TextView mEntryDateTv;
    @BindView(R.id.checkBox_park) CheckBox mCheckboxPark;
    @BindView(R.id.checkBox_school) CheckBox mCheckboxSchool;
    @BindView(R.id.checkBox_station) CheckBox mCheckboxStation;
    @BindView(R.id.checkBox_store) CheckBox mCheckboxStore;
    @BindView(R.id.checkBox_is_sold) CheckBox mCheckboxIsSold;

    //If PropertyId get FullProperty
    private Integer mPropertyId;
    private Property mProperty;

    //Values of editText
    private String mType;
    private Integer mPrice;
    private String mDescription;
    private Integer mSurface;
    private Integer mNbrOfRoom;
    private Integer mAddressStreetNbr;
    private String mAddressStreet;
    private String mAddressDistrict;
    private String mAddressState;
    private Integer mAddressPostCode;
    private String mAddressCountry;

    //Folder to Stock image on internalStorage
    public static final String FOLDERNAME = "RealEstateManager_images";
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    //For permission
    static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int RC_IMAGE_PERMS = 200;
    private static final int RC_CHOOSE_PHOTO = 300;
    //For data
    private PicturesRecyclerViewAdapter mAdapter;
    private Bitmap bitmap;
    private List<Picture> mListPictures = new ArrayList<>();
    private Picture pictureToDelete;
    private List<String> mListFacilities = new ArrayList<>();
    private long userId;
    private Boolean mIsSold = false;

    private Date dateOfEntry;
    private Date dateOfSale;

    //CONSTRUCTOR
    public PropertyManagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_manager, container, false);
        ButterKnife.bind(this,view);
        configureViewModels(getContext());
        getCurrentUser();
        configureRecyclerView();
        checkIfArguments();
        configurePropertyTypeSpinner();
        setAddPictureClickListener();
        setAddPropertyButtonOnclickListener();
        onSelectedSoldCheckbox();
        return view;
    }

    private void getCurrentUser() {
        userId = getActivity().getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE).getLong(CURRENT_USER_ID,0);
    }

    private void configureRecyclerView() {
        mAdapter = new PicturesRecyclerViewAdapter(getContext(),this);
        mPicturesRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mPicturesRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Check if the Fragment received arguments
     * Get The PropertyById and populate editText
     */
    private void checkIfArguments() {
        Bundle args = getArguments();
        if (!args.isEmpty()){
            mPropertyId = getArguments().getInt(PROPERTY_ID_EXTRA_FOR_PROPERTY_MANAGER);
            if (mPropertyId != null){
                mPropertiesViewModel.getPropertyById(mPropertyId).observe(getActivity(),this::populateEditItemWithPropertyValues);
            }
        }
    }

    /**
     * Populate all Edit text with value of Property
     * @param property
     */
    private void populateEditItemWithPropertyValues(Property property) {
        this.mProperty = property;

        //populate adapter with pictures
        mListPictures = property.getPictureList();
        mAdapter.updateListPictures(mListPictures);

        mPropertyTypeSpinner.setSelection(Dummy.propertyType.indexOf(property.getType()));
        mPropertyPrice.setText(String.valueOf(property.getPrice()));
        mPropertyDescription.setText(property.getDescription());
        mPropertySurface.setText(String.valueOf(property.getSurface()));
        mPropertyNbrOfRooms.setText(String.valueOf(property.getNbrOfRooms()));

        List<String> facilities = mProperty.getFacilities();
        if (facilities != null){
            for (String facility: facilities) {
                switch (facility){
                    case "School":
                        mCheckboxSchool.setChecked(true);
                    break;
                    case "Store":
                        mCheckboxStore.setChecked(true);
                    break;
                    case "Station":
                        mCheckboxStation.setChecked(true);
                        break;
                    case "Park":
                        mCheckboxPark.setChecked(true);
                        break;
                }
            }
        }

        mPropertyAddressStreetNumber.setText(String.valueOf(mProperty.getAddress().getNumber()));
        mPropertyAddressStreet.setText(mProperty.getAddress().getStreet());
        mPropertyAddressDistrict.setText(mProperty.getAddress().getDistrict());
        mPropertyAddressState.setText(mProperty.getAddress().getState());
        mPropertyAddressPostCode.setText(String.valueOf(mProperty.getAddress().getPostCode()));
        mPropertyAddressCountry.setText(mProperty.getAddress().getCountry());

        if (mProperty.getAddedDate() != null){
            dateOfEntry = mProperty.getAddedDate();
            mEntryDateTv.setText(Utils.formatDate(mProperty.getAddedDate()));
        }

        if (mProperty.getSold()){
            mCheckboxIsSold.setChecked(true);
            dateOfSale = mProperty.getDateOfSale();
            mSoldDateSelector.setText(Utils.formatDate(mProperty.getDateOfSale()));
            mSoldDateSelector.setVisibility(View.VISIBLE);
        }else {
            mSoldDateSelector.setVisibility(View.GONE);
        }
        mAddPropertyButton.setText("Update Property");
    }

    /**
     * Populate Spinner to choose type of Property From List
     */
    private void configurePropertyTypeSpinner() {
        mPropertyTypeSpinner.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item, Dummy.propertyType));
        mPropertyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mType = (String) mPropertyTypeSpinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mType = String.valueOf(mPropertyTypeSpinner.getItemIdAtPosition(0));
            }
        });
    }

    /**
     * Manage Click on add picture Button
     */
    private void setAddPictureClickListener() {
        mAddPictureFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchChoosePictureFromAlbum();
            }
        });
        mTakePictureFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    /**
     * Check if we have permission to have access to pictures
     * Start Activity to pick up one picture from the phone
     */
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void dispatchChoosePictureFromAlbum() {
        if (!EasyPermissions.hasPermissions(getContext(), PERMS)) {
            EasyPermissions.requestPermissions(this, "We need permission to access to your pictures", RC_IMAGE_PERMS, PERMS);
            return;
        }
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    /**
     * On Result success to get Picture from phone
     * show bitmap file in Dialog preview
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri uriImageSelected = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriImageSelected);
                    showDialogImagePreview(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            showDialogImagePreview(bitmap);
        }
    }

    /**
     * Showing DialogPreview with a bitmap
     * @param bitmap
     */
    private void showDialogImagePreview(Bitmap bitmap) {
        DialogImagePreview dialog = new DialogImagePreview(bitmap);
        dialog.setTargetFragment(this,1);
        dialog.show(getParentFragmentManager(),"DialogImagePreview");
    }

    /**
     * Manage Click on Save button of the DialogPreview
     * Get EditText value , generate file name of picture
     * add new picture to list
     * Display pictures in RecyclerView
     * Then save picture on storage
     * @param dialogImagePreview
     */
    @Override
    public void onDialogImagePreviewSave(DialogImagePreview dialogImagePreview) {
        EditText imageDescription = dialogImagePreview.getDialog().findViewById(R.id.dialog_picture_description_et);
        String description = imageDescription.getText().toString();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String imageName = "image_"+ n +".jpg";
        Picture picture = new Picture(imageName,description);
        mListPictures.add(picture);
        mAdapter.updateListPictures(mListPictures);
        StorageUtils.setBitmapInStorage(getActivity().getFilesDir(),getContext(),imageName,FOLDERNAME,bitmap);
    }

    private void onSelectedSoldCheckbox() {
        mCheckboxIsSold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mSoldDateSelector.setVisibility(View.VISIBLE);
                }else {
                    mSoldDateSelector.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick(R.id.entry_date_selector_btn)
    public void showDialogDatePicker(){
        showDatePikerDialog();
    }

    @OnClick(R.id.sold_date_selector_btn)
    public void showDialogSoldDatePicker(){
        showSoldDatePikerDialog();
    }

    /**
     * Manage click on add/update property
     */
    private void setAddPropertyButtonOnclickListener() {
        mAddPropertyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValuesOfInput()){
                    if (mProperty != null){
                        setValuesOfPropertyAndUpdate();
                    }else {
                        createNewPropertyAndAddress();
                    }
                }
            }
        });
    }

    /**
     * Check if the values of input are not null
     * @return
     */
    private Boolean checkValuesOfInput() {
        Boolean values = true;
        if (mListPictures.isEmpty()){
            Snackbar.make(getView(),"You must add at least one image.",Snackbar.LENGTH_LONG).show();
            values = false;
        }
        if (!mPropertyPrice.getText().toString().isEmpty()){
            mPrice = Integer.valueOf(mPropertyPrice.getText().toString());
        }else{
            mPropertyPrice.setError("You must add a price.");
            values = false;
        }
        if (!mPropertyDescription.getText().toString().isEmpty()){
            mDescription = mPropertyDescription.getText().toString();
        }else{
            mPropertyDescription.setError("You must add a description.");
            values = false;
        }
        if (!mPropertySurface.getText().toString().isEmpty()){
            mSurface = Integer.valueOf(mPropertySurface.getText().toString());
        }else{
            mPropertySurface.setError("You must add a surface.");
            values = false;
        }
        if (!mPropertyNbrOfRooms.getText().toString().isEmpty()){
            mNbrOfRoom = Integer.valueOf(mPropertyNbrOfRooms.getText().toString());
        }else{
            mPropertyNbrOfRooms.setError("You must add a number of rooms.");
            values = false;
        }
        if (!mPropertyAddressStreetNumber.getText().toString().isEmpty()){
            mAddressStreetNbr = Integer.valueOf(mPropertyAddressStreetNumber.getText().toString());
        }else{
            mPropertyAddressStreetNumber.setError("You must add a number of street.");
            values = false;
        }
        if (!mPropertyAddressStreet.getText().toString().isEmpty()){
            mAddressStreet = mPropertyAddressStreet.getText().toString();
        }else{
            mPropertyAddressStreet.setError("You must add a street.");
            values = false;
        }
        if (!mPropertyAddressDistrict.getText().toString().isEmpty()){
            mAddressDistrict = mPropertyAddressDistrict.getText().toString();
        }else{
            mPropertyAddressDistrict.setError("You must add a district.");
            values = false;
        }
        if (!mPropertyAddressState.getText().toString().isEmpty()){
            mAddressState = mPropertyAddressState.getText().toString();
        }else{
            mPropertyAddressState.setError("You must add a State.");
            values = false;
        }
        if (!mPropertyAddressPostCode.getText().toString().isEmpty()){
            mAddressPostCode = Integer.valueOf(mPropertyAddressPostCode.getText().toString());
        }else{
            mPropertyAddressPostCode.setError("You must add a postcode.");
            values = false;
        }
        if (!mPropertyAddressCountry.getText().toString().isEmpty()){
            mAddressCountry = mPropertyAddressCountry.getText().toString();
        }else{
            mPropertyAddressCountry.setError("You must add a country.");
            values = false;
        }

        if (mCheckboxPark.isChecked()){
            mListFacilities.add("Park");
        }
        if (mCheckboxSchool.isChecked()){
            mListFacilities.add("School");
        }
        if (mCheckboxStation.isChecked()){
            mListFacilities.add("Station");
        }
        if (mCheckboxStore.isChecked()){
            mListFacilities.add("Store");
        }

        if (dateOfEntry == null){
            Snackbar.make(getView(),"You have to select the entry date.",Snackbar.LENGTH_LONG).show();
            values = false;
        }

        if (mCheckboxIsSold.isChecked()){
            mIsSold = true;
            if (dateOfSale == null){
                Snackbar.make(getView(),"You have to select the sold out date.",Snackbar.LENGTH_LONG).show();
                values = false;
            }
        }else {
            mIsSold = false;
            dateOfSale = null;
        }

        return values;
    }

    /**
     * Update property Entity in RoomDatabase
     */
    private void setValuesOfPropertyAndUpdate() {
        Property property = new Property();
        property.setId(mProperty.getId());
        property.setType(mType);
        property.setPrice(mPrice);
        property.setSurface(mSurface);
        property.setNbrOfRooms(mNbrOfRoom);
        property.setDescription(mDescription);
        property.setFacilities(mListFacilities);
        property.setSold(mIsSold);
        property.setAddedDate(dateOfEntry);
        property.setDateOfSale(dateOfSale);
        property.setUserId(userId);
        property.setPictureList(mListPictures);
        //for address
        Address address = new Address();
        address.setNumber(mAddressStreetNbr);
        address.setStreet(mAddressStreet);
        address.setDistrict(mAddressDistrict);
        address.setState(mAddressState);
        address.setPostCode(mAddressPostCode);
        address.setCountry(mAddressCountry);
        property.setAddress(address);

        mPropertiesViewModel.updateProperty(property);
        showConfirmationMessage("updated");
        getActivity().finish();
    }

    /**
     * Create new Property and address related
     */
    private void createNewPropertyAndAddress() {
        Address address = new Address(mAddressStreetNbr, mAddressStreet, null, mAddressDistrict,
            mAddressState, mAddressPostCode, mAddressCountry);
        mProperty = new Property(mType, mPrice, mSurface, mNbrOfRoom, mDescription, mIsSold, mListFacilities, dateOfEntry, dateOfSale, address, mListPictures, userId);
        mPropertiesViewModel.createProperty(mProperty);
        PropertyManagerFragment.this.showConfirmationMessage("added");
        getActivity().finish();
    }

    /**
     * Manage click on picture
     * @param picture
     */
    @Override
    public void onClickPicture(Picture picture) {
        pictureToDelete = picture;
        Bitmap bitmap = StorageUtils.getBitmapFromStorage(getActivity().getFilesDir(),getContext(),picture.getFile(),FOLDERNAME);
        DialogDeleteImage dialog = new DialogDeleteImage(bitmap);
        dialog.setTargetFragment(this,1);
        dialog.show(getParentFragmentManager(),"DialogImagePreview");
    }

    /**
     * Manage click to delete picture from list to show
     * And add to list we will delete in database on save property
     * @param dialogDeleteImage
     */
    @Override
    public void onDialogDeleteClick(DialogDeleteImage dialogDeleteImage) {
        mListPictures.remove(pictureToDelete);
        mAdapter.updateListPictures(mListPictures);
    }

    /**
     * Showing Confirmation message in notification
     * @param message
     */
    private void showConfirmationMessage(String message){
        String messageText = "The property "+ mProperty.getType()
            +" on the number "+ mProperty.getAddress().getNumber()
            +" of the street \"" + mProperty.getAddress().getStreet()
            +"\" in the district of \""+ mProperty.getAddress().getDistrict()
            +"\" was successfully "+message+"!";
        NotificationService notificationService = new NotificationService(getContext());
        notificationService.sendNotification(1,messageText);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            dispatchChoosePictureFromAlbum();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    private void showDatePikerDialog() {
        DialogEntryDatePicker dialog = new DialogEntryDatePicker();
        dialog.setTargetFragment(this,2);
        dialog.show(getParentFragmentManager(),"DialogDatePickerFragment");
    }

    private void showSoldDatePikerDialog() {
        DialogSoldDatePiker dialog = new DialogSoldDatePiker();
        dialog.setTargetFragment(this,2);
        dialog.show(getParentFragmentManager(),"DialogDatePickerFragment");
    }

    @Override
    public void onDialogDatePikerValidateClick(DialogEntryDatePicker dialog) {
        DatePicker datePicker = dialog.getDialog().findViewById(R.id.entry_date_dp);

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        dateOfEntry = calendar.getTime();
        mEntryDateTv.setText(Utils.formatDate(dateOfEntry));
    }

    @Override
    public void onDialogSoldDatePikerValidateClick(DialogSoldDatePiker dialog) {
        DatePicker datePicker = dialog.getDialog().findViewById(R.id.entry_date_dp);

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        dateOfSale = calendar.getTime();
        mSoldDateSelector.setText(Utils.formatDate(dateOfSale));
    }
}
