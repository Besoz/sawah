package com.accorpa.sawah.place;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.accorpa.sawah.R;
import com.accorpa.sawah.custom_views.CustomCheckBox;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by root on 13/02/17.
 */

public class PlaceDoubleViewHolder extends RecyclerView.ViewHolder{

    public TextView titleArabic;
    //        public TextView subtitleTextView;
    public TextView titleEnglish;
    public NetworkImageView mNetworkImageView;
    public CustomCheckBox customCheckBox;

    public PlaceDoubleViewHolder(View itemView, View v, View v2) {
        super(itemView);

        mNetworkImageView = (NetworkImageView) itemView.findViewById(R.id.icon);
        titleArabic = (TextView) itemView.findViewById(R.id.place_title_ar);
        titleEnglish = (TextView) itemView.findViewById(R.id.place_title_en);
        customCheckBox = (CustomCheckBox) itemView.findViewById(R.id.like_button);

        customCheckBox.setBackgroundResIDs(R.drawable.heart_active, R.drawable.heart);
    }
}
