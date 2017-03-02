package com.accorpa.sawah.place;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
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


import java.net.PortUnreachableException;
import java.util.ArrayList;

/**
 * Created by root on 26/02/17.
 */

public class PlaceRecycleAdapter extends  RecyclerView.Adapter<PlaceRecycleAdapter.ViewHolder> {


    private Context mContext;
//    private LayoutInflater mInflater;

    private ArrayList<Place> mDataSource;

    private RecycleAdapterListener mListener;

    private boolean addLikeButton, addDeleteButton, specialPlaceLayout;

    private Animation rotation;

    public PlaceRecycleAdapter(Context mContext, RecycleAdapterListener mListener,
                               boolean addLikeButton,
                               boolean specialPlaceLayout) {
        this.mContext = mContext;

        this.mDataSource = new ArrayList<>();
        this.mListener =  mListener;

        this.addLikeButton = addLikeButton;
        this.specialPlaceLayout = specialPlaceLayout;

        rotation = AnimationUtils.loadAnimation(mContext, R.anim.shake);


    }

    @Override
    public PlaceRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_item, parent, false);


        ViewHolder vh = new ViewHolder(convertView, addLikeButton);
        return vh;
    }

    @Override
    public void onBindViewHolder(final PlaceRecycleAdapter.ViewHolder holder, int position) {

        final Place place = mDataSource.get(position);

        holder.titleArabic.setText(place.getPalceNameArb());
        holder.titleEnglish.setText(place.getPalceNameEng());

        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= place.getImageLocation().replaceAll(" ", "%20");


        AnimationDrawable frameAnimation = (AnimationDrawable) holder.mNetworkImageView.getBackground();
        frameAnimation.start();

        holder.mNetworkImageView.setImageUrl(imageUrl, mImageLoader);


        holder.specialTag.setVisibility(place.isSpecial() && specialPlaceLayout ?
                View.VISIBLE : View.GONE);



        if(addLikeButton){

            if(place.isFavourite()){
                holder.likeButton.setChecked();
            }else{
                holder.likeButton.setUnChecked();
            }

            holder.likeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DataHandler.getInstance(mContext).togglePlaceFavourite(place);
                    holder.likeButton.toggleState();
                }
            });
        }

        holder.specialTag.setVisibility(specialPlaceLayout && place.isSpecial() ? View.VISIBLE : View.GONE);

        if(addDeleteButton) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.v.startAnimation(rotation);

        }else {
            holder.deleteButton.setVisibility(View.GONE);
            holder.v.clearAnimation();
        }

        if(addDeleteButton){
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    holder.v.clearAnimation();
                    DataHandler.getInstance(mContext).togglePlaceFavourite(place);
                    mDataSource.remove(place);
                    holder.v.clearAnimation();
                    PlaceRecycleAdapter.this.notifyDataSetChanged();


                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.v.clearAnimation();
                mListener.itemSelected(place);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

//    @Override
//    public double aspectRatioForIndex(int i) {
//        return 1;
//    }

    public void setDataSource(ArrayList<Place> dataSource) {
        this.mDataSource = dataSource;
    }

    public void setShowDeletionButton(boolean showDeleteButton) {
        addDeleteButton = showDeleteButton;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleArabic;
        public TextView titleEnglish;
        public NetworkImageView mNetworkImageView;
        public CustomCheckBox likeButton;
        public CustomCheckBox deleteButton;
        public LinearLayout specialTag;
        public View v;

        public ViewHolder(View v, boolean addLikeButton) {
            super(v);
            this.v = v;
            this.mNetworkImageView = (NetworkImageView) v.findViewById(R.id.icon);
            mNetworkImageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
            this.titleArabic = (TextView) v.findViewById(R.id.place_title_ar);
            this.titleEnglish = (TextView) v.findViewById(R.id.place_title_en);
            this.likeButton = (CustomCheckBox) v.findViewById(R.id.like_button);
            likeButton.setVisibility(addLikeButton? View.VISIBLE : View.GONE);
            likeButton.setBackgroundResIDs(R.drawable.heart_active, R.drawable.heart);
            this.specialTag = (LinearLayout) v.findViewById(R.id.special_place_tag);
            this.deleteButton = (CustomCheckBox) v.findViewById(R.id.delete_button);

        }
    }

}
