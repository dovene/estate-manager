package com.openclassrooms.realestatemanager.UI.Fragment.PropertyManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.Model.Picture;
import com.openclassrooms.realestatemanager.R;

import java.util.ArrayList;
import java.util.List;

public class PicturesRecyclerViewAdapter extends RecyclerView.Adapter<PicturesViewHolder> {

    private final Context mContext;
    private PicturesViewHolder.ListenerPictureClick mCallback;
    private List<Picture> mListPictures = new ArrayList<>();

    //CONSTRUCTOR
    public PicturesRecyclerViewAdapter(Context context, PicturesViewHolder.ListenerPictureClick callback){
        this.mContext = context;
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public PicturesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture,parent,false);
        return new PicturesViewHolder(view,mContext,mCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull PicturesViewHolder holder, int position) {
        holder.updateView(mListPictures.get(position));
    }

    @Override
    public int getItemCount() {
        if (mListPictures == null){
            return 0;
        }else{
            return mListPictures.size();
        }
    }

    public void updateListPictures(List<Picture> listPictures){
        this.mListPictures = listPictures;
        notifyDataSetChanged();
    }
}
