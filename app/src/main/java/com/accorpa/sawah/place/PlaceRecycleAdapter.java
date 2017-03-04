package com.accorpa.sawah.place;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
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
import com.accorpa.sawah.custom_views.CustomTextView;
import com.accorpa.sawah.models.Place;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.vision.text.Line;


import java.net.PortUnreachableException;
import java.util.ArrayList;

/**
 * Created by root on 26/02/17.
 */

public class PlaceRecycleAdapter extends  RecyclerView.Adapter<PlaceRecycleAdapter.ViewHolder> {


    private Context mContext;
//    private LayoutInflater mInflater;
    private View convertView;
    private ArrayList<Place> mDataSource;

    private RecycleAdapterListener mListener;

    private boolean addLikeButton, addDeleteButton, specialPlaceLayout;

    private Animation rotation;

    public PlaceRecycleAdapter(Context mContext, RecycleAdapterListener mListener,
                               boolean addLikeButton,
                               boolean specialPlaceLayout,
                               boolean addDeleteButton) {
        this.mContext = mContext;

        this.mDataSource = new ArrayList<>();
        this.mListener =  mListener;

        this.addLikeButton = addLikeButton;
        this.specialPlaceLayout = specialPlaceLayout;
        this.addDeleteButton = addDeleteButton;

        rotation = AnimationUtils.loadAnimation(mContext, R.anim.shake);
    }

    @Override
    public PlaceRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_item, parent, false);
        ViewHolder vh = new ViewHolder(convertView, addLikeButton);
        return vh;
    }

    @Override
    public void onBindViewHolder(final PlaceRecycleAdapter.ViewHolder holder, int position) {

        final Place place = mDataSource.get(position);
        System.out.println("Place: "+place.getPalceNameEng() + "----" + place.isSpecial());
        if(place.isSpecial() || !specialPlaceLayout)
        {
            holder.specialCard.setVisibility(View.VISIBLE);
            holder.notSpecialCard.setVisibility(View.GONE);
        }
        else {
            holder.specialCard.setVisibility(View.GONE);
            holder.notSpecialCard.setVisibility(View.VISIBLE);
        }

        holder.titleArabic.setText(place.getPalceNameArb());
        holder.titleEnglish.setText(place.getPalceNameEng());

        holder.titleArabicSpecial.setText(place.getPalceNameArb());
        holder.titleEnglishSpecial.setText(place.getPalceNameEng());

        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= place.getImageLocation().replaceAll(" ", "%20");


        LayerDrawable layer = (LayerDrawable) holder.mNetworkImageView.getBackground();
        if(layer != null) {
            AnimationDrawable frameAnimation = (AnimationDrawable) layer.getDrawable(0);
            frameAnimation.start();
        }

        layer = (LayerDrawable) holder.mNetworkImageViewSpecial.getBackground();
        if(layer != null) {
            AnimationDrawable frameAnimation = (AnimationDrawable) layer.getDrawable(0);
            frameAnimation.start();
        }

        holder.mNetworkImageView.setImageUrl(imageUrl, mImageLoader);
        holder.mNetworkImageViewSpecial.setImageUrl(imageUrl, mImageLoader);


        holder.specialTag.setVisibility(place.isSpecial() && specialPlaceLayout ?
                View.VISIBLE : View.GONE);

        holder.specialTagSpecial.setVisibility(place.isSpecial() && specialPlaceLayout ?
                View.VISIBLE : View.GONE);

        if(addLikeButton){

            if(place.isFavourite()){
                holder.likeButton.setChecked();
                holder.likeButtonSpecial.setChecked();
            }else{
                holder.likeButton.setUnChecked();
                holder.likeButtonSpecial.setUnChecked();
            }

            holder.likeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DataHandler.getInstance(mContext).togglePlaceFavourite(place);
                    holder.likeButton.toggleState();
                }
            });

            holder.likeButtonSpecial.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DataHandler.getInstance(mContext).togglePlaceFavourite(place);
                    holder.likeButtonSpecial.toggleState();
                }
            });
        }

        holder.specialTag.setVisibility(specialPlaceLayout && place.isSpecial() ? View.VISIBLE : View.GONE);
        holder.specialTagSpecial.setVisibility(specialPlaceLayout && place.isSpecial() ? View.VISIBLE : View.GONE);
        System.out.println(this);
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

        public TextView titleArabicSpecial;
        public TextView titleEnglishSpecial;
        public NetworkImageView mNetworkImageViewSpecial;
        public CustomCheckBox likeButtonSpecial;
        public LinearLayout specialTagSpecial;

        public CardView specialCard;
        public CardView notSpecialCard;
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

            this.mNetworkImageViewSpecial = (NetworkImageView) v.findViewById(R.id.special_icon);
            mNetworkImageViewSpecial.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
            this.titleArabicSpecial = (TextView) v.findViewById(R.id.special_place_title_ar);
            this.titleEnglishSpecial = (TextView) v.findViewById(R.id.special_place_title_en);
            this.likeButtonSpecial = (CustomCheckBox) v.findViewById(R.id.special_like_button);
            this.specialTagSpecial = (LinearLayout) v.findViewById(R.id.special_special_place_tag);
            likeButtonSpecial.setVisibility(addLikeButton? View.VISIBLE : View.GONE);
            likeButtonSpecial.setBackgroundResIDs(R.drawable.heart_active, R.drawable.heart);

            this.specialCard = (CardView) v.findViewById(R.id.special_card);
            this.notSpecialCard = (CardView)v.findViewById(R.id.not_special_card);

        }
    }

}
