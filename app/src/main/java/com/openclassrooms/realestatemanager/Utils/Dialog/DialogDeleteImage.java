package com.openclassrooms.realestatemanager.Utils.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.openclassrooms.realestatemanager.R;

public class DialogDeleteImage extends DialogFragment {

    public interface DialogDeleteListener{
        void onDialogDeleteClick(DialogDeleteImage dialogDeleteImage);
    }

    DialogDeleteListener mListener;

    private Bitmap mBitmap;

    public DialogDeleteImage(Bitmap bitmap){
        this.mBitmap = bitmap;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_image_show,null);
        builder.setView(view)
            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mListener.onDialogDeleteClick(DialogDeleteImage.this);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
        ImageView image = view.findViewById(R.id.dialog_picture_iv);
        image.setImageBitmap(this.mBitmap);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (DialogDeleteImage.DialogDeleteListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException(getTargetFragment().toString()+" must implement DialogDeleteListener");
        }
    }
}
