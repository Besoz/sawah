package com.accorpa.sawah.place;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.R;
import com.accorpa.sawah.RecycleAdapterListener;
import com.accorpa.sawah.custom_views.CustomCheckBox;
import com.accorpa.sawah.models.Place;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by root on 26/02/17.
 */

public class PlaceRecycleAdapter extends  RecyclerView.Adapter<PlaceRecycleAdapter.ViewHolder> {

    private Context mContext;
//    private LayoutInflater mInflater;

    private Place[] mDataSource;

    private RecycleAdapterListener mListener;


    public PlaceRecycleAdapter(Context mContext, RecycleAdapterListener mListener) {
        this.mContext = mContext;

        this.mDataSource = new Place[0];
        this.mListener =  mListener;
    }

    @Override
    public PlaceRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_item, parent, false);

        NetworkImageView mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.icon);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.place_title_ar);
        TextView detailTextView  = (TextView) convertView.findViewById(R.id.place_title_en);
        CustomCheckBox customCheckBox = (CustomCheckBox) convertView.findViewById(R.id.like_button);
        customCheckBox.setBackgroundResIDs(R.drawable.heart_active, R.drawable.heart);
        mNetworkImageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
        LinearLayout specailTag = (LinearLayout) convertView.findViewById(R.id.special_place_tag);

        ViewHolder vh = new ViewHolder(convertView, mNetworkImageView, titleTextView, detailTextView,
        customCheckBox, specailTag);
        return vh;
    }

    @Override
    public void onBindViewHolder(final PlaceRecycleAdapter.ViewHolder holder, int position) {

        final Place place = mDataSource[position];

        holder.titleArabic.setText(place.getPalceNameArb());
        holder.titleEnglish.setText(place.getPalceNameEng());

        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= place.getImageLocation().replaceAll(" ", "%20");


        AnimationDrawable frameAnimation = (AnimationDrawable) holder.mNetworkImageView.getBackground();
        frameAnimation.start();

        holder.mNetworkImageView.setImageUrl(imageUrl, mImageLoader);

        if(place.isFavourite()){
            holder.customCheckBox.setChecked();
        }else{
            holder.customCheckBox.setUnChecked();
        }

        if(place.isSpecial()){
            holder.specialTag.setVisibility(View.VISIBLE);
        }else{
            holder.specialTag.setVisibility(View.GONE);
        }

        holder.customCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DataHandler.getInstance(mContext).togglePlaceFavourite(place);
                holder.customCheckBox.toggleState();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemSelected(place);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSource.length;
    }

//    @Override
//    public double aspectRatioForIndex(int i) {
//        return 1;
//    }

    public void setDataSource(Place[] dataSource) {
        this.mDataSource = dataSource;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleArabic;
        public TextView titleEnglish;
        public NetworkImageView mNetworkImageView;
        public CustomCheckBox customCheckBox;
        public LinearLayout specialTag;

        public ViewHolder(View v, NetworkImageView mNetworkImageView, TextView titleTextView,
                          TextView titleEnglish, CustomCheckBox customCheckBox,
                          LinearLayout specialTag) {
            super(v);
            this.mNetworkImageView = mNetworkImageView;
            this.titleArabic = titleTextView;
            this.titleEnglish = titleEnglish;
            this.customCheckBox = customCheckBox;
            this.specialTag = specialTag;
        }
    }

}
