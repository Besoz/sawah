package com.accorpa.sawah.custom_views;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.accorpa.sawah.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageViewerFragment extends Fragment {
    private Bitmap bitmap;
    private ImageView iv;
    private View fragment;
    public ImageViewerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        fragment = inflater.inflate(R.layout.fragment_image_viewer, container, false);
        setImageViewBitmap(bitmap);
        return fragment;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public void setImageViewBitmap(Bitmap bitmap)
    {
        if (iv == null)
        {
            iv = (ImageView) fragment.findViewById(R.id.image);
        }
        iv.setImageBitmap(bitmap);
    }
}
