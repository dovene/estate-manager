package com.openclassrooms.realestatemanager.UI.Fragment.ListProperties;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.Model.Picture;
import com.openclassrooms.realestatemanager.Model.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.StorageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.realestatemanager.UI.Fragment.PropertyManager.PropertyManagerFragment.FOLDERNAME;

public class PropertiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.property_list_picture_iv)
    ImageView mPictureImageView;
    @BindView(R.id.item_property_type_text_view)
    TextView mPropertyType;
    @BindView(R.id.item_property_price_text_view)
    TextView mPropertyPrice;
    @BindView(R.id.item_property_district_text_view)
    TextView mPropertyDistrict;
    @BindView(R.id.item_list_property)
    ConstraintLayout mItemView;
    @BindView(R.id.sold_out_stamp_iv)
    ImageView soldOutStamp;

    private final Context mContext;
    private Property mProperty;

    private OnPropertyListener mOnPropertyListener;

    public interface OnPropertyListener{
        void onPropertyClick(int propertyId);
    }

    //CONSTRUCTOR
    public PropertiesViewHolder(@NonNull View itemView, Context context, OnPropertyListener propertyListener) {
        super(itemView);
        this.mContext = context;
        this.mOnPropertyListener = propertyListener;
        ButterKnife.bind(this, itemView);
    }

    /**
     * Update view with property values
     * @param property
     * @param index
     */
    public void updateView(Property property, int index) {
        this.mProperty = property;

        //get first image of property image list if not empty
        if (!mProperty.getPictureList().isEmpty()) {
            Bitmap bitmap = StorageUtils.getBitmapFromStorage(mContext.getFilesDir(), mContext, property.getPictureList().get(0).getFile(), FOLDERNAME);
            mPictureImageView.setImageBitmap(bitmap);
        }
        mPropertyType.setText(property.getType());
        mPropertyPrice.setText("$"+ String.format("%,d", property.getPrice()));
        mPropertyDistrict.setText(property.getAddress().getDistrict());
        if (property.getSold()){
            soldOutStamp.setVisibility(View.VISIBLE);
        }else{
            soldOutStamp.setVisibility(View.GONE);
        }
        mItemView.setOnClickListener(this);
        changeBackgroundColor(index);
    }

    private void changeBackgroundColor(int index) {
        if(index == mProperty.getId()){
            mItemView.setBackgroundResource(R.color.primaryLightColor);
            mPropertyPrice.setTextColor(ContextCompat.getColor(mContext,R.color.secondaryLightColor));
            mPropertyType.setTextColor(ContextCompat.getColor(mContext,R.color.primaryTextColor));
            mPropertyDistrict.setTextColor(ContextCompat.getColor(mContext,R.color.primaryTextColor));
        }else{
            mItemView.setBackgroundResource(R.color.primaryTextColor);
            mPropertyPrice.setTextColor(ContextCompat.getColor(mContext,R.color.secondaryDarkColor));
            mPropertyType.setTextColor(ContextCompat.getColor(mContext,R.color.primaryDarkColor));
            mPropertyDistrict.setTextColor(ContextCompat.getColor(mContext,R.color.primaryDarkColor));
        }
    }

    @Override
    public void onClick(View v) {
        mOnPropertyListener.onPropertyClick(mProperty.getId());
    }
}
