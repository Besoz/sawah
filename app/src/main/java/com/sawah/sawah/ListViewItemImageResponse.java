package com.sawah.sawah;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by root on 19/01/17.
 */

class ListViewItemImageResponse implements Response.Listener<Bitmap> {

    RecyclerView.ViewHolder holder;

    public ListViewItemImageResponse(RecyclerView.ViewHolder holder) {
        this.holder = holder;
    }

    @Override
    public void onResponse(Bitmap response) {

    }
}
